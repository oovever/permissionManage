package com.oovever.service;

import com.google.common.collect.Lists;
import com.oovever.beans.CacheKeyConstants;
import com.oovever.common.RequestHolder;
import com.oovever.dao.SysAclMapper;
import com.oovever.dao.SysRoleAclMapper;
import com.oovever.dao.SysRoleUserMapper;
import com.oovever.model.SysAcl;
import com.oovever.model.SysUser;
import com.oovever.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.codehaus.jackson.type.TypeReference;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Resource
    private SysCacheService sysCacheService;
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
    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()) {
            return true;
        }
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }

        List<SysAcl> userAclList  = getCurrentUserAclListFromCache();
        Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());

        boolean hasValidAcl = false;
        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        for (SysAcl acl : aclList) {
            // 判断一个用户是否具有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1) { // 权限点无效
                continue;
            }
            hasValidAcl = true;
            if (userAclIdSet.contains(acl.getId())) {
                return true;
            }
        }
        if (!hasValidAcl) {
            return true;
        }
        return false;
    }
    public List<SysAcl> getCurrentUserAclListFromCache() {
        int userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {
            List<SysAcl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {
                sysCacheService.saveCache(JsonMapper.obj2String(aclList), 600, CacheKeyConstants.USER_ACLS, String.valueOf(userId));
            }
            return aclList;
        }
        return JsonMapper.string2Obj(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }

}
