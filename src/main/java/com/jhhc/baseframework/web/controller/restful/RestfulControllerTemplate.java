package com.jhhc.baseframework.web.controller.restful;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController风格的模板，作为Spring Bean进行配置，期望以后就不用 专门写方法了
 *
 * @author yecq
 */
//@RestController
//@RequestMapping("/resources")
public abstract class RestfulControllerTemplate extends RestfulControllerBase {

//    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public Object gets(HttpServletRequest request) {
        Map<String, String> param = getParamMap(request);
        return null;
    }

//    @RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
    public Object get(@PathVariable("id") String id, HttpServletRequest request) {
        return null;
    }

//    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public Object add(HttpServletRequest request) {
        Map<String, String> param = getParamMap(request);
        return null;
    }

//    @RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
    public Object modify(@PathVariable("id") String id, HttpServletRequest request) {
        Map<String, String> param = getParamMap(request);
        return null;
    }

//    @RequestMapping(value = {"/{id}"}, method = RequestMethod.DELETE)
    public Object remove(@PathVariable("id") String id, HttpServletRequest request) {
        return null;
    }
}
