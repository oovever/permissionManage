package com.oovever.service;


import com.oovever.common.RequestHolder;
import com.oovever.dao.SysAclModuleMapper;
import com.oovever.exception.ParamException;
import com.oovever.model.SysAclModule;
import com.oovever.param.AclModuleParam;
import com.oovever.util.BeanValidator;
import com.oovever.util.IpUtil;
import com.oovever.util.LevelUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @author OovEver
 * 2018/1/16 20:29
 */
@Service
public class SysAclModuleService {
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    /**
     * 保存权限模块
     * @param param 权限模块参数
     */
    public void save(AclModuleParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
//        组装权限模块
        SysAclModule aclModule = SysAclModule.builder().name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
                .status(param.getStatus()).remark(param.getRemark()).build();
        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());
        sysAclModuleMapper.insertSelective(aclModule);
    }

    /**
     * 同一级下 不应出现相同权限模块
     * @param parentId 父模块id
     * @param aclModuleName 权限模块名称
     * @param deptId 权限Id
     * @return 是否在同一级下有相同模块名称
     */
    private boolean checkExist(Integer parentId, String aclModuleName, Integer deptId) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, deptId) > 0;

    }

    /**
     * 获取权限模块层级
     * @param aclModuleId 权限模块id
     * @return 返回权限模块层级
     */
    private String getLevel(Integer aclModuleId) {
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();
    }
}
