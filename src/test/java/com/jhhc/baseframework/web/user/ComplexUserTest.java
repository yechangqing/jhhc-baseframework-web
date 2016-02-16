package com.jhhc.baseframework.web.user;

import com.jhhc.baseframework.record.SqlOperator;
import com.jhhc.baseframework.test.Base;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author yecq
 */
public class ComplexUserTest extends Base {

    @Autowired
    private SqlOperator sql;

    private Map<String, Object> do_getExistParam(ComplexUser obj) {
        try {
            Method method = AbstractComplexUser.class.getDeclaredMethod("do_getExistParam");
            method.setAccessible(true);
            return (Map<String, Object>) method.invoke(obj);
        } catch (InvocationTargetException ex) {
            if (ex.getCause().getClass().equals(IllegalArgumentException.class)) {
                throw new IllegalArgumentException(ex.getCause().getMessage());
            } else if (ex.getCause().getClass().equals(IllegalStateException.class)) {
                throw new IllegalStateException(ex.getCause().getMessage());
            } else {
                throw new RuntimeException(ex.getCause().getMessage());
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException ex) {
            throw new RuntimeException(ex.getCause().getMessage());
        }
    }

    @Test
    public void test_ComplexUser1() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("视图v_user不存在");
        User user = new ComplexUser1("8");
    }

    @Test
    public void test_ComplexUser2() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表info2不存在");
        User user = new ComplexUser2("8");
    }

    @Test
    public void test_ComplexUser23() {
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("视图不存在字段in_id");
        User user = new ComplexUser3("8");
    }

    @Test
    public void test_ComplexUser() {
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("记录不存在");
        User user = new ComplexUser("6");
    }

    @Test
    public void test1_ComplexUser() {
        new ComplexUser("1");
    }

    @Test
    public void test2_ComplexUser() {
        Map hv1 = new HashMap();
        hv1.put("username", "qwer");
        hv1.put("passwd", "123456");
        hv1.put("identity_number1", "989098");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("不存在字段identity_number1");
        new ComplexUser(hv1);
    }

    @Test
    public void test21_ComplexUser() {
        Map hv1 = new HashMap();
        hv1.put("user12.username", "qwer");
        hv1.put("passwd", "123456");
        hv1.put("info12.identity_number1", "989098");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("表info12不含有字段identity_number1");
        new ComplexUser(hv1);
    }

    @Test
    public void test3_ComplexUser() {
        Map hv1 = new HashMap();
        hv1.put("username", "qwer");
        hv1.put("passwd", "123456");
        hv1.put("identity_number", "989098");
        new ComplexUser(hv1);
    }

    @Test
    public void test_do_getExistParam() {
        Map hv = new HashMap();
        hv.put("username", "abcd");
        hv.put("info12.identity_number", "11231");
        hv.put("user12.passwd", "ABCCD");
        ComplexUser user = new ComplexUser(hv);
        Map<String, Object> ret = do_getExistParam(user);
        assertThat(ret.size(), is(3));
        assertThat(ret.get("user12.username") + "", is("abcd"));
        assertThat(ret.get("v_user12.username") + "", is("abcd"));
        assertThat(ret.get("info12.identity_number") + "", is("11231"));
    }

    @Test
    public void test1_do_getExistParam() {
        Map hv = new HashMap();
        hv.put("username", "abcd");
        hv.put("user12.passwd", "ABCCD");
        hv.put("user12_id", "1");
        ComplexUser user = new ComplexUser(hv);
        Map<String, Object> ret = do_getExistParam(user);
        assertThat(ret.size(), is(2));
        assertThat(ret.get("user12.username") + "", is("abcd"));
        assertThat(ret.get("v_user12.username") + "", is("abcd"));
    }

    @Test
    public void test_exist() {
        Map hv1 = new HashMap();
        hv1.put("username", "zxcv");
        hv1.put("passwd", "1234");
        hv1.put("identity_number", "123454");
        User user = new ComplexUser(hv1);
        boolean b = user.exist();
        assertThat(b, is(true));
    }

    @Test
    public void test1_exist() {
        Map hv1 = new HashMap();
        hv1.put("username", "yecq1");
        hv1.put("passwd", "1234");
        hv1.put("identity_number", "320414");
        User user = new ComplexUser(hv1);
        boolean b = user.exist();
        assertThat(b, is(true));
    }

    @Test
    public void test2_exist() {
        Map hv1 = new HashMap();
        hv1.put("username", "yecq1");
        hv1.put("passwd", "1234");
        hv1.put("identity_number", "23455");
        User user = new ComplexUser(hv1);
        boolean b = user.exist();
        assertThat(b, is(false));
    }

    @Test
    public void test_check() {
        Map hv1 = new HashMap();
        hv1.put("username", "yecq");
        User user = new ComplexUser(hv1);
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("需要用户名和密码来验证");
        user.check();
    }

    @Test
    public void test1_check() {
        Map hv1 = new HashMap();
        hv1.put("username", "yecq");
        hv1.put("passwd", "1234");
        User user = new ComplexUser(hv1);
        String id = user.check();
        assertThat(id, is(nullValue()));
    }

    @Test
    public void test2_check() {
        Map hv1 = new HashMap();
        hv1.put("username", "abcd3");
        hv1.put("passwd", "1234");
        User user = new ComplexUser(hv1);
        String id = user.check();
        assertThat(id, is(nullValue()));
    }

    @Test
    public void test3_check() {
        Map hv1 = new HashMap();
        hv1.put("username", "abcde");
        hv1.put("passwd", "12345");
        User user = new ComplexUser(hv1);
        String id = user.check();
        assertThat(id, not(nullValue()));
    }

    @Test
    public void test_remove() {
        User user = new ComplexUser("1");
        user.remove();
        String stmt = "select * from v_user12 where id=1";
        List list = sql.query(stmt);
        assertThat(list.size(), is(0));
    }

    @Test
    public void test_getInfo() {
        User user = new ComplexUser("1");
        assertThat(user.getInfo("username") + "", is("abcde"));
        assertThat(user.getInfo("passwd") + "", is("12345"));
        assertThat(user.getInfo("identity_number") + "", is("320219"));
        assertThat(user.getInfo("info12_id") + "", is("3"));
    }

    @Test
    public void test_verify() {
        User user = new ComplexUser("2");
        boolean b = user.verify("asdffgh");
        assertThat(b, is(false));
    }

    @Test
    public void test1_verify() {
        User user = new ComplexUser("1");
        boolean b = user.verify("12345");
        assertThat(b, is(true));
    }

    @Test
    public void test_modifyPasswd() {
        User user = new ComplexUser("1");
        user.modifyPasswd("54321");
        assertThat(user.getInfo("passwd") + "", is("54321"));
    }

    @Test
    public void test_modify() {
        User user = new ComplexUser("1");
        Map hv = new HashMap();
        hv.put("passwd1", "1234");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("不存在字段passwd1");
        user.modify(hv);
    }

    @Test
    public void test1_modify() {
        User user = new ComplexUser("1");
        Map hv = new HashMap();
        hv.put("passwd", "121212");
        hv.put("username", "jqh");
        user.modify(hv);
        assertThat(user.getInfo("username") + "", is("jqh"));
        assertThat(user.getInfo("passwd") + "", is("121212"));
    }

    @Test
    public void test2_modify() {
        User user = new ComplexUser("2");
        Map hv = new HashMap();
        hv.put("identity_number", "898998");
        user.modify(hv);
        assertThat(user.getInfo("username") + "", is("zxcv"));
        assertThat(user.getInfo("identity_number") + "", is("898998"));
    }

    @Test
    public void test3_modify() {
        User user = new ComplexUser("1");
        Map hv = new HashMap();
        hv.put("passwd", "8011111");
        hv.put("identity_number", "900900");
        user.modify(hv);
        assertThat(user.getInfo("username") + "", is("abcde"));
        assertThat(user.getInfo("passwd") + "", is("8011111"));
        assertThat(user.getInfo("identity_number") + "", is("900900"));
    }

    @Test
    public void test_add() {
        Map hv = new HashMap();
        hv.put("username", "ioioio");
        hv.put("passwd1", "12345678");
        hv.put("identity_number", is("3455091"));
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("不存在字段passwd1");
        new ComplexUser(hv).add();
    }

    @Test
    public void test1_add() {
        Map hv = new HashMap();
        hv.put("username", "ioioio");
        hv.put("identity_number", ("34566543"));
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("必须有用户名和密码");
        new ComplexUser(hv).add();
    }

    @Test
    public void test2_add() {
        Map hv = new HashMap();
        hv.put("username", "ioioio");
        hv.put("passwd", "12345687");
        hv.put("identity_number", "34566543");
        String id = new ComplexUser(hv).add();
        assertThat(id, not(nullValue()));
    }

    @Test
    public void test3_add() {
        Map hv = new HashMap();
        hv.put("username", "abcde");
        hv.put("passwd", "12345687");
        hv.put("identity_number", "34566543");
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("用户已存在");
        new ComplexUser(hv).add();
    }

    @Test
    public void test4_add() {
        Map hv = new HashMap();
        hv.put("username", "bddaf");
        hv.put("passwd", "12345687");
        hv.put("identity_number", "320219");
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("用户已存在");
        new ComplexUser(hv).add();
    }
}
