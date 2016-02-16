package com.jhhc.baseframework.web.core;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author yecq
 */
class MetaData4View implements CoreMetaData {

    private CoreView core;

    public MetaData4View(CoreView core) {
        this.core = core;
    }

    @Override
    public String getType() {
        return "view";
    }

    @Override
    public String getTableName() {
        return this.core.getView();
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
        Map<String, Set<String>> map = this.core.tb_headers;
        Set<String> h = map.keySet();
        String[] ret = new String[h.size()];
        h.toArray(ret);
        return ret;
    }

}
