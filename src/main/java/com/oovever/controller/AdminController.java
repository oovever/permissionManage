package com.oovever.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author OovEver
 * 2018/1/15 23:00
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    /**
     * Admin登录页面
     * @return 返回要登录的页面
     */
    @RequestMapping("index.page")
    public ModelAndView index() {
        return new ModelAndView("admin");
    }
}
