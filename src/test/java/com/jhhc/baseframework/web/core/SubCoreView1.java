package com.jhhc.baseframework.web.core;

import java.util.Map;

/**
 *
 * @author yecq
 */
public class SubCoreView1 extends CoreView {

    public SubCoreView1(Map<String, Object> hv1) {
        super(hv1);
    }

    public SubCoreView1(String id) {
        super(id);
    }

    @Override
    public String getMainTable() {
        return "core1";
    }

    @Override
    public String[] getSlaveTables() {
        return new String[]{"info1.info1_id"};
    }

    @Override
    public String getView() {
        return "v_core1";
    }

}
