package com.jhhc.baseframework.web.controllersample;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 返回jsp的controller
 *
 * @author yecq
 */
//@Controller
public class JspController {

    @RequestMapping({"home.go"})
    public String getHomePage(Model model) {
        System.out.println("home.go");
        Wife w = new Wife("蒋秋华", 29);
        model.addAttribute("wife", w);
        return "index11";
    }

    @RequestMapping("another.go")
    public ModelAndView getAnother() {
        System.out.println("another.go");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index11");
        Wife w = new Wife("本宝宝", 18);
        mv.addObject("wife", w);
        return mv;
    }
}
