package com.jhhc.baseframework.web.controllersample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 返回的是body里的json
 *
 * @author yecq
 */
//@Controller
public class JsonController {

    @RequestMapping("a.go")
    @ResponseBody
    public String getJson(@RequestParam("json") String json) {
        System.out.println("发来了 " + json);
        return json + " world!";
    }
}
