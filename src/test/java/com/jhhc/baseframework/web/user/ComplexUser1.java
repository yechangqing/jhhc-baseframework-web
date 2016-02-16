package com.jhhc.baseframework.web.user;

import java.util.Map;

/**
 *
 * @author yecq
 */
public class ComplexUser1 extends AbstractComplexUser {

    public ComplexUser1(String id) {
        super(id);
    }

    public ComplexUser1(Map<String, Object> hv1) {
        super(hv1);
    }

    @Override
    protected String getInfoTable() {
        return "info12.info12_id";
    }

    @Override
    protected String getViewTable() {
        return "v_user";
    }

    @Override
    public String[] unique() {
        return new String[]{"username"};
    }

}
