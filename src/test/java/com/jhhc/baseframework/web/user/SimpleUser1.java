package com.jhhc.baseframework.web.user;

import java.util.Map;

/**
 *
 * @author yecq
 */
public class SimpleUser1 extends AbstractSimpleUser {

    public SimpleUser1(Map<String, Object> hv) {
        super(hv);
    }

    public SimpleUser1(String id) {
        super(id);
    }

    @Override
    public String[] unique() {
        return null;
    }

}
