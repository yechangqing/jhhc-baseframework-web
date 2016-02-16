package com.jhhc.baseframework.web.user;

import com.jhhc.baseframework.record.SqlOperator;
import com.jhhc.baseframework.web.core.Core;
import com.jhhc.baseframework.web.core.CoreTable;
import com.jhhc.baseframework.web.core.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 只含有username和passwd字段的检查
 *
 * @author yecq
 */
public class DefaultUser implements User {

    private Map<String, Object> hv;
    private String id;
    private Core core;

    public DefaultUser(String id) {
        this.core = new InnerCoreTable(id);
        this.id = id.trim();
        this.hv = null;
    }

    public DefaultUser(Map<String, Object> hv1) {
        this.core = new InnerCoreTable(hv1);
        this.id = null;
        this.hv = hv1;
    }

    @Override
    public String[] unique() {
        return new String[]{"username"};
    }

    @Override
    public boolean exist() {
        return false;
    }

    @Override
    public String check() {
        // 只检查用户名密码
        Object username = this.hv.get("username");
        Object passwd = this.hv.get("passwd");
        if (username == null || passwd == null) {
            throw new IllegalArgumentException("必须有用户名和密码");
        }
        String stmt = "select id,passwd from user where username=?";
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
        // 检查必须的字段
        Object username = this.hv.get("username");
        Object passwd = this.hv.get("passwd");
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
    public void modify(Map<String, Object> hv) {
        this.core.modify(hv);
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

        public InnerCoreTable(String id) {
            super(id);
        }

        public InnerCoreTable(Map<String, Object> hv) {
            super(hv);
        }

        @Override
        public String getTable() {
            return "user";
        }
    }
}
