package com.jhhc.baseframework.web.user;

import java.util.Map;

/**
 *
 * @author yecq
 */
public class SimpleUser2 extends AbstractSimpleUser {

    public SimpleUser2(Map<String, Object> hv) {
        super(hv);
    }

    public SimpleUser2(String id) {
        super(id);
    }

    public SimpleUser2(String username, boolean flag) {
        super(username, flag);
    }

    @Override
    public String[] unique() {
        return null;
    }

}
