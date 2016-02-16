package com.jhhc.baseframework.web.core;

import com.jhhc.baseframework.test.Base;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author yecq
 */
public class CoreView2233Test extends Base {

    @Test
    public void test_initHeader() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("视图v_core22不存在");
        CoreView s = new SubCoreView22("1");
    }

    @Test
    public void test1_CoreView22() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("视图v_core22不存在");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("id", "3");
        new SubCoreView22(hv1);
    }

    @Test
    public void test1_CoreView33() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表core33不存在");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("id", "3");
        new SubCoreView33(hv1);
    }

    @Test
    public void test1_CoreView3333() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("视图不存在字段a_id");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1_id", "3");
        new SubCoreView3333(hv1);
    }

    @Test
    public void test1_CoreView44() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表info23不存在");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1_id", "3");
        new SubCoreView44(hv1);
    }

    @Test
    public void test1_CoreView4444() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("视图不存在字段info_id");
        Map<String, Object> hv1 = new HashMap();
        hv1.put("core1_id", "3");
        new SubCoreView4444(hv1);
    }

    @Test
    public void test1_CoreView55() {
        Core core = new SubCoreView55("3");
        assertThat(core.getId(), is("3"));
        assertThat(core.getInfo("info1_id") + "", is("8"));
    }

    ////////////////////
    // 视图名称错
    private class SubCoreView22 extends CoreView {

        public SubCoreView22(Map<String, Object> hv1) {
            super(hv1);
        }

        public SubCoreView22(String id) {
            super(id);
        }

        @Override
        public String getView() {
            return "v_core22";
        }

        @Override
        public String getMainTable() {
            return "core1";
        }

        @Override
        public String[] getSlaveTables() {
            return new String[]{"info1.info1_id"};
        }
    }

    // 主表名称错
    private class SubCoreView33 extends CoreView {

        public SubCoreView33(Map<String, Object> hv1) {
            super(hv1);
        }

        public SubCoreView33(String id) {
            super(id);
        }

        @Override
        public String getView() {
            return "v_core1";
        }

        @Override
        public String getMainTable() {
            return "core33";
        }

        @Override
        public String[] getSlaveTables() {
            return new String[]{"info1.info1_id"};
        }
    }

    // 主表的id名错
    private class SubCoreView3333 extends CoreView {

        public SubCoreView3333(Map<String, Object> hv1) {
            super(hv1);
        }

        public SubCoreView3333(String id) {
            super(id);
        }

        @Override
        public String getView() {
            return "v_core1";
        }

        @Override
        public String getMainTable() {
            return "core1.a_id";
        }

        @Override
        public String[] getSlaveTables() {
            return new String[]{"info1.info1_id"};
        }
    }

    // 从表名称错
    private class SubCoreView44 extends CoreView {

        public SubCoreView44(Map<String, Object> hv1) {
            super(hv1);
        }

        public SubCoreView44(String id) {
            super(id);
        }

        @Override
        public String getView() {
            return "v_core1";
        }

        @Override
        public String getMainTable() {
            return "core1.id";
        }

        @Override
        public String[] getSlaveTables() {
            return new String[]{"info23"};
        }
    }

    // 从表字段错
    private class SubCoreView4444 extends CoreView {

        public SubCoreView4444(Map<String, Object> hv1) {
            super(hv1);
        }

        public SubCoreView4444(String id) {
            super(id);
        }

        @Override
        public String getView() {
            return "v_core1";
        }

        @Override
        public String getMainTable() {
            return "core1.id";
        }

        @Override
        public String[] getSlaveTables() {
            return new String[]{"info1.info_id"};
        }
    }

    // 从表省略id名称
    private class SubCoreView55 extends CoreView {

        public SubCoreView55(Map<String, Object> hv1) {
            super(hv1);
        }

        public SubCoreView55(String id) {
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
            return new String[]{"info1"};
        }
    }
}
