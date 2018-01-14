package com.oovever.controller;

import com.oovever.common.JsonData;
import com.oovever.param.UserParam;
import com.oovever.service.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author OovEver
 * 2018/1/14 23:45
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;

    /**
     * 保存用户操作
     * @param param 用户参数
     * @return 返回保存结果
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(UserParam param) {
        sysUserService.save(param);
        return JsonData.success();
    }
    /**
     * 更新用户操作
     * @param param 用户参数
     * @return 返回更新结果
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(UserParam param) {
        sysUserService.update(param);
        return JsonData.success();
    }
}
