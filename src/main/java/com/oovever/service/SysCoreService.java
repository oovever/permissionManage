package com.oovever.service;

import com.google.common.collect.Lists;
import com.oovever.common.RequestHolder;
import com.oovever.dao.SysAclMapper;
import com.oovever.dao.SysRoleAclMapper;
import com.oovever.dao.SysRoleUserMapper;
import com.oovever.model.SysAcl;
import com.oovever.model.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OovEver
 * 2018/1/17 13:40
 * 获取角色与用户权限的service
 */
@Service
public class SysCoreService {
    @Resource
    private SysAclMapper      sysAclMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysRoleAclMapper  sysRoleAclMapper;

    /**
     *
     * @return 当前用户包含的所有权限列表
     */
    public List<SysAcl> getCurrentUserAclList() {
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    /**
     * 当前指定角色权限已分配列表
     * @param roleId 角色id
     * @return 当前角色权限列表
     */
    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(aclIdList);
    }

    /**
     *
     * @param userId 用户id
     * @return 当前用户权限列表
     */
    public List<SysAcl> getUserAclList(int userId) {
        if (isSuperAdmin()) {
//            获取所有权限
            return sysAclMapper.getAll();
        }
//       根据userid查角色id列表
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }
//        根据角色列表查询权限点id列表
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }
//        根据权限点id列表查权限
        return sysAclMapper.getByIdList(userAclIdList);
    }
    /**
     *
     * @return 是否是超级管理员
     */
    public boolean isSuperAdmin() {
        // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser.getMail().contains("admin")) {
            return true;
        }
        return false;
    }
}
