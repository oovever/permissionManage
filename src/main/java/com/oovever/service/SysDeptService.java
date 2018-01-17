package com.oovever.service;

import com.google.common.base.Preconditions;
import com.oovever.common.RequestHolder;
import com.oovever.dao.SysDeptMapper;
import com.oovever.dao.SysUserMapper;
import com.oovever.exception.ParamException;
import com.oovever.model.SysDept;
import com.oovever.param.DeptParam;
import com.oovever.util.BeanValidator;
import com.oovever.util.IpUtil;
import com.oovever.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 部门管理的Service
 * @author OovEver
 * 2018/1/13 23:46
 */
@Service
public class SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;
    /**
     * 保存部门
     * @param param 部门参数
     */
    public void save(DeptParam param) {
//        验证部门参数信息
        BeanValidator.validate(param);
        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept dept = SysDept.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
//        操作时间
        dept.setOperateTime(new Date());
//        保存方法
        sysDeptMapper.insertSelective(dept);
        sysLogService.saveDeptLog(null, dept);
    }

    /**
     * 根据id返回部门
     * @param deptId 部门id
     * @return 返回部门信息
     */
    private String getLevel(Integer deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        }
        return dept.getLevel();
    }
    /**
     *  检查当前部门是否重复
     * @param parentId 上一级部门Id
     * @param deptName 部门名称
     * @param deptId 部门id
     * @return 是否重复
     */
    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
//        TODO
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    /**
     * 部门更新操作
     * @param param 更新参数
     */
    public void update(DeptParam param) {
        BeanValidator.check(param);
        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
//        更新的部门是否存在
        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");
        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
//更新之后的部门
        SysDept after = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        //        TODO 操作者
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
//        TODO 操作者IP
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
//        操作时间
        after.setOperateTime(new Date());

        updateWithChild(before, after);
//        sysLogService.saveDeptLog(before, after);
        sysLogService.saveDeptLog(before, after);
    }

    /**
     * 更新部门
     * @param before 之前的部门
     * @param after 更新之后的部门
     */
    @Transactional
    void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
//        更新之后的部门与更新之前的部门不在一个层级， 需要更新其子部门
        if (!after.getLevel().equals(before.getLevel())) {
//            取出当前部门的子部门
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }
    public void delete(int deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在，无法删除");
        if (sysDeptMapper.countByParentId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有子部门，无法删除");
        }
        if(sysUserMapper.countByDeptId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有用户，无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
    }
}
