package com.jhhc.baseframework.web.core;

import com.jhhc.baseframework.record.Record;
import com.jhhc.baseframework.record.RecordView;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author yecq
 */
abstract public class CoreView implements Core {

    private String id;      // 主表的id
    private Map<String, Object> hv;  // 字段集，字段格式为： 字段， 或者 表名.字段
    private Map<String, Map<String, Object>> st_hv;   // 规范化后的hv，包括视图

    private String view;    // 视图名称
    Set<String> header; // 视图的字段
    private RecordView rec;

    private Map<String, String> id_table;    // 分支表的主键名对应于表名， id名->表名
    Map<String, Set<String>> tb_headers;  // 各表的表头
    Map<String, CoreTable> cores;   // 表名->分支表Core，注意这是内部类，没有Map的那个构造方法

    // 返回视图名
    abstract public String getView();

    // 主表，格式为 表名.id 或者 表名（默认主键字段为id)，如user、user.id、user.u_id
    abstract public String getMainTable();

    // 各从表，每一项格式为 表名.主键字段名，不符合此格式会抛出IllegalArgumentException，格式如上
    abstract public String[] getSlaveTables();

    protected CoreView() {
        // 检查view
        String view1 = getView();
        if (view1 == null || view1.trim().equals("")) {
            throw new IllegalArgumentException("视图名称为空");
        }

        // 初始化rec，view
        this.view = view1.trim();
        this.rec = Root.getInstance().getRecordView();

        initHeader();
        initIdTableAndTbHeaders();
    }

    private void initHeader() {
        this.header = new HashSet();
        try {
            String[] names = Root.getInstance().getSqlOperator().getHeader(this.view);
            for (int i = 0; i < names.length; i++) {
                this.header.add(names[i]);
            }
        } catch (Throwable e) {
            throw new IllegalArgumentException("视图" + view + "不存在");
        }
    }

    private void initIdTableAndTbHeaders() {
        this.id_table = new HashMap();
        this.tb_headers = new HashMap();

        // 先操作main table
        String str = getMainTable();
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("main table为空");
        }
        str = str.trim();
        String[] ret = analysisTableId(str);
        String idt_id = (ret[1] == null || ret[1].equals("")) ? "id" : ret[1];
        // 检查下有没有这个id字段
        if (!this.header.contains(idt_id)) {
            throw new IllegalArgumentException("视图中不含有字段" + idt_id);
        }
        this.id_table.put(idt_id, ret[0]);
        do_initTbHeaders(ret[0]);

        String[] strs = getSlaveTables();
        if (strs == null || strs.length == 0) {
            throw new IllegalArgumentException("slave table为空");
        }
        for (int i = 0; i < strs.length; i++) {
            str = strs[i];
            if (str == null || str.trim().equals("")) {
                throw new IllegalArgumentException("slave table表项为空");
            }
            str = str.trim();
            ret = analysisTableId(str);
// 可能视图中不含有子表的id字段
            if (ret[1] != null) {
                idt_id = ret[1];
                if (!this.header.contains(idt_id)) {
                    throw new IllegalArgumentException("视图中不含有字段" + idt_id);
                }
                this.id_table.put(idt_id, ret[0]);
            }
            do_initTbHeaders(ret[0]);
        }
    }

    private void do_initTbHeaders(String table) {
        String[] h = Root.getInstance().getSqlOperator().getHeader(table);
        Set<String> set = new HashSet();
        for (int i = 0; i < h.length; i++) {
            set.add(h[i]);
        }
        this.tb_headers.put(table, set);
    }

    // 检查 表名.id名 的格式，并检查表以及id名称在不在
    public String[] analysisTableId(String str) {
        String[] ret = new String[2];

        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("参数格式为 表名.字段名 或 字段名");
        }
        str = str.trim();
        if (str.startsWith(".") || str.endsWith(".")) {
            throw new IllegalArgumentException("参数格式为 表名.字段名 或 字段名");
        }

        String[] tmp = str.split("\\.");;
        if (tmp.length == 1) {
            // 只有表名
            ret[0] = tmp[0].trim();
            ret[1] = null;
        } else if (tmp.length == 2) {
            ret[0] = tmp[0].trim();
            ret[1] = tmp[1].trim();
        } else {
            throw new IllegalArgumentException("参数格式为 表名.字段名 或 字段名");
        }

        // 检查表在不在
        Root.getInstance().getSqlOperator().checkTable(ret[0]);

        // 检查id名称在不在
        if (ret[1] != null && !this.header.contains(ret[1])) {
            throw new IllegalArgumentException("视图不存在字段" + ret[1]);
        }
        return ret;
    }

    protected CoreView(Map<String, Object> hv1) {
        this();
//        if (hv1 == null) {
//            throw new IllegalArgumentException("hv为空");
//        }
//        // 分析hv1
//        // 现在可以允许带视图名的
//        this.st_hv = analysisHvIncludeView(hv1);
//        this.hv = hv1;
//        this.id = null;
        changeHv(hv1);
    }

    // 解析hv的格式，格式为 键->值,键的格式为 字段名 或者 表名.字段名
    // 返回的map格式为 表名->map1，map1格式为 字段名->值
    // 如果表或者字段不存在，则抛出异常
    public Map<String, Map<String, Object>> analysisHv(Map<String, Object> hv1) {
        Map<String, Map<String, Object>> ret = new HashMap();
        if (hv1 == null) {
            return ret;
        }

        // 初始化ret，放入所有的表
        Iterator<String> iteret = this.tb_headers.keySet().iterator();
        while (iteret.hasNext()) {
            String tb = iteret.next();
            ret.put(tb, new HashMap());
        }

        // 解析字段
        Iterator<Map.Entry<String, Object>> ite = hv1.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, Object> ent = ite.next();
            String k = ent.getKey().trim();
            Object value = ent.getValue();

            String[] r = analysisTableHeader(k);

            // 放入相应的map
            ret.get(r[0]).put(r[1], value);
        }
        return ret;
    }

    // 包含视图字段的解析
    public Map<String, Map<String, Object>> analysisHvIncludeView(Map<String, Object> hv1) {
        // 先分析视图

        Map<String, Map<String, Object>> ret = new HashMap();
        Map<String, Object> tmp = new HashMap();
        Iterator<Entry<String, Object>> ite = hv1.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, Object> ent = ite.next();
            String k = ent.getKey().trim();
            String[] r = analysisViewHeader(k);
            if (r != null) {
                tmp.put(r[1], ent.getValue());
                // 删除直接表明视图的那个，不然在hv的检查中会报错
                String[] kk = k.split("\\.");
                if (kk.length == 2) {
                    ite.remove();
                }
            }
        }

        ret.put(this.view, tmp);

        ret.putAll(analysisHv(hv1));
        return ret;
    }

    // 解析并分析字段格式，如果不存在这个表或者字段，则抛出异常
    public String[] analysisTableHeader(String str) {
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("参数格式为 表名.字段名 或 字段名");
        }
        str = str.trim();
        if (str.startsWith(".") || str.endsWith(".")) {
            throw new IllegalArgumentException("参数格式为 表名.字段名 或 字段名");
        }

        String table = null;
        String header1 = null;

        String[] tmp = str.split("\\.");
        if (tmp.length == 1) {
            // 只有字段名
            header1 = tmp[0].trim();
            // 看看是哪个表的
            table = getTableByHeaderName(header1);
        } else if (tmp.length == 2) {
            table = tmp[0].trim();
            header1 = tmp[1].trim();
            // 看看这个表是否存在
            if (!this.tb_headers.containsKey(table)) {
                throw new IllegalArgumentException("视图不含有表" + table);
            }
            if (!this.tb_headers.get(table).contains(header1)) {
                throw new IllegalArgumentException("表" + table + "不含有字段" + header1);
            }
        } else {
            throw new IllegalArgumentException("参数格式为 表名.字段名 或 字段名");
        }

        return new String[]{table, header1};
    }

    // 解析并分析字段是否属于视图，如果不属于则返回null
    public String[] analysisViewHeader(String str) {
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("参数格式为 表名.字段名 或 字段名");
        }
        str = str.trim();
        if (str.startsWith(".") || str.endsWith(".")) {
            throw new IllegalArgumentException("参数格式为 表名.字段名 或 字段名");
        }

        String view1 = null;
        String header1 = null;

        String[] tmp = str.split("\\.");
        if (tmp.length == 1) {
            view1 = this.view;
            header1 = tmp[0];
        } else if (tmp.length == 2) {
            view1 = tmp[0];
            header1 = tmp[1];
        } else {
            throw new IllegalArgumentException("参数格式为 表名.字段名 或 字段名");
        }
        if (!view1.equals(this.view)) {
            return null;
        } else {
            return this.header.contains(header1) ? new String[]{view1, header1} : null;
        }
    }

    // 看某个字段是哪个表的，如果没有或者有歧义，则发出IllegalArgumentException
    private String getTableByHeaderName(String header) {
        header = header.trim();
        int count = 0;
        String table = null;
        Iterator<Map.Entry<String, Set<String>>> ite = this.tb_headers.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, Set<String>> ent = ite.next();
            String k1 = ent.getKey().trim();
            Set<String> set = ent.getValue();
            if (set.contains(header)) {
                table = k1;
                count++;
            }
        }

        if (count == 0) {
            throw new IllegalArgumentException("不存在字段" + header);
        }
        if (count > 1) {
            throw new IllegalArgumentException("有歧义的字段" + header);
        }
        return table;
    }

    // 返回规范化后的hv
//    public Map<String, Map<String, Object>> getStandardizedHv() {
//        return this.st_hv;
//    }
    protected CoreView(String id) {
        this();
//        if (id == null || id.trim().equals("")) {
//            throw new IllegalArgumentException("id为空");
//        }
//        Map ids = new HashMap();
//        id = id.trim();
//        ids.put("id", id);
//        this.rec.init1(view, ids);
//        this.id = id;
//        this.hv = null;
//
//        // 初始化core
//        initCores();
        changeId(id);
    }

    private void initCores() {
        this.cores = new HashMap();
        Iterator<Entry<String, String>> ite = this.id_table.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, String> entry = ite.next();
            String id_header = entry.getKey();
            final String table = entry.getValue();

            // 获得id的值
            String id1 = getInfo(id_header) + "";
            CoreTable core = new CoreTable(id1) {
                @Override
                public String getTable() {
                    return table;
                }
            };
            this.cores.put(table, core);
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    // 获得hv中的字段，key可以为 表名（视图名）.字段名，或者字段名
    public Object getInfoOfHv(String key) {
        if (key == null || key.trim().equals("")) {
            throw new IllegalArgumentException("key值为空");
        }
        String[] r = analysisViewHeader(key);
        if (r == null) {
            r = analysisTableHeader(key);
        }
        return this.st_hv.get(r[0]).get(r[1]);
    }

    // 获取初始的hv
    protected Map<String, Object> getOriginHv() {
        return this.hv;
    }

    protected void setOriginHv(Map<String, Object> hv1) {
        this.hv = hv1;
    }

    // 获取视图里的字段，不能获取表里的
    @Override
    public Map<String, Object> getInfo() {
        return this.rec.getInfo();
    }

    // 可以视图和其他表里的字段 key的格式为 字段名 或者 表名.字段名
    @Override
    public Object getInfo(String key) {
        if (key == null || key.trim().equals("")) {
            throw new IllegalArgumentException("key值为空");
        }
        key = key.trim();
        String[] r = analysisViewHeader(key);
        if (r != null) {
            // 在view中获取
            return this.rec.getInfo(r[1]);
        }
        r = analysisTableHeader(key);
        // 获取其他表里的数据
        return this.cores.get(r[0]).getInfo(r[1]);
    }

    @Override
    public <T> T getInfo(String key, Class<T> cls) {
        Object o = getInfo(key);
        if (!o.getClass().equals(cls)) {
            throw new IllegalArgumentException("Class类型不符合");
        }
        return (T) o;
    }

    @Notify
    @Override
    public String add() {
        // 分析字段
        Map<String, Map<String, Object>> param = analysisHv(this.hv);

        // 先添加主表
        String[] r = analysisTableId(getMainTable());
        String mainTb = r[0];
        Map<String, Object> map = param.remove(mainTb);
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException("必须有主表内容");
        }

        Record rec1 = Root.getInstance().getRecord();
        rec1.init0(mainTb, map);
        String main_id = rec1.add();

        // 后添加各个从表，顺序暂时不定
        Iterator<Map.Entry<String, Map<String, Object>>> ite = param.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, Map<String, Object>> entry = ite.next();
            String table = entry.getKey();
            map = entry.getValue();
            if (!map.isEmpty()) {
                // 加入外键
                rec1.init0(table, map);
                String[] keys = rec1.getForeignKeys();
                String forekey = null;
                for (int i = 0; i < keys.length; i++) {
                    // 此时keys[i]的格式为 表.字段->表.字段
                    String[] tmp = keys[i].split("->");
                    String[] t2 = tmp[1].split("\\.");
                    if (t2[0].equals(mainTb) && t2[1].equals("id")) {
                        forekey = tmp[0].split("\\.")[1];
                        break;
                    }
                }
                if (forekey == null) {
                    throw new IllegalStateException("表" + table + "没有关联到主表的外键");
                }

                map.put(forekey, main_id);
                rec1.add();
            }
        }

        return main_id;
    }

    @Notify
    @Override
    public void remove() {
        String[] r = analysisTableId(getMainTable());
        CoreTable core = this.cores.get(r[0]);
        core.remove();
    }

    @Notify
    @Override
    public void modify(Map<String, Object> hv1) {
        // 分析参数
        Map<String, Map<String, Object>> param = analysisHv(hv1);

        // 依次修改，先这么搞
        Iterator<Map.Entry<String, Map<String, Object>>> ite = param.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, Map<String, Object>> entry = ite.next();
            String k = entry.getKey();
            Map<String, Object> v = entry.getValue();
            if (!v.isEmpty()) {
                this.cores.get(k).modify(v);
            }
        }
    }

    @Override
    public CoreMetaData getMetaData() {
        return new MetaData4View(this);
    }

    // 临时更换id
    protected void changeId(String id) {
        if (id == null || id.trim().equals("")) {
            throw new IllegalArgumentException("id为空");
        }
        Map ids = new HashMap();
        id = id.trim();
        ids.put("id", id);
        this.rec.init1(view, ids);
        this.id = id;
        this.hv = null;

        // 初始化core
        initCores();
    }

    // 临时改变hv
    protected void changeHv(Map<String, Object> hv1) {
        if (hv1 == null) {
            throw new IllegalArgumentException("hv为空");
        }
        // 分析hv1
        // 现在可以允许带视图名的
        this.st_hv = analysisHvIncludeView(hv1);
        this.hv = hv1;
        this.id = null;
    }
}
