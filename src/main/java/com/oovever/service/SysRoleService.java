package com.oovever.service;

import com.google.common.base.Preconditions;
import com.oovever.common.RequestHolder;
import com.oovever.dao.SysRoleMapper;
import com.oovever.exception.ParamException;
import com.oovever.model.SysRole;
import com.oovever.param.RoleParam;
import com.oovever.util.BeanValidator;
import com.oovever.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author OovEver
 * 2018/1/17 0:25
 */
@Service
public class SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * 添加角色
     * @param param 角色参数
     */
    public void save(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        SysRole role = SysRole.builder().name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());
        sysRoleMapper.insertSelective(role);
    }

    /**
     * 更新角色
     * @param param 参数
     */
    public void update(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        SysRole after = SysRole.builder().id(param.getId()).name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKeySelective(after);
    }

    /**
     *
     * @return 返回所有角色
     */
    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }
    /**
     * 角色是否存在
     * @param name 角色名称
     * @param id 角色id
     * @return 返回角色是否存在
     */
    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }
}
