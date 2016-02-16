package com.jhhc.baseframework.web.core;

import com.jhhc.baseframework.record.Relation;
import com.jhhc.baseframework.record.SqlOperator;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 返回一系列对象
 *
 * @author yecq
 */
@Component
public class CoreSelector {

    @Autowired
    private Relation rel;

    @Autowired
    private SqlOperator sql;

    public List<Map<String, Object>> getList(String condition, String[] order, String table) {
        this.rel.init1(table, condition, order);
        return rel.getRows();
    }

    public List<Map<String, Object>> getList(String condition, Object[] args, String[] order, String table) {
        String stmt = "select * from " + table + " where " + condition;
        String ob = "";
        if (order != null) {
            for (int i = 0; i < order.length; i++) {
                ob += order[i] + ",";
            }
        }
        if (!ob.equals("")) {
            ob = ob.substring(0, ob.length() - 1);
            stmt = stmt + " order by " + ob;
        }
        return sql.query(stmt, args);
    }

    public List<Map<String, Object>> getList(String condition, String[] order, Core core) {
        // 首先获得表名字
        String table = core.getMetaData().getTableName();
        this.rel.init1(table, condition, order);
        return rel.getRows();
    }

    public <T> List<T> getList(String condition, String[] order, Class<T> cls) {
        if (!Core.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("必须是Core类型");
        }
        // 首先获得表名字
        String table = getTable(null, cls);
        this.rel.init1(table, condition, order);
        List<Map<String, Object>> list = rel.getRows();
        return doGetList(list, cls);
    }

    public List<Map<String, Object>> getList(String condition, Object[] args, String[] order, Core core) {
        String table = core.getMetaData().getTableName();
        String stmt = "select * from " + table + " where " + condition;
        String ob = "";
        if (order != null) {
            for (int i = 0; i < order.length; i++) {
                ob += order[i] + ",";
            }
        }
        if (!ob.equals("")) {
            ob = ob.substring(0, ob.length() - 1);
            stmt = stmt + " order by " + ob;
        }
        return sql.query(stmt, args);
    }

    public <T> List<T> getList(String condition, Object[] args, String[] order, Class<T> cls) {
        if (!Core.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("必须是Core类型");
        }
        // 首先获得表名字
        String table = getTable(null, cls);
        String stmt = "select * from " + table + " where " + condition;
        String ob = "";
        if (order != null) {
            for (int i = 0; i < order.length; i++) {
                ob += order[i] + ",";
            }
        }
        if (!ob.equals("")) {
            ob = ob.substring(0, ob.length() - 1);
            stmt = stmt + " order by " + ob;
        }
        List<Map<String, Object>> list = sql.query(stmt, args);
        return doGetList(list, cls);
    }

    private <T> List<T> doGetList(List<Map<String, Object>> list, Class<T> cls) {
        int len = list.size();
        List<T> ret = new ArrayList();
        for (int i = 0; i < len; i++) {
            Map<String, Object> map = list.get(i);
            if (!map.containsKey("id")) {
                throw new IllegalStateException("没有选取id");
            }
            String id = map.get("id") + "";
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

    public List<Map<String, Object>> getList(String condition, Core core) {
        return getList(condition, null, core);
    }

    public List<Map<String, Object>> getList(String condition, String table) {
        return getList(condition, null, table);
    }

    public <T> List<T> getList(String condition, Class cls) {
        return getList(condition, null, cls);
    }

    public List<Map<String, Object>> getList(String condition, Object[] args, Core core) {
        return getList(condition, args, null, core);
    }

    public List<Map<String, Object>> getList(String condition, Object[] args, String table) {
        return getList(condition, args, null, table);
    }

    public <T> List<T> getList(String condition, Object[] args, Class cls) {
        return getList(condition, args, null, cls);
    }

    public List<Map<String, Object>> getListByOr(Map<String, Object> or, String[] order, String table) {
        if (or == null || or.isEmpty()) {
            return getList(null, order, table);
        }
        String stmt = "";
        int len = or.size();
        Object[] args = new Object[len];
        int k = 0;
        Iterator<Entry<String, Object>> ite = or.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, Object> en = ite.next();
            stmt += en.getKey() + "=? or ";
            args[k++] = en.getValue();
        }
        stmt = stmt.substring(0, stmt.length() - 4);
        stmt = "select * from " + table + " where " + stmt;
        return sql.query(stmt, args);
    }

    public List<Map<String, Object>> getListByOr(Map<String, Object> or, String[] order, Core core) {
        if (or == null || or.isEmpty()) {
            return getList(null, order, core);
        }
        String stmt = "";
        int len = or.size();
        Object[] args = new Object[len];
        int k = 0;
        Iterator<Entry<String, Object>> ite = or.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, Object> en = ite.next();
            stmt += en.getKey() + "=? or ";
            args[k++] = en.getValue();
        }
        stmt = stmt.substring(0, stmt.length() - 4);
        stmt = "select * from " + core.getMetaData().getTableName() + " where " + stmt;
        return sql.query(stmt, args);
    }

    // or里面的条件是或
    public <T> List<T> getListByOr(Map<String, Object> or, String[] order, Class<T> cls) {
        if (or == null || or.isEmpty()) {
            return getList(null, order, cls);
        }
        String stmt = "";
        int len = or.size();
        Object[] args = new Object[len];
        int k = 0;
        Iterator<Entry<String, Object>> ite = or.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, Object> en = ite.next();
            stmt += en.getKey() + "=? or ";
            args[k++] = en.getValue();
        }
        stmt = stmt.substring(0, stmt.length() - 4);
        stmt = "select * from " + getTable(null, cls) + " where " + stmt;
        List<Map<String, Object>> ret = sql.query(stmt, args);
        return doGetList(ret, cls);
    }

    public List<Map<String, Object>> getListByOr(Map<String, Object> or, String table) {
        return getListByOr(or, null, table);
    }

    public List<Map<String, Object>> getListByOr(Map<String, Object> or, Core core) {
        return getListByOr(or, null, core);
    }

    public <T> List<T> getListByOr(Map<String, Object> or, Class<T> cls) {
        return getListByOr(or, null, cls);
    }

    public List<Map<String, Object>> getListByAnd(Map<String, Object> and, String[] order, String table) {
        if (and == null || and.isEmpty()) {
            return getList(null, order, table);
        }
        String stmt = "";
        int len = and.size();
        Object[] args = new Object[len];
        int k = 0;
        Iterator<Entry<String, Object>> ite = and.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, Object> en = ite.next();
            stmt += en.getKey() + "=? and ";
            args[k++] = en.getValue();
        }
        stmt = stmt.substring(0, stmt.length() - 5);
        stmt = "select * from " + table + " where " + stmt;
        return sql.query(stmt, args);
    }

    public List<Map<String, Object>> getListByAnd(Map<String, Object> and, String[] order, Core core) {
        if (and == null || and.isEmpty()) {
            return getList(null, order, core);
        }
        String stmt = "";
        int len = and.size();
        Object[] args = new Object[len];
        int k = 0;
        Iterator<Entry<String, Object>> ite = and.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, Object> en = ite.next();
            stmt += en.getKey() + "=? and ";
            args[k++] = en.getValue();
        }
        stmt = stmt.substring(0, stmt.length() - 5);
        stmt = "select * from " + core.getMetaData().getTableName() + " where " + stmt;
        return sql.query(stmt, args);
    }

    // and里面的条件是并
    public <T> List<T> getListByAnd(Map<String, Object> and, String[] order, Class<T> cls) {
        if (and == null || and.isEmpty()) {
            return getList(null, order, cls);
        }
        String stmt = "";
        int len = and.size();
        Object[] args = new Object[len];
        int k = 0;
        Iterator<Entry<String, Object>> ite = and.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, Object> en = ite.next();
            stmt += en.getKey() + "=? and ";
            args[k++] = en.getValue();
        }
        stmt = stmt.substring(0, stmt.length() - 5);
        stmt = "select * from " + getTable(null, cls) + " where " + stmt;
        List<Map<String, Object>> ret = sql.query(stmt, args);
        return doGetList(ret, cls);
    }

    public List<Map<String, Object>> getListByAnd(Map<String, Object> and, String table) {
        return getListByAnd(and, null, table);
    }

    public List<Map<String, Object>> getListByAnd(Map<String, Object> and, Core core) {
        return getListByAnd(and, null, core);
    }

    public <T> List<T> getListByAnd(Map<String, Object> and, Class<T> cls) {
        return getListByAnd(and, null, cls);
    }

    String getTable(Core core, Class cls) {
        try {
            if (core == null) {
                Constructor cons = cls.getDeclaredConstructor(Map.class);
                Object o = cons.newInstance(new HashMap());
                core = (Core) o;
            }
            return core.getMetaData().getTableName();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    String getTable(Core core) {
        return core.getMetaData().getTableName();
    }

    // 求视图中是否存在某些字段的记录，包括视图和它的各个表
    // 适用于独立定义的CoreView对象
    public boolean existOfViewByOr(Map<String, Object> or, Class viewCls) {
        try {
            if (!CoreView.class.isAssignableFrom(viewCls)) {
                throw new IllegalArgumentException("必须是CoreView类型");
            }

            // 先看数据库里有没有view的元素，没有的话，就不用找了，有的话之后还要用到
            List<Map<String, Object>> l = this.sql.query("select * from " + getTable(null, viewCls));
            if (l.isEmpty()) {
                return false;
            }

            Constructor cons = viewCls.getDeclaredConstructor(String.class);
            CoreView core = (CoreView) cons.newInstance(l.get(0).get("id") + "");

            // 取得view的各个字表的Class 
            Iterator<Entry<String, Object>> ite = or.entrySet().iterator();
            while (ite.hasNext()) {
                Entry<String, Object> ent = ite.next();
                String k = ent.getKey();
                String[] r = core.analysisViewHeader(k);
                String tableName = null;
                if (r != null) {
                    // 视图的字段
                    tableName = getTable(core, viewCls);
                } else {
                    // 表的字段
                    r = core.analysisTableHeader(k);
                    tableName = getTable(core.cores.get(r[0]));
                }
                List list = this.sql.query("select * from " + tableName + " where " + r[1] + " = ?", new Object[]{ent.getValue()});
                if (!list.isEmpty()) {
                    return true;
                }
            }
            return false;
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException("反射创建CoreView对象时出错");
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException("反射创建CoreView对象时出错");
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(ex.getCause().getMessage());
        }
    }

    // 适用于独立定义和在内部定义的CoreView对象
    public boolean existOfViewByOr(Map<String, Object> or, CoreView core) {
        // 先看数据库里有没有view的元素，没有的话，就不用找了，有的话之后还要用到
        List<Map<String, Object>> l = this.sql.query("select * from " + core.getView());
        if (l.isEmpty()) {
            return false;
        }

        // 再看看各个子表里有没有
        Iterator<Entry<String, Object>> ite = or.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, Object> ent = ite.next();
            String k = ent.getKey();
            String[] r = core.analysisViewHeader(k);
            String tableName = null;
            if (r != null) {
                // 视图的字段
                tableName = core.getView();
            } else {
                // 表的字段
                r = core.analysisTableHeader(k);
                tableName = r[0];
            }
            List list = this.sql.query("select * from " + tableName + " where " + r[1] + " = ?", new Object[]{ent.getValue()});
            if (!list.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
