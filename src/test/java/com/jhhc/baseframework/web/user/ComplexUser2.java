package com.jhhc.baseframework.web.user;

import java.util.Map;

/**
 *
 * @author yecq
 */
public class ComplexUser2 extends AbstractComplexUser {

    public ComplexUser2(String id) {
        super(id);
    }

    public ComplexUser2(Map<String, Object> hv1) {
        super(hv1);
    }

    @Override
    protected String getInfoTable() {
        return "info2";
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
