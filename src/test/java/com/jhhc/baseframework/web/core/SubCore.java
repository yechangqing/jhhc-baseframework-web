package com.jhhc.baseframework.web.core;

import java.util.Map;

/**
 *
 * @author yecq
 */
public class SubCore extends CoreTable {

    public SubCore(String id) {
        super(id);
    }

    public SubCore(Map<String, Object> hv) {
        super(hv);
    }

    @Override
    public String getTable() {
        return "core1";
    }
}
