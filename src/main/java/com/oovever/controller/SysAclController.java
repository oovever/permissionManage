package com.oovever.controller;

import com.oovever.common.JsonData;
import com.oovever.param.AclModuleParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author OovEver
 * 2018/1/16 20:20
 * 权限点控制类
 */
@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {

    /**
     * acl页面
     */
    @RequestMapping("/acl.page")
    public ModelAndView page() {
        return new ModelAndView("acl");
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(AclModuleParam param) {
//        sysAclModuleService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclModuleParam param) {
//        sysAclModuleService.update(param);
        return JsonData.success();
    }
}
