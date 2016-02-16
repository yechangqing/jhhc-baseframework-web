package com.jhhc.baseframework.web.user;


/**
 *
 * @author yecq
 */
public class Login1 extends Login{

    public Login1(String username, String passwd) {
        super(username, passwd);
    }

    @Override
    protected Class getUserClass() {
        return SimpleUser.class;
    }
}
