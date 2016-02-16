package com.jhhc.baseframework.web.core;

import java.util.Map;

/**
 *
 * @author yecq
 */
public class SubCoreView33 extends CoreView {

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
