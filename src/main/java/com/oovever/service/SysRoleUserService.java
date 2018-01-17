package com.oovever.service;

import com.google.common.collect.Lists;
import com.oovever.dao.SysLogMapper;
import com.oovever.dao.SysRoleUserMapper;
import com.oovever.dao.SysUserMapper;
import com.oovever.model.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OovEver
 * 2018/1/17 21:10
 */
@Service
public class SysRoleUserService {
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysUserMapper     sysUserMapper;
    @Resource
    private SysLogMapper      sysLogMapper;

    public List<SysUser> getListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

}
