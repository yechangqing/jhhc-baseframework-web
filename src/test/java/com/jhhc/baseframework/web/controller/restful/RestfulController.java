package com.jhhc.baseframework.web.controller.restful;

import com.google.gson.Gson;
import com.jhhc.baseframework.test.MakeLogged;
import com.jhhc.baseframework.web.controller.Logged;
import com.jhhc.baseframework.web.service.Sret;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 每一个映射方法，返回值为HttpEntity及其父类Object 返回的数据可以经由getRet()
 *
 * @author yecq
 */
@RestController
@RequestMapping("/std/contracts")
public class RestfulController extends RestfulControllerBase {

    @Autowired
    private RestfulService sv;

    // 获取所有资源，GET操作，如要加上额外条件，可见后面的方法
//    @RequestMapping(method = RequestMethod.GET)
    public Object doGet() {
        Sret sr = sv.getAllContract();
        return sr;
    }

    // 获取某个资源
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object doGet(@PathVariable("id") String id) {
        Sret sr = sv.getContract(id);
        return sr;
    }

    // 增加一个对象
    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity doPost(@RequestParam("json") String json) {
        Contract con = new Gson().fromJson(json, Contract.class);
        Sret sr = sv.add(con);
        // 加上其他属性
        Map param = new HashMap();
        param.put("name", "yecq");
        param.put("depart", "ise");
        return getSretEntityRet(sr, param);
    }

    // 修改一个对象
    @RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
    public ResponseEntity doPut(@PathVariable("id") String id, @RequestParam("json") String json) {
        Contract con = new Gson().fromJson(json, Contract.class);
        Sret sr = sv.modify(id, con);
        Map map = new HashMap();
        // 自己加点消息
        map.put("time", "2015-1-1");
        return getSretEntityRet(sr, map);
    }

    // 删除一个对象
    @RequestMapping(value = {"/{id}"}, method = RequestMethod.DELETE)
    public Object doDelete(@PathVariable("id") String id) {
        Sret sr = sv.remove(id);
        return sr;
    }

    // 删除一个对象，需要登录
    @RequestMapping(value = {"/{id}/log"}, method = RequestMethod.DELETE)
    @Logged
    public Object doDeleteLogged(@PathVariable("id") String id, HttpSession session) {
        Sret sr = sv.remove(id);
        return sr;
    }

    @RequestMapping(value = {"/{id}/log1"}, method = RequestMethod.DELETE)
    @Logged
    @MakeLogged
    public Object doDeleteLogged1(@PathVariable("id") String id, HttpSession session) {
        Sret sr = sv.remove(id);
        return sr;
    }

    // 也有不返回ResponseEntity，而是返回一般对象的方式
    @RequestMapping(value = "/{id}/other1", method = RequestMethod.GET)
    public Object getContractDirect(@PathVariable("id") String id) {
        Contract con = new Contract();
        con.setCode("L1610");
        return con;
    }

    @RequestMapping(value = "/{id}/other2", method = RequestMethod.GET)
    public Object getContractDirect2(@PathVariable("id") String id) {
        Contract con = new Contract();
        con.setCode("sr1507");
        Map param = new HashMap();
        param.put("exchange", "大连");
        return getObjectEntityRet(con, param);
    }

    /////////////////
    // GET 加上参数
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public Object doGetWithParam(@RequestParam("cond1") String cond1, @RequestParam("cond2") String cond2, HttpServletRequest req) {
        Sret sr = new Sret();
        return sr;
    }
}
