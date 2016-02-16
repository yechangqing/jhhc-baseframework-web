package com.jhhc.baseframework.web.core;

import com.jhhc.baseframework.record.Record;
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
abstract public class CoreTable implements Core {

    private String id;
    private String table;
    private Map<String, Object> hv;
    private Map<String, Object> st_hv;   // 对hv进行标准化后
    Set<String> header;
    private Record rec;

    abstract public String getTable();

    protected CoreTable() {
        String table1 = getTable();
        if (table1 == null || table1.trim().equals("")) {
            throw new IllegalArgumentException("表名称为空");
        }
        this.table = table1.trim();
        this.header = new HashSet();
        initHeader(this.table);
        this.rec = Root.getInstance().getRecord();
    }

    private void initHeader(String table) {
        try {
            String[] names = Root.getInstance().getSqlOperator().getHeader(table);
            for (int i = 0; i < names.length; i++) {
                this.header.add(names[i]);
            }
        } catch (Throwable e) {
            throw new IllegalArgumentException("表" + table + "不存在");
        }
    }

    protected CoreTable(String id) {
        this();
//        this.rec.init1(this.table, id);
//        this.id = id.trim();
//        this.hv = null;
        changeId(id);
    }

    protected CoreTable(Map<String, Object> hv) {
        this();
//        this.st_hv = analysisHv(hv);
//        this.rec.init0(this.table, this.st_hv);
//        this.hv = hv;
//        this.id = null;
        changeHv(hv);
    }

    // 同CoreView一样，格式可以是 字段名 或者 表名.字段名，如果不存在这个表或者字段，则抛出异常
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
            table = this.table;
            header1 = tmp[0].trim();
        } else if (tmp.length == 2) {
            table = tmp[0].trim();
            header1 = tmp[1].trim();
            if (!table.equals(this.table)) {
                throw new IllegalArgumentException("不是表" + this.table);
            }
            // 看看字段是否存在
            if (!this.header.contains(header1)) {
                throw new IllegalArgumentException("表" + table + "不含有字段" + header1);
            }
        } else {
            throw new IllegalArgumentException("参数格式为 表名.字段名 或 字段名");
        }

        return new String[]{table, header1};
    }

    // 解析hv字段，规范化可能有的 表名.字段名 的格式，统统恢复成 表名.字段名
    public Map<String, Object> analysisHv(Map<String, Object> hv) {
        Map<String, Object> ret = new HashMap();
        Iterator<Entry<String, Object>> ite = hv.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, Object> ent = ite.next();
            String k = ent.getKey().trim();
            String[] r = analysisTableHeader(k);
            ret.put(r[1], ent.getValue());
        }
        return ret;
    }

    // 返回标准化后的Hv
//    public Map<String, Object> getStandardizedHv() {
//        return this.st_hv;
//    }
    // 获取hv中的值，这里的key可以为 表名.字段名 或者字段名
    public Object getInfoOfHv(String key) {
        if (key == null || key.trim().equals("")) {
            throw new IllegalArgumentException("key值为空");
        }
        String[] r = analysisTableHeader(key);
        return this.st_hv.get(r[1]);
    }

    // 获取初始的hv
    protected Map<String, Object> getOriginHv() {
        return this.hv;
    }

    protected void setOriginHv(Map<String, Object> hv1) {
        this.hv = hv1;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Map<String, Object> getInfo() {
        return this.rec.getInfo();
    }

    @Override
    public Object getInfo(String key) {
        if (key == null || key.trim().equals("")) {
            throw new IllegalArgumentException("key值为空");
        }
        key = key.trim();
        if (!this.header.contains(key)) {
            throw new IllegalArgumentException("不含有属性" + key);
        }
        Map map = getInfo();
        return map.get(key);
    }

    @Override
    public <T> T getInfo(String key, Class<T> cls) {
        Object ret = getInfo(key);
        if (!ret.getClass().equals(cls)) {
            throw new IllegalArgumentException("Class类型不符合");
        }
        return (T) ret;
    }

    @Notify
    @Override
    public String add() {
        return this.rec.add();
    }

    @Notify
    @Override
    public void modify(Map<String, Object> hv1) {
        this.rec.modify(analysisHv(hv1));
    }

    @Notify
    @Override
    public void remove() {
        this.rec.delete();
    }

    @Override
    public CoreMetaData getMetaData() {
        return new MetaData4Table(this);
    }

    // 有需求会临时改变id
    protected void changeId(String id) {
        this.rec.init1(this.table, id);
        this.id = id.trim();
        this.hv = null;
    }

    // 有需求会临时改变hv
    protected void changeHv(Map<String, Object> hv1) {
        this.st_hv = analysisHv(hv1);
        this.rec.init0(this.table, this.st_hv);
        this.hv = hv1;
        this.id = null;
    }
}
