package com.oovever.controller;

import com.oovever.common.JsonData;
import com.oovever.param.RoleParam;
import com.oovever.service.*;
import com.oovever.util.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OovEver
 * 2018/1/17 0:24
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {
    @Resource
    private SysRoleService     sysRoleService;
    @Resource
    private SysTreeService     sysTreeService;
    @Resource
    private SysRoleAclService  sysRoleAclService;
    @Resource
    private SysRoleUserService sysRoleUserService;
    @Resource
    private SysUserService     sysUserService;
    /**
     *
     * @return 返回角色管理界面
     */
    @RequestMapping("role.page")
    public ModelAndView page() {
        return new ModelAndView("role");
    }
    @RequestMapping("/save.json")
    @ResponseBody
    /**
     * 角色保存
     */
    public JsonData saveRole(RoleParam param) {
        sysRoleService.save(param);
        return JsonData.success();
    }

    /**
     * 角色更新
     * @param param 参数
     * @return 返回更新结果
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateRole(RoleParam param) {
        sysRoleService.update(param);
        return JsonData.success();
    }
    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list() {
        return JsonData.success(sysRoleService.getAll());
    }

    /**
     *
     * @param roleId 角色id
     * @return 返回角色树
     */
    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") int roleId) {
        return JsonData.success(sysTreeService.roleTree(roleId));
    }
    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonData changeAcls(@RequestParam("roleId") int roleId, @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);
        return JsonData.success();
    }
}
