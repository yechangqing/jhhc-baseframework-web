package com.jhhc.baseframework.web.user;

import java.util.Map;

/**
 *
 * @author yecq
 */
public class ComplexUser extends AbstractComplexUser {

    public ComplexUser(String id) {
        super(id);
    }

    public ComplexUser(Map<String, Object> hv) {
        super(hv);
    }

    @Override
    public String getUserTable() {
        return "user12";
    }

    @Override
    protected String getInfoTable() {
        return "info12.info12_id";
    }

    @Override
    protected String getViewTable() {
        return "v_user12";
    }

    @Override
    public String[] unique() {
        return new String[]{"username", "identity_number"};
    }

}
