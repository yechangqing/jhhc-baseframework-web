package com.jhhc.baseframework.web.core;

import java.util.Set;

/**
 *
 * @author yecq
 */
class MetaData4Table implements CoreMetaData {

    private CoreTable core;

    public MetaData4Table(CoreTable core) {
        this.core = core;
    }

    @Override
    public String getType() {
        return "table";
    }

    @Override
    public String getTableName() {
        return this.core.getTable();
    }

    @Override
    public String[] getHeaders() {
        Set<String> h = this.core.header;
        String[] ret = new String[h.size()];
        h.toArray(ret);
        return ret;
    }

    @Override
    public String[] getTablesOfView() {
        throw new IllegalStateException("这不是视图");
    }
}
