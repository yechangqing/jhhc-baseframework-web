package com.jhhc.baseframework.web.core;

import java.util.Map;

/**
 *
 * @author yecq
 */
public class SubCore1 extends CoreTable {

    public SubCore1(String id) {
        super(id);
    }

    public SubCore1(Map<String, Object> hv) {
        super(hv);
    }

    @Override
    public String getTable() {
        return "core";
    }

}
