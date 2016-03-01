package com.jhhc.baseframework.web.controller.restful;

import com.jhhc.baseframework.web.service.Sret;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设计更复杂，适合多种情况的Controller
 *
 * @author yecq
 */
@RestController
@RequestMapping("/contracts")
public class ComplexRestfulController extends RestfulControllerBase {

    // 一般的GET，附加条件放在参数里
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public Object gets(HttpServletRequest request) {        // 加入request之后，处理起来反而更加灵活
        Sret sr = new Sret();
        if (isNoParam(request)) {
            sr.setOk("全部数据");
        } else if (hasParam(request, "a")) {
            sr.setOk("参数a=" + request.getParameter("a"));
        } else if (hasParam(request, new String[]{"c", "d"})) {
            sr.setOk("c=" + request.getParameter("c") + ",d=" + request.getParameter("d"));
        } else {
            sr.setFail("参数不合法");
        }

        return sr;
    }

    // 获取单个元素GET，不需要其他条件
    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public Object get(@PathVariable("id") String id) {
        Sret sr = new Sret();
        sr.setOk("对象id=" + id);
        return sr;
    }

    // 增加元素POST
    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public Object add(HttpServletRequest request) {
        Map<String, String> map = getParamMap(request);
        // 送去添加
        Sret sr = new Sret();
        sr.setOk("添加属性" + map.size() + "个");
        return sr;
    }

    // 删除元素DELETE，无需参数
    @RequestMapping(value = {"/{id}"}, method = RequestMethod.DELETE)
    public Object remove(@PathVariable("id") String id) {
        Sret sr = new Sret();
        sr.setOk("删除元素id=" + id);
        return sr;
    }

    // 修改元素PUT，id写在url上，修改参数放在param中
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Object modify(@PathVariable("id") String id, HttpServletRequest request) {
        Map map = getParamMap(request);
        // 去修改
        Sret sr = new Sret();
        sr.setOk("修改元素id=" + id);
        return sr;
    }

    @RequestMapping(value = {"/decode"}, method = RequestMethod.GET)
    public Object decode(@RequestParam("json") String json, HttpServletRequest request) {
        Sret sr = new Sret();
        sr.setOk("收到参数json=" + json);
        return sr;
    }
}
