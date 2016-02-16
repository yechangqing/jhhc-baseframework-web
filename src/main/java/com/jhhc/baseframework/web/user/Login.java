package com.jhhc.baseframework.web.user;

import com.jhhc.baseframework.web.core.Root;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yecq
 */
abstract public class Login {

    private String id;
    private String username;
    private String passwd;
    private LoginManager manager;

    // 登陆用
    protected Login(String username, String passwd) {
        if (username == null || username.trim().equals("") || passwd == null) {
            throw new IllegalArgumentException("用户名或者密码为空");
        }
        this.id = null;
        this.username = username;
        this.passwd = passwd;
        this.manager = Root.getInstance().getBean(LoginManager.class);
    }

    public String getUsername() {
        return this.username;
    }

    abstract protected Class getUserClass();

    public User getUser() {
        Class cls = getUserClass();
        if (!User.class.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("必须是User类型");
        }
        User ret = null;
        try {
            Constructor cons = cls.getConstructor(String.class);
            Object obj = cons.newInstance(this.id);
            ret = (User) obj;
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
        return ret;
    }

    // 登录
    public String login() {
        // 看是否已经登录
        Login lo1 = this.manager.getLogin(this.username);
        if (lo1 != null) {
            if (!isRepeat()) {
                throw new IllegalStateException("已经登录，不允许重复登录");
            }

            this.id = lo1.getInfo("id") + "";
            return this.id;
        }

        // 检查是否有这样的用户
        Map map = new HashMap();
        map.put("username", this.username);
        map.put("passwd", this.passwd);
        String id1 = new DefaultUser(map).check();
        if (id1 == null) {
            throw new IllegalStateException("用户名或者密码错误");
        }

        // 放入LoginManager
        this.id = id1;
        Root.getInstance().getBean(LoginManager.class).addLogin(this);
        return this.id;
    }

    // 注销
    public void logout() {
        LoginManager mana = Root.getInstance().getBean(LoginManager.class);
        Login lo = mana.getLogin(this.username);
        if (lo == null) {
            throw new IllegalStateException("用户未登录");
        }
        mana.removeLogin(lo);
    }

    // 获取信息
    public Object getInfo(String key) {
        User user = getUser();
        return user.getInfo(key);
    }

    // 是否允许重复登录
    public boolean isRepeat() {
        return true;
    }

}
