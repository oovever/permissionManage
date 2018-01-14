package com.oovever.dao;

import com.oovever.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);
//    返回所有部门列表
    List<SysDept> getAllDept();
//    获取当前部门下的子部门
    List<SysDept> getChildDeptListByLevel(@Param("level") String level);
//    批量更新Level
    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    //检查是否重复
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);
}