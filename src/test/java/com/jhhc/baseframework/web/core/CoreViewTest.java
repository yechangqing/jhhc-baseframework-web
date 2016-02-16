package com.jhhc.baseframework.web.core;

import com.jhhc.baseframework.test.Base;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dbunit.DatabaseUnitException;
import org.junit.Test;
import org.junit.Before;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 *
 * @author yecq
 */
public class CoreViewTest extends Base {

    private Constructor stati;
    private Constructor obj;
    private Method m_checkTableName;
    private CoreView corev;

    @Before
    @Override
    public void before() throws DatabaseUnitException, SQLException {
        super.before();
        otherInit();
    }

    private <T> T do_method(String name, Object param, Class paramClass, Class<T> cls) {
        try {
            Method method = CoreView.class.getDeclaredMethod(name, paramClass);
            method.setAccessible(true);
            return (T) method.invoke(this.corev, param);
        } catch (InvocationTargetException ex) {
            if (ex.getCause().getClass().equals(IllegalArgumentException.class)) {
                throw new IllegalArgumentException(ex.getCause().getMessage());
            } else if (ex.getCause().getClass().equals(IllegalStateException.class)) {
                throw new IllegalStateException(ex.getCause().getMessage());
            } else {
                throw new RuntimeException(ex.getCause().getMessage());
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException(ex.getCause().getMessage());
        }
    }

    public void otherInit() {
        try {
            obj = SubCoreView1.class.getDeclaredConstructor(String.class);
            obj.setAccessible(true);
            try {
                corev = (SubCoreView1) obj.newInstance("3");
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex.getCause().toString());
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex.getCause().toString());
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex.getCause().toString());
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex.getCause().toString());
            }
            m_checkTableName = CoreView.class.getDeclaredMethod("analysisTableId", String.class);
            m_checkTableName.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex.getCause().toString());
        } catch (SecurityException ex) {
            throw new RuntimeException(ex.getCause().toString());
        }
    }

    private <T> T getField(String name, Class<T> cls) {
        try {
            obj = SubCoreView1.class.getDeclaredConstructor(String.class);
            obj.setAccessible(true);
            try {
                corev = (SubCoreView1) obj.newInstance("3");
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex.getCause().toString());
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex.getCause().toString());
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex.getCause().toString());
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex.getCause().toString());
            }
            Field fld = CoreView.class.getDeclaredField(name);
            fld.setAccessible(true);
            return (T) fld.get(corev);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex.getCause().toString());
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex.getCause().toString());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex.getCause().toString());
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex.getCause().toString());
        }
    }

    @Test
    public void test1_checkTableName() throws IllegalAccessException, IllegalArgumentException {
        String str = "adr.cgsa.";
        this.expectedEx.expectMessage("参数格式为 表名.字段名 或 字段名");
        do_method("analysisTableId", str, String.class, String[].class);
    }

    @Test
    public void test2_checkTableName() throws IllegalAccessException, IllegalArgumentException {
        String str = "core1.email";
        String[] ret = do_method("analysisTableId", str, String.class, String[].class);
        assertThat(ret.length, is(2));
        assertThat(ret[0], is("core1"));
        assertThat(ret[1], is("email"));
    }

    @Test
    public void test4_checkTableName() throws IllegalAccessException {
        String str = "table.id1.exs";
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("参数格式为 表名.字段名 或 字段名");
        do_method("analysisTableId", str, String.class, String[].class);
    }

    @Test
    public void test1_initHeader() {
        Map hv1 = new HashMap();
        hv1.put("username", "1");
        SubCoreView1 s = new SubCoreView1(hv1);
        Set<String> h = getField("header", Set.class);
        assertThat(h.size(), is(8));
    }

    @Test
    public void test_initIdTableAndTbHeaders() {
        Map<String, String> map = getField("id_table", Map.class);
        assertThat(map.size(), is(2));
        assertThat(map.get("id"), is("core1"));
        assertThat(map.get("info1_id"), is("info1"));

        Map<String, Set<String>> map1 = getField("tb_headers", Map.class);
        assertThat(map1.size(), is(2));
        Set<String> h1 = map1.get("core1");
        assertThat(h1.size(), is(5));
        Set<String> h2 = map1.get("info1");
        assertThat(h2.size(), is(4));
    }

    @Test
    public void test_initCores() {
        Map<String, CoreTable> map = getField("cores", Map.class);
        assertThat(map.size(), is(2));
        CoreTable id = map.get("core1");
        assertThat(id.getId(), is("3"));
        assertThat(id.getTable(), is("core1"));
        CoreTable info1_id = map.get("info1");
        assertThat(info1_id.getId(), is("8"));
        assertThat(info1_id.getTable(), is("info1"));
    }

    @Test
    public void test1_CoreViewNew1() {
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("记录不存在");
        new SubCoreView1("13");
    }

    @Test
    public void test2_CoreViewNew1() {
        new SubCoreView1(new HashMap());
    }

    @Test
    public void test_getInfo() {
        CoreView core = new SubCoreView1("3");
        Map<String, Object> map = core.getInfo();
        assertThat(map.size(), is(8));
        assertThat(map.get("username") + "", is("abcd"));
        assertThat(map.get("passwd") + "", is("123456"));
        assertThat(map.get("name") + "", is("ABC"));
        assertThat(map.get("email") + "", is("abc@123.com"));
        assertThat(map.get("id") + "", is("3"));
        assertThat(map.get("department") + "", is("信工系"));
        assertThat(map.get("room") + "", is("14栋"));
        assertThat(map.get("info1_id") + "", is("8"));
    }

    @Test
    public void test1_getInfo() {
        CoreView core = new SubCoreView1("3");
        assertThat(core.getInfo("username") + "", is("abcd"));
        assertThat(core.getInfo("passwd") + "", is("123456"));
        assertThat(core.getInfo("name") + "", is("ABC"));
        assertThat(core.getInfo("email") + "", is("abc@123.com"));
        assertThat(core.getInfo("id") + "", is("3"));
        assertThat(core.getInfo("department") + "", is("信工系"));
        assertThat(core.getInfo("room") + "", is("14栋"));
        assertThat(core.getInfo("info1_id") + "", is("8"));
    }

    @Test
    public void test_remove() {
        String stmt = "select * from v_core1 where id=3";
        List list = Root.getInstance().getSqlOperator().query(stmt);
        assertThat(list.size(), is(1));
        new SubCoreView1("3").remove();
        list = Root.getInstance().getSqlOperator().query(stmt);
        assertThat(list.size(), is(0));
    }

    @Test
    public void test_getTableByHeaderName() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String str = "username";
        assertThat(do_method("getTableByHeaderName", str, String.class, String.class), is("core1"));

        str = "department";
        assertThat(do_method("getTableByHeaderName", str, String.class, String.class), is("info1"));
    }

    @Test
    public void test1_getTableByHeaderName() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("不存在字段username1");
        String str = "username1";
        do_method("getTableByHeaderName", str, String.class, String.class);
    }

    @Test
    public void test2_getTableByHeaderName() throws NoSuchMethodException, IllegalAccessException {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("有歧义的字段id");
        String str = "id";
        do_method("getTableByHeaderName", str, String.class, String.class);
    }

    @Test
    public void test_analysisTableHeader() {
        SubCoreView1 core = new SubCoreView1("3");
        String str = "username";
        String[] ret = core.analysisTableHeader(str);
        assertThat(ret[0], is("core1"));
        assertThat(ret[1], is("username"));

        str = "info1.department";
        ret = core.analysisTableHeader(str);
        assertThat(ret[0], is("info1"));
        assertThat(ret[1], is("department"));
    }

    @Test
    public void test1_analysisTableHeader() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("参数格式为 表名.字段名 或 字段名");
        SubCoreView1 core = new SubCoreView1("3");
        core.analysisTableHeader(".and.wait");
    }

    @Test
    public void test2_analysisTableHeader() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("参数格式为 表名.字段名 或 字段名");
        SubCoreView1 core = new SubCoreView1("3");
        core.analysisTableHeader("and.wait.");
    }

    @Test
    public void test3_analysisTableHeader() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("参数格式为 表名.字段名 或 字段名");
        SubCoreView1 core = new SubCoreView1("3");
        core.analysisTableHeader("and.wait.abc");
    }

    @Test
    public void test4_analysisTableHeader() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表core1不含有字段abc");
        SubCoreView1 core = new SubCoreView1("3");
        core.analysisTableHeader("core1.abc");
    }

    @Test
    public void test5_analysisTableHeader() {
        CoreView core = new SubCoreView1("3");
        String[] r = core.analysisTableHeader("info1.department");
        assertThat(r[0], is("info1"));
        assertThat(r[1], is("department"));

        r = core.analysisTableHeader("username");
        assertThat(r[0], is("core1"));
        assertThat(r[1], is("username"));
    }

    @Test
    public void test6_analysisTableHeader() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("info1.department", "计算机系");
        hv1.put("room", "29栋404");
        CoreView core = new SubCoreView1(hv1);

        String[] r = core.analysisTableHeader("info1.department");
        assertThat(r[0], is("info1"));
        assertThat(r[1], is("department"));

        r = core.analysisTableHeader("username");
        assertThat(r[0], is("core1"));
        assertThat(r[1], is("username"));
    }

    @Test
    public void test_analysisHv() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.username", 1);
        hv1.put("passwd", "234513");
        hv1.put("name", "ABC1");
        CoreView core = new SubCoreView1("3");

        Map<String, Map<String, Object>> map = core.analysisHv(hv1);
        Map<String, Object> m = map.get("core1");
        assertThat(m, not(nullValue()));
        assertThat(m.size(), is(3));
        assertThat(Integer.parseInt(m.get("username") + ""), is(1));
        assertThat(m.get("passwd") + "", is("234513"));
        assertThat(m.get("name") + "", is("ABC1"));

        m = map.get("info1");
        assertThat(m.isEmpty(), is(true));
    }

    @Test
    public void test1_analysisHv() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("info1.department", "信工院");
        hv1.put("room", "20栋101");
        CoreView core = new SubCoreView1("3");

        Map<String, Map<String, Object>> map = core.analysisHv(hv1);
        Map<String, Object> m = map.get("info1");
        assertThat(m, not(nullValue()));
        assertThat(m.size(), is(2));
        assertThat(m.get("department") + "", is("信工院"));
        assertThat(m.get("room") + "", is("20栋101"));

        m = map.get("core1");
        assertThat(m.isEmpty(), is(true));
    }

    @Test
    public void test2_analysisHv() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.username", 1);
        hv1.put("passwd", "234513");
        hv1.put("name", "ABC1");
        hv1.put("info1.department", "信工院");
        hv1.put("room", "20栋101");
        CoreView core = new SubCoreView1("3");

        Map<String, Map<String, Object>> map = core.analysisHv(hv1);
        Map<String, Object> m = map.get("core1");
        assertThat(m, not(nullValue()));
        assertThat(m.size(), is(3));
        assertThat(Integer.parseInt(m.get("username") + ""), is(1));
        assertThat(m.get("passwd") + "", is("234513"));
        assertThat(m.get("name") + "", is("ABC1"));

        m = map.get("info1");
        assertThat(m, not(nullValue()));
        assertThat(m.size(), is(2));
        assertThat(m.get("department") + "", is("信工院"));
        assertThat(m.get("room") + "", is("20栋101"));
    }

    @Test
    public void test3_analysisHv() {
        SubCoreView1 core = new SubCoreView1("3");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.username", 1);
        hv1.put("room", "20栋101");
        hv1.put("id", "3");

        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("有歧义的字段id");
        core.analysisHv(hv1);
    }

    @Test
    public void test4_analysisHv() {
        SubCoreView1 core = new SubCoreView1("3");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.username", 1);
        hv1.put("room", "20栋101");
        hv1.put("id1", "3");

        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("不存在字段id1");
        core.analysisHv(hv1);
    }

    @Test
    public void test5_analysisHv() {
        SubCoreView1 core = new SubCoreView1("3");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.username", 1);
        hv1.put("core1.room", "20栋101");

        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表core1不含有字段room");
        core.analysisHv(hv1);
    }

    @Test
    public void test6_analysisHv() {
        SubCoreView1 core = new SubCoreView1("3");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.username", 1);
        hv1.put("core222.room", "20栋101");

        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("不含有表core222");
        core.analysisHv(hv1);
    }

    @Test
    public void test7_analysisHv() {
        SubCoreView1 core = new SubCoreView1("3");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.username", 1);
        hv1.put("info1.room1", "20栋101");

        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表info1不含有字段room1");
        core.analysisHv(hv1);
    }

    @Test
    public void test_modify() {
        String stmt = "select * from v_core1 where id=3";
        List<Map<String, Object>> list = Root.getInstance().getSqlOperator().query(stmt);
        Map<String, Object> obj = list.get(0);
        assertThat(obj.get("username") + "", is("abcd"));
        assertThat(obj.get("email") + "", is("abc@123.com"));

        Map hv = new HashMap();
        hv.put("username", "wocao");
        hv.put("core1.email", "windforce@yeah.net");

        CoreView core = new SubCoreView1("3");
        core.modify(hv);

        list = Root.getInstance().getSqlOperator().query(stmt);
        obj = list.get(0);
        assertThat(obj.get("username") + "", is("wocao"));
        assertThat(obj.get("email") + "", is("windforce@yeah.net"));
    }

    @Test
    public void test1_modify() {
        String stmt = "select * from v_core1 where id=3";
        List<Map<String, Object>> list = Root.getInstance().getSqlOperator().query(stmt);
        Map<String, Object> obj = list.get(0);
        assertThat(obj.get("department") + "", is("信工系"));
        assertThat(obj.get("room") + "", is("14栋"));

        Map hv = new HashMap();
        hv.put("info1.department", "外语");
        hv.put("room", "29栋");

        CoreView core = new SubCoreView1("3");
        core.modify(hv);

        list = Root.getInstance().getSqlOperator().query(stmt);
        obj = list.get(0);
        assertThat(obj.get("department") + "", is("外语"));
        assertThat(obj.get("room") + "", is("29栋"));
    }

    @Test
    public void test2_modify() {
        String stmt = "select * from v_core1 where id=3";
        List<Map<String, Object>> list = Root.getInstance().getSqlOperator().query(stmt);
        Map<String, Object> obj = list.get(0);
        assertThat(obj.get("username") + "", is("abcd"));
        assertThat(obj.get("email") + "", is("abc@123.com"));
        assertThat(obj.get("department") + "", is("信工系"));
        assertThat(obj.get("room") + "", is("14栋"));

        Map hv = new HashMap();
        hv.put("core1.username", "wocao");
        hv.put("core1.email", "windforce@yeah.net");
        hv.put("department", "外语");
        hv.put("room", "29栋");

        CoreView core = new SubCoreView1("3");
        core.modify(hv);

        list = Root.getInstance().getSqlOperator().query(stmt);
        obj = list.get(0);
        assertThat(obj.get("username") + "", is("wocao"));
        assertThat(obj.get("email") + "", is("windforce@yeah.net"));
        assertThat(obj.get("department") + "", is("外语"));
        assertThat(obj.get("room") + "", is("29栋"));
    }

    @Test
    public void test_add() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("username", "皮卡丘");
        hv1.put("passwd", "qubapikaqiu");
        hv1.put("core1.name", "jqhyxl");
        hv1.put("core1.email", "r12b@163.com");
        hv1.put("info1.department", "计算机系");
        hv1.put("room", "29栋404");

        String id = new SubCoreView1(hv1).add();
        assertThat(id, not(nullValue()));
    }

    @Test
    public void test1_add() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("info1.department", "计算机系");
        hv1.put("room", "29栋404");

        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("必须有主表内容");
        String id = new SubCoreView1(hv1).add();
    }

    @Test
    public void test_CoreView_cores() {
        CoreView core = new SubCoreView1("3");
        Map map = core.cores;
        assertThat(map.size(), is(2));
        Object o1 = map.get("core1");
        System.out.println("o1.class is "+o1.getClass());
//        assertThat(o1.getClass().equals(CoreTable.class), is(true));
        assertThat(((CoreTable) o1).getId(), is("3"));

        Object o2 = map.get("info1");
        System.out.println("o2.class is "+o2.getClass());
//        assertThat(o2.getClass().equals(CoreTable.class), is(true));
        assertThat(((CoreTable) o2).getId(), is("8"));
    }

//    @Test
//    public void test2_checkHeader() {
//        Map<String, Object> hv1 = new HashMap();
//        hv1.put("info1.department", "计算机系");
//        hv1.put("room", "29栋404");
//        CoreView core = new SubCoreView1(hv1);
//
//        this.expectedEx.expect(IllegalArgumentException.class);
//        this.expectedEx.expectMessage("不存在字段abcd");
//        core.checkHeader("abcd");
//    }
////    @Test
//    public void test3_checkHeader() {
//        Map<String, Object> hv1 = new HashMap();
//        hv1.put("department", "计算机系");
//        hv1.put("room", "29栋404");
//        CoreView core = new SubCoreView1(hv1);
//
//        this.expectedEx.expect(IllegalArgumentException.class);
//        this.expectedEx.expectMessage("不存在字段abcd");
////        core.checkHeader("abcd");
//    }
}
