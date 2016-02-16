package com.jhhc.baseframework.web.user;

import com.jhhc.baseframework.record.SqlOperator;
import com.jhhc.baseframework.web.core.CoreSelector;
import com.jhhc.baseframework.web.core.CoreTable;
import com.jhhc.baseframework.web.core.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有信息都在一张表里的
 *
 * @author yecq
 */
public abstract class AbstractSimpleUser implements User {

    private String id;
    private Map<String, Object> hv;
    private CoreTable core;

    protected AbstractSimpleUser(Map<String, Object> hv) {
        this.core = new InnerCoreTable(hv);
        this.hv = hv;
        this.id = null;
    }

    protected AbstractSimpleUser(String id) {
        this.core = new InnerCoreTable(id);
        this.id = id.trim();
        this.hv = null;
    }

    protected AbstractSimpleUser(String username, boolean flag) {
        Map<String, Object> mp = new HashMap();
        mp.put("username", username);
        List<Map<String, Object>> list = Root.getInstance().getBean(CoreSelector.class).getListByAnd(mp, new InnerCoreTable());

//        Map<String,Object> list=Root.getInstance().getBean(CoreSelector.class).getList(id, core)
//        String stmt = "select id from " + this.getUserTable() + " where username=?";
//        List<Map<String, Object>> list = root.getSqlOperator().query(stmt, new Object[]{username});
        if (list.isEmpty()) {
            throw new IllegalStateException("用户不存在");
        }
        this.id = list.get(0).get("id") + "";
        this.hv = null;
        this.core = new CoreTable(this.id) {
            @Override
            public String getTable() {
                return "user";
            }
        };
    }

    protected String getUserTable() {
        return "user";
    }

    @Override
    public abstract String[] unique();

    @Override
    public boolean exist() {
        Map<String, Object> hv1 = this.core.analysisHv(this.hv);
        String[] uni = unique();
        if (uni == null || uni.length == 0) {
            return false;
        }
        Map<String, Object> mp = new HashMap();
        for (int i = 0; i < uni.length; i++) {
            String[] r = this.core.analysisTableHeader(uni[i]);
            mp.put(r[1], hv1.get(r[1]));
        }
        return !Root.getInstance().getBean(CoreSelector.class).getListByOr(mp, this.core).isEmpty();
    }

    @Override
    public String check() {
        Map<String, Object> hv1 = this.core.analysisHv(this.hv);
        // 检查用户名密码是否匹配
        Object username = hv1.get("username");
        Object passwd = hv1.get("passwd");
        if (username == null || passwd == null) {
            throw new IllegalArgumentException("需要用户名和密码来验证");
        }
        // 为避免sql注入，不使用一个sql语句来验证
        String stmt = "select id,passwd from " + this.getUserTable() + " where username=?";
        SqlOperator sql = Root.getInstance().getSqlOperator();
        List<Map<String, Object>> list = sql.query(stmt, new Object[]{username});
        if (list.isEmpty()) {
            return null;
        }
        Map<String, Object> tmp = list.get(0);
        String id1 = tmp.get("id") + "";
        String passwd1 = tmp.get("passwd") + "";
        // 验证密码
        if (!passwd.equals(passwd1)) {
            return null;
        }
        return id1;
    }

    @Override
    public String add() {
        Map<String, Object> hv1 = this.core.analysisHv(this.hv);
        // 检查必须的字段
        Object username = hv1.get("username");
        Object passwd = hv1.get("passwd");
        if (username == null || passwd == null) {
            throw new IllegalArgumentException("必须有用户名和密码");
        }

        // 检查用户是否存在
        if (exist()) {
            throw new IllegalStateException("用户已存在");
        }

        // 插入信息
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
        String passwd = this.getInfo("passwd") + "";
        return passwd.equals(passwd1);
    }

    @Override
    public void modifyPasswd(String newPasswd) {
        Map hv1 = new HashMap();
        hv1.put("passwd", newPasswd);
        this.modify(hv1);
    }

    private class InnerCoreTable extends CoreTable {

        public InnerCoreTable() {
            super();
        }

        public InnerCoreTable(String id) {
            super(id);
        }

        public InnerCoreTable(Map<String, Object> hv) {
            super(hv);
        }

        @Override
        public String getTable() {
            return getUserTable();
        }
    }
}
