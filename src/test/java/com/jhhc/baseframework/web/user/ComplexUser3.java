package com.jhhc.baseframework.web.user;

import java.util.Map;

/**
 *
 * @author yecq
 */
public class ComplexUser3 extends AbstractComplexUser {

    public ComplexUser3(String id) {
        super(id);
    }

    public ComplexUser3(Map<String, Object> hv1) {
        super(hv1);
    }

    @Override
    protected String getInfoTable() {
        return "info12.in_id";
    }

    @Override
    protected String getViewTable() {
        return "v_user12";
    }

    @Override
    public String[] unique() {
        return new String[]{"username"};
    }
}
