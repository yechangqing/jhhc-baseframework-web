package com.jhhc.baseframework.web.user;

import java.util.Map;

/**
 *
 * @author yecq
 */
public class SimpleUser extends AbstractSimpleUser {

    public SimpleUser(Map<String, Object> hv) {
        super(hv);
    }

    public SimpleUser(String id) {
        super(id);
    }

    @Override
    public String[] unique() {
        return new String[]{"username", "email"};
    }

}
