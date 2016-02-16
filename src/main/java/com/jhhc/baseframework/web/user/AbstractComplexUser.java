package com.jhhc.baseframework.web.user;

import com.jhhc.baseframework.web.core.CoreSelector;
import com.jhhc.baseframework.web.core.CoreView;
import com.jhhc.baseframework.web.core.Root;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author yecq
 */
public abstract class AbstractComplexUser implements User {

    private Map<String, Object> hv;
    private String id;
    private InnerCoreView core;

    protected AbstractComplexUser(String id) {
        this.core = new InnerCoreView(id);
        checkUnique();
        this.id = id;
        this.hv = null;
    }

    protected AbstractComplexUser(Map<String, Object> hv1) {
        this.core = new InnerCoreView(hv1);
        checkUnique();
        this.hv = hv1;
        this.id = null;
    }

    public String getUserTable() {
        return "user";
    }

    abstract protected String getInfoTable();

    abstract protected String getViewTable();

    private void checkUnique() {
        String[] str = unique();
        if (str == null || str.length == 0) {
            return;
        }

        for (int i = 0; i < str.length; i++) {
            this.core.analysisTableHeader(str[i]);
        }
    }

    @Override
    public abstract String[] unique();

    // hv里的值为 表名.字段名 或者 字段名
    @Override
    public boolean exist() {
        String[] uni = unique();
        if (uni == null || uni.length == 0) {
            return false;
        }
        return Root.getInstance().getBean(CoreSelector.class).existOfViewByOr(do_getExistParam(), this.core);
    }

    // 根据unique()，解析hv中的字段，保留unique中的字段
    private Map<String, Object> do_getExistParam() {
        Map<String, Object> ret = new HashMap();

        String[] uni = unique();
        Set<String> set = new HashSet();
        for (int i = 0; i < uni.length; i++) {
            // 先看看是不是在view中
            String[] r = this.core.analysisViewHeader(uni[i]);
            if (r != null) {
                set.add(r[0] + "." + r[1]);
            }
            // 再解析是不是在各个表中
            r = this.core.analysisTableHeader(uni[i]);
            set.add(r[0] + "." + r[1]);
        }

        Map<String, Map<String, Object>> param = this.core.analysisHvIncludeView(this.hv);
        Iterator<Entry<String, Map<String, Object>>> ite = param.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, Map<String, Object>> ent = ite.next();
            String table = ent.getKey().trim();
            Map<String, Object> v = ent.getValue();
            Iterator<Entry<String, Object>> vite = v.entrySet().iterator();
            while (vite.hasNext()) {
                Entry<String, Object> vent = vite.next();
                String k = vent.getKey().trim();
                if (set.contains(table + "." + k)) {
                    ret.put(table + "." + k, vent.getValue());
                }
            }
        }
        return ret;
    }

    @Override
    public String check() {
        Map<String, Map<String, Object>> map = this.core.analysisHv(this.hv);
// 检查用户名密码是否匹配
        String[] tmp = this.core.analysisTableId(getUserTable());
        Map<String, Object> ub = map.get(tmp[0]);
        Object username = ub.get("username");
        Object passwd = ub.get("passwd");
        if (username == null || passwd == null) {
            throw new IllegalArgumentException("需要用户名和密码来验证");
        }

        CoreSelector sel = Root.getInstance().getBean(CoreSelector.class);
        Map<String, Object> v1 = new HashMap();
        v1.put("username", username);
        List<Map<String, Object>> list = sel.getListByAnd(v1, this.core);
//        // 为避免sql注入，不使用一个sql语句来验证
//        String stmt = "select id,passwd from " + tmp[0] + " where username=?";
//        SqlOperator sql = Root.getInstance().getSqlOperator();
//        List<Map<String, Object>> list = sql.query(stmt, new Object[]{username});
        if (list.isEmpty()) {
            return null;
        }
        Map<String, Object> mp = list.get(0);
        String id1 = mp.get("id") + "";
        String passwd1 = mp.get("passwd") + "";
        // 验证密码
        if (!passwd.equals(passwd1)) {
            return null;
        }
        return id1;
    }

    @Override
    public String add() {
        Map<String, Map<String, Object>> map = this.core.analysisHv(this.hv);
        // 检查必须的字段
        String[] tmp = this.core.analysisTableId(getUserTable());
        Map<String, Object> ub = map.get(tmp[0]);

        Object username = ub.get("username");
        Object passwd = ub.get("passwd");
        if (username == null || passwd == null) {
            throw new IllegalArgumentException("必须有用户名和密码");
        }

        // 检查用户是否存在
        if (exist()) {
            throw new IllegalStateException("用户已存在");
        }

        return this.core.add();
    }

    @Override
    public void remove() {
        this.core.remove();
    }

    @Override
    public void modify(Map<String, Object> hv1) {
        this.core.modify(hv1);
    }

    @Override
    public Object getInfo(String key) {
        return this.core.getInfo(key);
    }

    @Override
    public boolean verify(String passwd1) {
        String passwd = this.core.getInfo("passwd") + "";
        return passwd.equals(passwd1);
    }

    @Override
    public void modifyPasswd(String newPasswd) {
        Map hv1 = new HashMap();
        hv1.put("passwd", newPasswd);
        this.core.modify(hv1);
    }

    // 内部定义CoreView的子类
    private class InnerCoreView extends CoreView {

        public InnerCoreView(Map<String, Object> hv1) {
            super(hv1);
        }

        public InnerCoreView(String id) {
            super(id);
        }

        @Override
        public String getView() {
            return getViewTable();
        }

        @Override
        public String getMainTable() {
            return getUserTable();
        }

        @Override
        public String[] getSlaveTables() {
            return new String[]{getInfoTable()};
        }

    }
}
