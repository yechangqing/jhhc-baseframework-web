package com.jhhc.baseframework.web.core;

import com.jhhc.baseframework.record.SqlOperator;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 另一种更易于使用的Selector
 *
 * @author yecq
 */
public class CoreSelectorChain {

    private SqlOperator sql;

    private String table;
    private List<String> header;
    private List<Where> where;
    private List<String> order;

    public CoreSelectorChain(String table) {
        if (table == null || table.trim().equals("")) {
            throw new IllegalArgumentException("表名为空");
        }
        this.table = table.trim();
        this.header = new LinkedList();
        this.where = new LinkedList();
        this.order = new LinkedList();
        this.sql = Root.getInstance().getSqlOperator();
    }

    public CoreSelectorChain select(String header) {
        if (header == null || header.trim().equals("")) {
            return this;     // 此时为select *
        }
        this.header.add(header.trim());
        return this;
    }

    public CoreSelectorChain select(String[] header) {
        if (header == null) {
            return this;    // 此时为select *
        }
        for (int i = 0; i < header.length; i++) {
            if (header[i] == null || header[i].trim().equals("")) {
                continue;
            }
            this.header.add(header[i].trim());
        }
        return this;
    }

    public CoreSelectorChain where(String condition) {
        if (condition == null || condition.trim().equals("")) {
            throw new IllegalArgumentException("where语句为空");
        }
        // 增加第一个where语句
        if (!this.where.isEmpty()) {
            throw new IllegalArgumentException("指明是and还是or");
        }
        this.where.add(new First(condition.trim()));
        return this;
    }

    public CoreSelectorChain whereAnd(String condition) {
        if (condition == null || condition.trim().equals("")) {
            throw new IllegalArgumentException("where语句为空");
        }
        if (this.where.isEmpty()) {
            this.where.add(new First(condition.trim()));
        } else {
            this.where.add(new And(condition.trim()));
        }
        return this;
    }

    public CoreSelectorChain whereOr(String condition) {
        if (condition == null || condition.trim().equals("")) {
            throw new IllegalArgumentException("where语句为空");
        }
        if (this.where.isEmpty()) {
            this.where.add(new First(condition.trim()));
        } else {
            this.where.add(new Or(condition.trim()));
        }
        return this;
    }

    public CoreSelectorChain orderBy(String key) {
        if (key == null || key.trim().equals("")) {
            throw new IllegalArgumentException("where语句为空");
        }
        this.order.add(key.trim());
        return this;
    }

    public CoreSelectorChain orderBy(String[] keys) {
        if (keys == null || keys.length == 0) {
            return this;
        }
        for (int i = 0; i < keys.length; i++) {
            this.order.add(keys[i]);
        }
        return this;
    }

    public List<Map<String, Object>> execute() {
        return this.sql.query(generateSql());
    }

    public <T> List<T> execute(Class<T> cls) {
        // 此时一定要选取id
        if (!this.header.isEmpty() && !this.header.contains("id")) {
            throw new IllegalStateException("没有选取id");
        }
        List<Map<String, Object>> list = execute();
        return doExecuteWithClass(list, cls);
    }

    private <T> List<T> doExecuteWithClass(List<Map<String, Object>> list, Class<T> cls) {
        int len = list.size();
        List<T> ret = new ArrayList();
        for (int i = 0; i < len; i++) {
            String id = list.get(i).get("id") + "";
            try {
                Constructor<T> cons = cls.getConstructor(String.class);
                Object o = cons.newInstance(id);
                ret.add((T) o);
            } catch (NoSuchMethodException | SecurityException ex) {
                throw new RuntimeException(ex);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
        return ret;
    }

    private String generateSql() {
        String stmt = "select " + generateHeaders() + " from " + this.table;
        if (!where.isEmpty()) {
            stmt += " where " + generateWhere();
        }
        if (!order.isEmpty()) {
            stmt += " order by " + generateOrderBy();
        }
        return stmt;
    }

    private String generateHeaders() {
        if (this.header.isEmpty()) {
            return "*";
        }
        Iterator<String> ite = this.header.iterator();
        String str = "";
        while (ite.hasNext()) {
            str += ite.next() + ",";
        }

        return str.substring(0, str.length() - 1);
    }

    private String generateWhere() {
        if (this.where.isEmpty()) {
            return "";
        }

        String str = "";
        Iterator<Where> ite = this.where.iterator();
        while (ite.hasNext()) {
            str += ite.next().getWhere() + " ";
        }
        return str.substring(0, str.length() - 1);
    }

    private String generateOrderBy() {
        if (this.order.isEmpty()) {
            return "";
        }

        String str = "";
        Iterator<String> ite = this.order.iterator();
        while (ite.hasNext()) {
            str += ite.next() + ",";
        }
        return str.substring(0, str.length() - 1);
    }

    private interface Where {

        String getWhere();
    }

    private class First implements Where {

        private String where;

        public First(String where) {
            this.where = where;
        }

        @Override
        public String getWhere() {
            return this.where;
        }

    }

    private class And implements Where {

        private String where;

        public And(String where) {
            this.where = where;
        }

        @Override
        public String getWhere() {
            return "and " + this.where;
        }
    }

    private class Or implements Where {

        private String where;

        public Or(String where) {
            this.where = where;
        }

        @Override
        public String getWhere() {
            return "or " + this.where;
        }
    }
}
