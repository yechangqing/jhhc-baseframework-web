package com.jhhc.baseframework.web.controller.restful;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author yecq
 */
@Controller
public class JspController1 {

    @RequestMapping("/jsp10")
    public String doJsp10(HttpServletRequest request) {
        return "abcd";
    }

    @RequestMapping("/function/{id}/branch/{mid}")
    public ModelAndView getJsp1000(HttpServletRequest request, @PathVariable("id") String id, @PathVariable("mid") String mid, @RequestParam("name") String name) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("zxdf");
        mv.addObject("name", name);
        return mv;
    }
}
