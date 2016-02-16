package com.jhhc.baseframework.web.user;

import com.jhhc.baseframework.test.Base;
import com.jhhc.baseframework.web.core.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author yecq
 */
public class SimpleUserTest extends Base {

    @Test
    public void test0_exist() {
        Map hv = new HashMap();
        hv.put("username", "yecq");
        hv.put("name", "叶小怜1");
        boolean b = new SimpleUser(hv).exist();
        assertThat(b, is(true));
    }

    @Test
    public void test01_exist() {
        Map hv = new HashMap();
        hv.put("user.username", "yecq");
        hv.put("user.name", "叶小怜1");
        boolean b = new SimpleUser(hv).exist();
        assertThat(b, is(true));
    }

    @Test
    public void test02_exist() {
        Map hv = new HashMap();
        hv.put("username", "yecq");
        hv.put("user.name", "叶小怜1");
        boolean b = new SimpleUser(hv).exist();
        assertThat(b, is(true));
    }

    @Test
    public void test1_exist() {
        Map hv = new HashMap();
        hv.put("username", "qwert");
        hv.put("email", "yecq@123.com");
        boolean b = new SimpleUser(hv).exist();
        assertThat(b, is(true));
    }

    @Test
    public void test2_exist() {
        Map hv = new HashMap();
        hv.put("username", "qwer");
        hv.put("email", "abcd@123.com");
        boolean b = new SimpleUser(hv).exist();
        assertThat(b, is(false));
    }

    @Test
    public void test_check() {
        Map hv = new HashMap();
        hv.put("username", "yecq");
        hv.put("passwd", "1234");
        String id = new SimpleUser(hv).check();
        assertThat(id, is("1"));
    }

    @Test
    public void test2_check() {
        Map hv = new HashMap();
        hv.put("username", "abcd");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("需要用户名和密码来验证");
        String id = new SimpleUser(hv).check();
    }

    @Test
    public void test3_check() {
        Map hv = new HashMap();
        hv.put("passwd", "abcd");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("需要用户名和密码来验证");
        String id = new SimpleUser(hv).check();
    }

    @Test
    public void test4_check() {
        Map hv = new HashMap();
        hv.put("username", "yecq1");
        hv.put("passwd", "asdf");
        String id = new SimpleUser(hv).check();
        assertThat(id, is(nullValue()));
    }

    @Test
    public void test5_check() {
        Map hv = new HashMap();
        hv.put("username", "yecq");
        hv.put("passwd", "asdf");
        String id = new SimpleUser(hv).check();
        assertThat(id, is(nullValue()));
    }

    @Test
    public void test6_check() {
        Map hv = new HashMap();
        hv.put("username", "yecq");
        hv.put("passwd", "1234");
        String id = new SimpleUser(hv).check();
        assertThat(id, is("1"));
    }

    @Test
    public void test_add() {
        Map map = new HashMap();
        map.put("name", "姓名1");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("必须有用户名和密码");
        String id = new SimpleUser(map).add();
    }

    @Test
    public void test1_add() {
        Map map = new HashMap();
        map.put("username", "yecq");
        map.put("passwd", "1234566");
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("用户已存在");
        String id = new SimpleUser(map).add();
    }

    @Test
    public void test11_add() {
        Map map = new HashMap();
        map.put("username", "yecq");
        map.put("passwd", "1234566");
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("用户已存在");
        String id = new SimpleUser(map).add();
    }

    @Test
    public void test2_add() {
        Map map = new HashMap();
        map.put("username", "yecq1");
        map.put("passwd", "12345687");
        map.put("name", "叶小怜1");
        map.put("email", "qwer@123.com");
        String id = new SimpleUser(map).add();
        assertThat(id, not(nullValue()));
    }

    @Test
    public void test_remove() {
        new SimpleUser("1").remove();
        String stmt = "select * from user where id =1";
        List list = Root.getInstance().getSqlOperator().query(stmt);
        assertThat(list.size(), is(0));
    }

    @Test
    public void test_modify() {
        Map map = new HashMap();
        map.put("abcd1", "asdf");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("没有属性abcd1");
        new SimpleUser("1").modify(map);
    }

    @Test
    public void test1_modify() {
        Map map = new HashMap();
        map.put("name", "qwer1");
        map.put("email", "zxcb@124.com");
        new SimpleUser("1").modify(map);
        String stmt = "select * from user where id=1";
        List<Map<String, Object>> list = Root.getInstance().getSqlOperator().query(stmt);
        Map<String, Object> tmp = list.get(0);
        assertThat(tmp.get("name") + "", is("qwer1"));
        assertThat(tmp.get("email") + "", is("zxcb@124.com"));
    }

    @Test
    public void test_getInfo() {
        String v = new SimpleUser("1").getInfo("name") + "";
        assertThat(v, is("叶小怜"));
    }

    @Test
    public void test_verify() {
        boolean b = new SimpleUser("1").verify("1234");
        assertThat(b, is(true));
        b = new SimpleUser("1").verify("123456");
        assertThat(b, is(false));
    }

    @Test
    public void test_modifyPasswd() {
        User user = new SimpleUser("1");
        user.modifyPasswd("zxcvb");
        assertThat(user.getInfo("passwd") + "", is("zxcvb"));
    }

    @Test
    public void test_SimpleUser2() {
        new SimpleUser2("1");
    }

    @Test
    public void test1_SimpleUser2() {
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("记录不存在");
        new SimpleUser2("4");
    }

    @Test
    public void test3_SimpleUser2() {
        Map<String, Object> hv = new HashMap();
        hv.put("username", "abcd");
        new SimpleUser2(hv);
    }

    @Test
    public void test4_SimpleUser2() {
        Map<String, Object> hv = new HashMap();
        hv.put("adsfas", "abcd");
        this.expectedEx.expect(IllegalArgumentException.class);
        this.expectedEx.expectMessage("没有属性adsfas");
        new SimpleUser2(hv);
    }

    @Test
    public void test5_SimpleUser2() {
        this.expectedEx.expect(IllegalStateException.class);
        this.expectedEx.expectMessage("用户不存在");
        new SimpleUser2("abcder", true);
    }

    @Test
    public void test6_SimpleUser() {
        User user = new SimpleUser2("abcd", true);
        assertThat(user.getInfo("id") + "", is("2"));
    }
}
