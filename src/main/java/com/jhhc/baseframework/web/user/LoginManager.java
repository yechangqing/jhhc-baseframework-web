package com.jhhc.baseframework.web.user;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 *
 * @author yecq
 */
@Component
public class LoginManager {

    private Map<String, Login> logins;   // username, Login

    public LoginManager() {
        this.logins = new ConcurrentHashMap();
    }

    public Login getLogin(String username) {
        return this.logins.get(username);
    }

    public void addLogin(Login lo) {
        this.logins.put(lo.getUsername(), lo);
    }

    public void removeLogin(Login lo) {
        this.logins.remove(lo.getUsername());
    }

    public Collection<Login> getList() {
        return this.logins.values();
    }

    public void clear() {
        this.logins.clear();
    }
}
