package com.jhhc.baseframework.web.controller.restful;

import com.jhhc.baseframework.test.MakeLogged;
import com.jhhc.baseframework.web.controller.Logged;
import com.jhhc.baseframework.web.service.Sret;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author yecq
 */
@RestController
public class ComplexController1 {

    @RequestMapping(value = {"/accounts"}, method = RequestMethod.GET)
    @Logged
    public Object gets(HttpServletRequest request) {
        Sret sr = new Sret();
        sr.setOk("成功");
        return sr;
    }

    @RequestMapping(value = {"/accounts/{id}"}, method = RequestMethod.GET)
    @Logged
    @MakeLogged
    public Object get(HttpServletRequest req, @PathVariable("id") String id) {
        Sret sr = new Sret();
        sr.setOk("获得对象id=" + id);
        return sr;
    }
}
