package com.jhhc.baseframework.web.controller.restful;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 测试下获取jsp
 *
 * @author yecq
 */
@Controller
@RequestMapping("/jsp")
public class JspController {

    @RequestMapping("")
    public String getJsp0(HttpServletRequest request, Model model) {
        model.addAttribute("time", "2016-2-10");
        return "abcde";
    }

    @RequestMapping("/{id}")
    public String getJsp00(HttpServletRequest request, @PathVariable("id") String id, Model model) {
        model.addAttribute("time", "2016-1-19");
        return "abcde";
    }

    @RequestMapping("/jsp1")
    public ModelAndView getJsp1(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("zxdf");
        mv.addObject("time", "2016-2-11");
        return mv;
    }

    @RequestMapping("/jsp1/{id}")
    public ModelAndView getJsp12(HttpServletRequest request, @PathVariable("id") String id, @RequestParam("name") String name) {
        ModelAndView mv = new ModelAndView("abcde");
        mv.addObject("time", "2016-4-21");
        return mv;
    }
}
