package com.jhhc.baseframework.web.user;

import com.jhhc.baseframework.test.Base;
import java.sql.SQLException;
import org.dbunit.DatabaseUnitException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yecq
 */
public class Login1Test extends Base {

    @Autowired
    private LoginManager mana;

    @Before
    @Override
    public void before() throws DatabaseUnitException, SQLException {
        super.before();
        mana.clear();

        Login lo = new Login1("abcd", "123456");
        lo.login();
    }

    @Test
    public void test_Login() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("用户名或者密码为空");
        Login lo = new Login1(null, null);
    }

//    @Test
    public void test_getUser() {
        Login lo = mana.getLogin("abcd");
        User user = lo.getUser();
        assertThat(user, not(nullValue()));
        assertThat(user.getInfo("name") + "", is("ABC"));
    }

//    @Test
    public void test_login() {
        Login lo = new Login1("yecq", "1234");
        String id = lo.login();
        assertThat(id, is("1"));
        lo = mana.getLogin("yecq");
        assertThat(lo.getInfo("name") + "", is("叶小怜"));
    }

//    @Test
    public void test1_login() {
        Login lo = new Login1("yecq", "123456");
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("用户名或者密码错误");
        String id = lo.login();
    }

//    @Test
    public void test2_login() {
        Login lo = new Login1("yecq1", "1234");
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("用户名或者密码错误");
        String id = lo.login();
    }

//    @Test
    public void test_logout() {
        Login lo = mana.getLogin("abcd");
        lo.logout();
        assertThat(mana.getLogin("abcd"), is(nullValue()));
    }

//    @Test
    public void test1_logout() {
        Login lo = mana.getLogin("abcd");
        assertThat(lo, not(nullValue()));
        mana.removeLogin(lo);
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("用户未登录");
        lo.logout();
    }

//    @Test
    public void test_getInfo() {
        Login lo = mana.getLogin("abcd");
        assertThat(lo.getInfo("name") + "", is("ABC"));
        assertThat(lo.getInfo("email") + "", is("abc@123.com"));
    }
}
