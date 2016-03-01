package com.jhhc.baseframework.web.controller.restful;

import com.jhhc.baseframework.test.IntegrateRestfulBase;
import com.jhhc.baseframework.test.TestReturn;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author yecq
 */
public class ComplexRestfulControllerTest extends IntegrateRestfulBase {

    @Test
    public void test_gets1() {
        TestReturn ret = doGet("/contracts");
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("全部数据"));
    }

    @Test
    public void test_gets2() {
        // url中放入参数
        TestReturn ret = doGet("/contracts?a=12");
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("参数a=12"));
    }

    @Test
    public void test3_gets() {
        // url中放入参数
        TestReturn ret = doGet("/contracts?a=%7B%22direct%22%3A%22%E7%A9%BA%22%7D");
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("参数a={\"direct\":\"空\"}"));
    }

    @Test
    public void test_gets3() {
        Map map = new HashMap();
        map.put("c", "234");
        map.put("d", "ppp");
        TestReturn ret = doGet("/contracts", map);
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("c=234,d=ppp"));
    }

    @Test
    public void test_get() {
        TestReturn ret = doGet("/contracts/3");
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("对象id=3"));
    }

    @Test
    public void test_add() {
        Map map = new HashMap();
        map.put("name", "王小二");
        map.put("number", 13);
        TestReturn ret = doPost("/contracts", map);
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("添加属性2个"));
    }

    @Test
    public void test_remove() {
        TestReturn ret = doDelete("/contracts/4");
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("删除元素id=4"));
    }

    @Test
    public void test_modify() {
        Map map = new HashMap();
        map.put("name", "王小二1");
        map.put("number", 1996);
        TestReturn ret = doPut("/contracts/2", map);
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("修改元素id=2"));
    }

    ///// ComplexController1测试
    @Test
    public void test_log1() {
        TestReturn ret = doGet("/accounts");
        assertThat(ret.getHeader("status"), is("fail"));
        assertThat(ret.getHeader("message"), is("用户未登录"));
    }

    @Test
    public void test_log2() {
        TestReturn ret = doGet("/accounts/2");
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("获得对象id=2"));
    }

    @Test
    public void test_decode() {
        Map map = new HashMap();
        map.put("json", URLEncoder.encode("{\"direct\":\"空\"}"));
        TestReturn ret = doGet("/contracts/decode", map);
        assertThat(ret.getStatus(), is("ok"));
        assertThat(ret.getMessage(), is("收到参数json={\"direct\":\"空\"}"));
    }
}
