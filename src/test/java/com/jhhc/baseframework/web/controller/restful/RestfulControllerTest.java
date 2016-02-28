package com.jhhc.baseframework.web.controller.restful;

import com.google.gson.Gson;
import com.jhhc.baseframework.test.IntegrateRestfulBase;
import com.jhhc.baseframework.test.TestReturn;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dbunit.DatabaseUnitException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 *
 * @author yecq
 */
public class RestfulControllerTest extends IntegrateRestfulBase {

    @Autowired
    private RestfulService sv;

    @Before
    @Override
    public void before() throws DatabaseUnitException, SQLException {
        super.before();
        this.sv.clear();
        this.sv.init();
    }

//    @Test
    public void test_doGet() {
        TestReturn ret = doGet("/std/contracts/");
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("ok"));
        List<Contract> list = ret.getObject4List(Contract.class);
        assertThat(list.size(), is(3));
        assertThat(list.get(1).getCode(), is("m1609"));
    }

    @Test
    public void test_doGet1() {
        TestReturn ret = doGet("/std/contracts/3");
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("ok"));
        assertThat(ret.getBody(), not(nullValue()));
        Contract con = ret.getObject(Contract.class);
        assertThat(con.getId(), is("3"));
        assertThat(con.getCode(), is("ta1610"));
    }

    @Test
    public void test_doPost() {
        Contract con = new Contract();
        con.setCode("y1601");
        con.setName("豆油");
        Map map = new HashMap();
        map.put("json", new Gson().toJson(con));
        TestReturn ret = doPost("/std/contracts/", map);
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("创建成功"));
        assertThat(ret.getHeader("name"), is("yecq"));
        assertThat(ret.getHeader("depart"), is("ise"));
        String id = ret.getObject(String.class);
        assertThat(id, is("4"));
    }

    @Test
    public void test_put() {
        Map map = new HashMap();
        Contract con = new Contract();
        con.setMargin(0.06);
        map.put("json", con);
        TestReturn ret = doPut("/std/contracts/3", map);
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("修改成功"));
        assertThat(ret.getHeader("time"), is("2015-1-1"));
    }

    @Test
    public void test_delete() {
        TestReturn ret = doDelete("/std/contracts/2");
        assertThat(ret.getHeader("status"), is("ok"));
        assertThat(ret.getHeader("message"), is("删除成功"));
    }

    @Test
    public void test_delete_logged() {
        TestReturn ret = doDelete("/std/contracts/2/log");
        assertThat(ret.getHeader("status"), is("fail"));
        assertThat(ret.getHeader("message"), is("用户未登录"));
    }

    @Test
    public void test_delete_logged1() {
        TestReturn ret = doDelete("/std/contracts/2/log1");
        assertThat(ret.getHeader("status"), is("ok"));
    }

    @Test
    public void test_getContractDirect() {
        TestReturn ret = doGet("/std/contracts/1/other1");
        assertThat(ret.getHeader("status"), is("ok"));
        Contract con = ret.getObject(Contract.class);
        assertThat(con.getCode(), is("L1610"));
    }

    @Test
    public void test_getContractDirect2() {
        TestReturn ret = doGet("/std/contracts/1/other2");
        assertThat(ret.getHeader("status"), is("ok"));
        Contract con = ret.getObject(Contract.class);
        assertThat(con.getCode(), is("sr1507"));
        assertThat(ret.getHeader("exchange"), is("大连"));
    }

    @Test
    public void test_doGetWithParam() {
        TestReturn ret = doGet("/std/contracts?cond1=id>2&cond2=name=1234");
        assertThat(ret.getHeader("status"), is("ok"));
    }

    @Test
    public void test_doGetWithParam1() {
        Map map = new HashMap();
        map.put("cond1", "id>1000");
        map.put("cond2", "name!=yecq");
        TestReturn ret = doGet("/std/contracts", map);
        assertThat(ret.getHeader("status"), is("ok"));
    }

    @Test
    public void test_doGetWithParam2() {
        // 混合url和param方式
        Map map = new HashMap();
        map.put("cond1", "id<3");
        TestReturn ret = doGet("/std/contracts?cond2=number<>1998", map);
        assertThat(ret.getHeader("status"), is("ok"));
    }
    
}
