package com.oovever.service;

import com.oovever.dao.SysDeptMapper;
import com.oovever.exception.ParamException;
import com.oovever.model.SysDept;
import com.oovever.param.DeptParam;
import com.oovever.util.BeanValidator;
import com.oovever.util.LevelUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 部门管理的Service
 * @author OovEver
 * 2018/1/13 23:46
 */
@Service
public class SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;

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
//        TODO 操作者
        dept.setOperator("system");
//        TODO 操作者IP
        dept.setOperateIp("127.0.0.1");
//        操作时间
        dept.setOperateTime(new Date());
//        保存方法
        sysDeptMapper.insertSelective(dept);
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
        return true;
    }
}
