package com.jhhc.baseframework.web.core;

import com.jhhc.baseframework.test.Base;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yecq
 */
public class CoreSelectorTest extends Base {

    @Autowired
    private CoreSelector objs;

    @Test
    public void test0_getTable() {
        SubCore core3 = new SubCore("1");
        assertThat(objs.getTable(null, core3.getClass()), is("core1"));
    }

    @Test
    public void test01_getTable() {
        SubCore core3 = new SubCore("1");
        assertThat(objs.getTable(core3, core3.getClass()), is("core1"));
    }

    @Test
    public void test1_getTable() {
        Core core3 = new SubCoreView1("3");
        assertThat(objs.getTable(null, core3.getClass()), is("v_core1"));
    }

    @Test
    public void test11_getTable() {
        Core core3 = new SubCoreView1("3");
        assertThat(objs.getTable(core3, core3.getClass()), is("v_core1"));
    }

    @Test
    public void test_isAssignableFrom() {
        Class c1 = CoreTable.class;
        Map hv = new HashMap();
        hv.put("id", "3");
        Class c3 = new SubCore(hv).getClass();
        boolean b = c1.isAssignableFrom(c3);        // true
        assertThat(b, is(true));
    }

    @Test
    public void test_getList() {
        List<SubCore> list = objs.getList(null, null, SubCore.class);
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getId(), is("1"));
        assertThat(list.get(1).getId(), is("2"));
        assertThat(list.get(2).getId(), is("3"));
    }

    @Test
    public void test1_getList() {
        List<SubCore> list = objs.getList("id>2", null, SubCore.class);
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getId(), is("3"));
    }

    @Test
    public void test2_getList() {
        List<SubCore> list = objs.getList("id>0", new String[]{"username"}, SubCore.class);
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getId(), is("2"));
        assertThat(list.get(1).getId(), is("3"));
        assertThat(list.get(2).getId(), is("1"));
    }

    @Test
    public void test3_getList() {
        List<SubCore> list = objs.getList("id>0", SubCore.class);
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getId(), is("1"));
        assertThat(list.get(1).getId(), is("2"));
        assertThat(list.get(2).getId(), is("3"));
    }

    @Test
    public void test4_getList() {
        String condition = "name=?";
        Object[] args = new Object[]{"ABC"};
        List<SubCore> list = objs.getList(condition, args, null, SubCore.class);
        assertThat(list.size(), is(2));
    }

    @Test
    public void test5_getList() {
        String condition = "name=? or username=?";
        Object[] args = new Object[]{"ABC", "yecq"};
        List<SubCore> list = objs.getList(condition, args, new String[]{"name", "id"}, SubCore.class);
        assertThat(list.size(), is(3));
        assertThat(list.get(0).getId(), is("2"));
        assertThat(list.get(1).getId(), is("3"));
        assertThat(list.get(2).getId(), is("1"));
    }

    @Test
    public void test_getListByOr() {
        Map<String, Object> map = new HashMap();
        map.put("name", "叶小怜");
        List<SubCore> list = objs.getListByOr(map, SubCore.class);
        assertThat(list.size(), is(1));

        map.clear();
        map.put("name", "叶小怜");
        map.put("email", "abc@123.com");
        list = objs.getListByOr(map, SubCore.class);
        assertThat(list.size(), is(2));
    }

    @Test
    public void test_getListByAnd() {
        Map<String, Object> map = new HashMap();
        map.put("name", "叶小怜");
        map.put("passwd", "afdas");
        List<SubCore> list = objs.getListByAnd(map, SubCore.class);
        assertThat(list.size(), is(0));

        map.clear();
        map.put("username", "yecq");
        map.put("passwd", "1234");
        list = objs.getListByAnd(map, SubCore.class);
        assertThat(list.size(), is(1));
    }

    @Test
    public void test0_SubCore() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("必须是Core类型");
        List list = objs.getList(null, null, String.class);
    }

    // 都是视图
    @Test
    public void test0_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("username", "abcd");
        hv.put("core1_id", "3");
        boolean b = objs.existOfViewByOr(hv, SubCoreView1.class);
        assertThat(b, is(true));
    }

    @Test
    public void test01_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("username", "abcd");
        hv.put("core1_id", "3");
        boolean b = objs.existOfViewByOr(hv, new SubCoreView1(hv));
        assertThat(b, is(true));
    }

    // 表
    @Test
    public void test1_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("core1_id", "3");
        boolean b = objs.existOfViewByOr(hv, SubCoreView1.class);
        assertThat(b, is(true));
    }

    @Test
    public void test11_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("core1_id", "3");
        boolean b = objs.existOfViewByOr(hv, new SubCoreView1(hv));
        assertThat(b, is(true));
    }

    // 视图和表
    @Test
    public void test2_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("core1_id", "3");
        hv.put("v_core1.username", "abcd");

        boolean b = objs.existOfViewByOr(hv, SubCoreView1.class);
        assertThat(b, is(true));
    }

    @Test
    public void test21_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("core1_id", "3");
        hv.put("v_core1.username", "abcd");

        boolean b = objs.existOfViewByOr(hv, new SubCoreView1(hv));
        assertThat(b, is(true));
    }

    @Test
    public void test3_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("core1.username", "abcd");
        hv.put("info1.id", "8");
        boolean b = objs.existOfViewByOr(hv, SubCoreView1.class);
        assertThat(b, is(true));
    }

    @Test
    public void test31_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("core1.username", "abcd");
        hv.put("info1.id", "8");
        boolean b = objs.existOfViewByOr(hv, new SubCoreView1(hv));
        assertThat(b, is(true));
    }

    @Test
    public void test4_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("username", "abcd1");
        hv.put("info1.id", "19");
        boolean b = objs.existOfViewByOr(hv, SubCoreView1.class);
        assertThat(b, is(false));
    }

    @Test
    public void test41_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("username", "abcd1");
        hv.put("info1.id", "19");
        boolean b = objs.existOfViewByOr(hv, new SubCoreView1(hv));
        assertThat(b, is(false));
    }

    @Test
    public void test5_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("username", "abcd1");
        hv.put("info1.id", "8");
        boolean b = objs.existOfViewByOr(hv, SubCoreView1.class);
        assertThat(b, is(true));
    }

    @Test
    public void test51_existOfViewByOr() {
        Map hv = new HashMap();
        hv.put("username", "abcd1");
        hv.put("info1.id", "8");
        boolean b = objs.existOfViewByOr(hv, new SubCoreView1(hv));
        assertThat(b, is(true));
    }

    @Test
    public void test_getList_table() {
        List<Map<String, Object>> list = objs.getList("id>0", new String[]{"username"}, "core1");
        assertThat(list.size(), is(3));
        assertThat(list.get(0).get("id") + "", is("2"));
        assertThat(list.get(1).get("id") + "", is("3"));
        assertThat(list.get(2).get("id") + "", is("1"));
    }

    @Test
    public void test1_getList_table() {
        String condition = "name=?";
        Object[] args = new Object[]{"ABC"};
        List<Map<String, Object>> list = objs.getList(condition, args, null, "core1");
        assertThat(list.size(), is(2));
    }

    @Test
    public void test_getListByOr_table() {
        Map<String, Object> map = new HashMap();
        map.put("name", "叶小怜");
        List<Map<String, Object>> list = objs.getListByOr(map, "core1");
        assertThat(list.size(), is(1));

        map.clear();
        map.put("name", "叶小怜");
        map.put("email", "abc@123.com");
        list = objs.getListByOr(map, "core1");
        assertThat(list.size(), is(2));
    }

    @Test
    public void test_getListByAnd_table() {
        Map<String, Object> map = new HashMap();
        map.put("name", "叶小怜");
        map.put("passwd", "afdas");
        List<Map<String, Object>> list = objs.getListByAnd(map, "core1");
        assertThat(list.size(), is(0));

        map.clear();
        map.put("username", "yecq");
        map.put("passwd", "1234");
        list = objs.getListByAnd(map, "core1");
        assertThat(list.size(), is(1));
    }
}
