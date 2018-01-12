package com.oovever.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author OovEver
 * 2018/1/12 15:38
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {
    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        log.info("hello");
        return "hello";
    }
}
