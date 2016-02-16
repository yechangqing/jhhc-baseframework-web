package com.jhhc.baseframework.web.core;

import com.jhhc.baseframework.test.Base;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 *
 * @author yecq
 */
public class CoreTest extends Base {

    @Test
    public void test_CoreNew() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表core不存在");
        CoreTable core = new SubCore1("1");
    }

    @Test
    public void test1_CoreNew() {
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("记录不存在");
        new SubCore("12");
    }

    @Test
    public void test2_CoreNew() {
        new SubCore("1");
    }

    @Test
    public void test3_CoreNew() {
        Map<String, Object> hv = new HashMap();
        hv.put("username", "abcd");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表core不存在");
        new SubCore1(hv);
    }

    @Test
    public void test4_CoreNew() {
        Map<String, Object> hv = new HashMap();
        hv.put("username", "abcd");
        new SubCore(hv);
    }

    @Test
    public void test41_CoreNew() {
        Map<String, Object> hv = new HashMap();
        hv.put("core1.username", "abcd");
        new SubCore(hv);
    }

    @Test
    public void test5_CoreNew() {
        Map<String, Object> hv = new HashMap();
        hv.put("username1", "abcd");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("没有属性username1");
        new SubCore(hv);
    }

    @Test
    public void test6_CoreNew() {
        Map<String, Object> hv = new HashMap();
        hv.put("username1", "abcd");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表core不存在");
        new SubCore1(hv);
    }

    @Test
    public void test7_CoreNew() {
        new SubCore(new HashMap());
    }

    @Test
    public void test_changeId() {
        CoreTable core = new SubCore("1");
        core.changeId("2");
        assertThat(core.getId(), is("2"));
        assertThat(core.getInfo("id") + "", is("2"));
        assertThat(core.getInfo("username") + "", is("aaa"));
    }

    @Test
    public void test1_changeId() {
        CoreTable core = new SubCore("1");
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("记录不存在");
        core.changeId("10");
    }

    @Test
    public void test_changeHv() {
        Map<String, Object> hv = new HashMap();
        hv.put("core1.username", "abcd");
        hv.put("passwd", "1234");
        CoreTable core = new SubCore(hv);
        hv.clear();
        hv.put("username", "ioio");
        hv.put("core1.passwd", "123465");
        core.changeHv(hv);
        assertThat(core.getInfoOfHv("username") + "", is("ioio"));
        assertThat(core.getInfoOfHv("core1.passwd") + "", is("123465"));
    }

    @Test
    public void test1_changeHv() {
        Map<String, Object> hv = new HashMap();
        hv.put("core1.username", "abcd");
        hv.put("passwd", "1234");
        CoreTable core = new SubCore(hv);
        hv.clear();
        hv.put("username1", "ioio");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("没有属性username1");
        core.changeHv(hv);
    }

    @Test
    public void test_getInfoOfHv() {
        Map<String, Object> hv = new HashMap();
        hv.put("username", "abcd");
        CoreTable core = new SubCore(hv);
        assertThat(core.getInfoOfHv("username") + "", is("abcd"));
    }

    @Test
    public void test1_getInfoOfHv() {
        Map<String, Object> hv = new HashMap();
        hv.put("core1.username", "abcd");
        CoreTable core = new SubCore(hv);
        assertThat(core.getInfoOfHv("username") + "", is("abcd"));
    }

    @Test
    public void test2_getInfoOfHv() {
        Map<String, Object> hv = new HashMap();
        hv.put("core1.username", "abcd");
        CoreTable core = new SubCore(hv);
        assertThat(core.getInfoOfHv("core1.username") + "", is("abcd"));
    }

    @Test
    public void test3_getInfoOfHv() {
        Map<String, Object> hv = new HashMap();
        hv.put("core1.username", "abcd");
        CoreTable core = new SubCore(hv);

        assertThat(core.getInfoOfHv("zxcv"), is(nullValue()));
    }

    @Test
    public void test_getInfo() {
        CoreTable core = new SubCore("1");
        Map<String, Object> map = core.getInfo();
        assertThat(map.get("id") + "", is("1"));
        assertThat(map.get("username") + "", is("yecq"));
        assertThat(map.get("passwd") + "", is("1234"));
        assertThat(map.get("name") + "", is("叶小怜"));
        assertThat(map.get("email") + "", is("yecq@123.com"));
    }

    @Test
    public void test1_getInfo() {
        CoreTable core = new SubCore("1");
        assertThat(core.getInfo("id") + "", is("1"));
        assertThat(core.getInfo("username") + "", is("yecq"));
        assertThat(core.getInfo("passwd") + "", is("1234"));
        assertThat(core.getInfo("name") + "", is("叶小怜"));
        assertThat(core.getInfo("email") + "", is("yecq@123.com"));
    }

    @Test
    public void test_add() {
        Map<String, Object> hv1 = new HashMap();
        hv1.put("username", "zxcv");
        hv1.put("passwd", "uiop");
        hv1.put("name", "ZZCD");
        hv1.put("email", "zxcv@123.cc");
        String id = new SubCore(hv1).add();
        assertThat(id, not(nullValue()));
    }

    @Test
    public void test_remove() {
        new SubCore("2").remove();
        String stmt = "select * from core1 where id=2";
        List list = Root.getInstance().getSqlOperator().query(stmt);
        assertThat(list.size(), is(0));
    }

    @Test
    public void test_modify() {
        CoreTable core = new SubCore("1");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("passwd", "90901");
        hv1.put("email", "90901@qq.com");
        core.modify(hv1);
        assertThat(core.getInfo("passwd") + "", is("90901"));
        assertThat(core.getInfo("email") + "", is("90901@qq.com"));
    }

    @Test
    public void test1_modify() {
        CoreTable core = new SubCore("1");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("passwd", "90901");
        hv1.put("email1", "90901@qq.com");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("没有属性email1");
        core.modify(hv1);
    }

    @Test
    public void test_analysisTableHeader() {
        CoreTable core = new SubCore4Ana();
        String[] r = core.analysisTableHeader("username");
        assertThat(r[0], is("core1"));
        assertThat(r[1], is("username"));
    }

    @Test
    public void test1_analysisTableHeader() {
        CoreTable core = new SubCore4Ana();
        String[] r = core.analysisTableHeader("core1.username");
        assertThat(r[0], is("core1"));
        assertThat(r[1], is("username"));
    }

    @Test
    public void test2_analysisTableHeader() {
        CoreTable core = new SubCore4Ana();
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("不是表core1");
        String[] r = core.analysisTableHeader("core.username");
    }

    @Test
    public void test3_analysisTableHeader() {
        CoreTable core = new SubCore4Ana();
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表core1不含有字段username1");
        String[] r = core.analysisTableHeader("core1.username1");
    }

    private class SubCore4Ana extends CoreTable {

        public SubCore4Ana() {
        }

        public SubCore4Ana(String id) {
            super(id);
        }

        public SubCore4Ana(Map<String, Object> hv) {
            super(hv);
        }

        @Override
        public String getTable() {
            return "core1";
        }

    }
}
