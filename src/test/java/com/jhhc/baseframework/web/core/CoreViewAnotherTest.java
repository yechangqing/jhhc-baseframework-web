package com.jhhc.baseframework.web.core;

import com.jhhc.baseframework.test.Base;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.dbunit.DatabaseUnitException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author yecq
 */
public class CoreViewAnotherTest extends Base {

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
    public void test_splitViewHeader() {
        CoreView core = new SubCoreView1("3");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("参数格式为 表名.字段名 或 字段名");
        do_method("analysisViewHeader", ".abc.ref", String.class, String[].class);
    }

    @Test
    public void test1_splitViewHeader() {
        CoreView core = new SubCoreView1("3");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("参数格式为 表名.字段名 或 字段名");
        do_method("analysisViewHeader", "abc.ref.", String.class, String[].class);
    }

    @Test
    public void test2_splitViewHeader() {
        CoreView core = new SubCoreView1("3");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("参数格式为 表名.字段名 或 字段名");
        do_method("analysisViewHeader", "abc.ref.asf", String.class, String[].class);
    }

    @Test
    public void test3_splitViewHeader() {
        CoreView core = new SubCoreView1("3");
        String[] str = do_method("analysisViewHeader", "v_core1.username", String.class, String[].class);
        assertThat(str[0], is("v_core1"));
        assertThat(str[1], is("username"));
    }

    @Test
    public void test4_splitViewHeader() {
        Map map = new HashMap();
        map.put("username", "2");
        CoreView core = new SubCoreView1(map);
        String[] str = do_method("analysisViewHeader", "v_core1.username", String.class, String[].class);
        assertThat(str[0], is("v_core1"));
        assertThat(str[1], is("username"));
    }

    @Test
    public void test5_splitViewHeader() {
        CoreView core = new SubCoreView1("3");
        String[] str = do_method("analysisViewHeader", "v_core.username", String.class, String[].class);
        assertThat(str, is(nullValue()));
    }

    @Test
    public void test6_splitViewHeader() {
        CoreView core = new SubCoreView1("3");
        String[] str = do_method("analysisViewHeader", "v_core1.abcd", String.class, String[].class);
        assertThat(str, is(nullValue()));
    }

    @Test
    public void test_changeId() {
        CoreView core = new SubCoreView1("3");
        core.changeId("1");
        assertThat(core.getId(), is("1"));
        assertThat(core.getInfo("id") + "", is("1"));
        assertThat(core.getInfo("department") + "", is("电子"));
    }

    @Test
    public void test1_changeId() {
        CoreView core = new SubCoreView1("3");
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("记录不存在");
        core.changeId("10");
    }

    @Test
    public void test_changeHv() {
        Map map = new HashMap();
        map.put("username", "2");
        map.put("info1.department", "29栋");
        CoreView core = new SubCoreView1(map);
        map.clear();
        map.put("username", "10");
        map.put("info1.department", "雅居乐");
        core.changeHv(map);
        assertThat(core.getInfoOfHv("username") + "", is("10"));
        assertThat(core.getInfoOfHv("info1.department") + "", is("雅居乐"));
    }

    @Test
    public void test1_changeHv() {
        Map map = new HashMap();
        map.put("username", "2");
        map.put("info1.department", "29栋");
        CoreView core = new SubCoreView1(map);
        map.clear();
        map.put("info1.department1", "雅居乐");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表info1不含有字段department1");
        core.changeHv(map);
    }

    @Test
    public void test_getInfo() {
        CoreView core = new SubCoreView1("3");
        Object o = core.getInfo("info1_id");
        assertThat(o + "", is("8"));
    }

    @Test
    public void test1_getInfo() {
        CoreView core = new SubCoreView1("3");
        Object o = core.getInfo("v_core1.info1_id");
        assertThat(o + "", is("8"));
    }

    @Test
    public void test2_getInfo() {
        CoreView core = new SubCoreView1("3");
        Object o = core.getInfo("core1.username");
        assertThat(o + "", is("abcd"));
    }

    @Test
    public void test3_getInfo() {
        CoreView core = new SubCoreView1("3");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("视图不含有表core22");
        Object o = core.getInfo("core22.username");
    }

    @Test
    public void test4_getInfo() {
        CoreView core = new SubCoreView1("3");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表core1不含有字段zxcv");
        Object o = core.getInfo("core1.zxcv");
    }

    @Test
    public void test_splitAndCheckTableId() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("视图不存在字段info1_idd");
        CoreView core = new SubCoreView0("3");
    }

    @Test
    public void test1_splitAndCheckTableId() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("视图不存在字段idd1");
        CoreView core = new SubCoreView01("3");
    }

    @Test
    public void test_analysisHvIncludeView() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.username", 1);
        hv1.put("passwd", "234513");
        hv1.put("name", "ABC1");
        CoreView core = new SubCoreView1("3");

        Map<String, Map<String, Object>> map = core.analysisHvIncludeView(hv1);
        assertThat(map.size(), is(3));
        Map<String, Object> m = map.get("core1");
        assertThat(m, not(nullValue()));
        assertThat(m.size(), is(3));
        assertThat(Integer.parseInt(m.get("username") + ""), is(1));
        assertThat(m.get("passwd") + "", is("234513"));
        assertThat(m.get("name") + "", is("ABC1"));

        m = map.get("info1");
        assertThat(m.isEmpty(), is(true));

        m = map.get("v_core1");
        assertThat(m.size(), is(2));
        assertThat(m.get("passwd") + "", is("234513"));
        assertThat(m.get("name") + "", is("ABC1"));
    }

    @Test
    public void test1_analysisHvIncludeView() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.username", 1);
        hv1.put("passwd", "234513");
        hv1.put("department", "信工计算机");
        CoreView core = new SubCoreView1("3");

        Map<String, Map<String, Object>> map = core.analysisHvIncludeView(hv1);
        assertThat(map.size(), is(3));
        Map<String, Object> m = map.get("core1");
        assertThat(m, not(nullValue()));
        assertThat(m.size(), is(2));
        assertThat(Integer.parseInt(m.get("username") + ""), is(1));
        assertThat(m.get("passwd") + "", is("234513"));

        m = map.get("info1");
        assertThat(m.get("department") + "", is("信工计算机"));

        m = map.get("v_core1");
        assertThat(m.size(), is(2));
        assertThat(m.get("passwd") + "", is("234513"));
        assertThat(m.get("department") + "", is("信工计算机"));
    }

    @Test
    public void test2_analysisHvIncludeView() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.username", 1);
        hv1.put("passwd", "234513");
        hv1.put("info1.department", "信工计算机");
        CoreView core = new SubCoreView1("3");

        Map<String, Map<String, Object>> map = core.analysisHvIncludeView(hv1);
        assertThat(map.size(), is(3));
        Map<String, Object> m = map.get("core1");
        assertThat(m, not(nullValue()));
        assertThat(m.size(), is(2));
        assertThat(Integer.parseInt(m.get("username") + ""), is(1));
        assertThat(m.get("passwd") + "", is("234513"));

        m = map.get("info1");
        assertThat(m.get("department") + "", is("信工计算机"));

        m = map.get("v_core1");
        assertThat(m.size(), is(1));
        assertThat(m.get("passwd") + "", is("234513"));
    }

    @Test
    public void test3_analysisHvIncludeView() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.username", 1);
        hv1.put("v_core1.passwd", "234513");
        hv1.put("info1.department", "信工计算机");
        CoreView core = new SubCoreView1("3");

        Map<String, Map<String, Object>> map = core.analysisHvIncludeView(hv1);
        assertThat(map.size(), is(3));
        Map<String, Object> m = map.get("core1");
        assertThat(m, not(nullValue()));
        assertThat(m.size(), is(1));
        assertThat(Integer.parseInt(m.get("username") + ""), is(1));

        m = map.get("info1");
        assertThat(m.get("department") + "", is("信工计算机"));

        m = map.get("v_core1");
        assertThat(m.size(), is(1));
        assertThat(m.get("passwd") + "", is("234513"));
    }

    private class SubCoreView0 extends CoreView {

        public SubCoreView0(Map<String, Object> hv1) {
            super(hv1);
        }

        public SubCoreView0(String id) {
            super(id);
        }

        @Override
        public String getView() {
            return "v_core1";
        }

        @Override
        public String getMainTable() {
            return "core1";
        }

        @Override
        public String[] getSlaveTables() {
            return new String[]{"info1.info1_idd"};
        }

    }

    private class SubCoreView01 extends CoreView {

        public SubCoreView01(Map<String, Object> hv1) {
            super(hv1);
        }

        public SubCoreView01(String id) {
            super(id);
        }

        @Override
        public String getView() {
            return "v_core1";
        }

        @Override
        public String getMainTable() {
            return "core1.idd1";
        }

        @Override
        public String[] getSlaveTables() {
            return new String[]{"info1.info1_idd"};
        }

    }

    @Test
    public void test_getInfoOfHv() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.id", "1");
        hv1.put("passwd", "234513");
        hv1.put("department", "信工计算机");
        CoreView core = new SubCoreView1(hv1);
        assertThat(core.getInfoOfHv("core1.id") + "", is("1"));
        assertThat(core.getInfoOfHv("passwd") + "", is("234513"));
        assertThat(core.getInfoOfHv("department") + "", is("信工计算机"));
    }

    @Test
    public void test1_getInfoOfHv() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.id", "1");
        hv1.put("passwd", "234513");
        hv1.put("department", "信工计算机");
        CoreView core = new SubCoreView1(hv1);
        assertThat(core.getInfoOfHv("core1.id") + "", is("1"));
        assertThat(core.getInfoOfHv("core1.passwd") + "", is("234513"));
        assertThat(core.getInfoOfHv("info1.department") + "", is("信工计算机"));
    }

    @Test
    public void test2_getInfoOfHv() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1.id", "1");
        hv1.put("passwd", "234513");
        hv1.put("department", "信工计算机");
        CoreView core = new SubCoreView1(hv1);
        assertThat(core.getInfoOfHv("room"), is(nullValue()));
    }
}
