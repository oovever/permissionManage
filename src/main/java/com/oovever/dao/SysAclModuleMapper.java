package com.oovever.dao;

import com.oovever.model.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);
//    根据名称和父id判断该权限是否存在
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);
//获取子层级列表
    List<SysAclModule> getChildAclModuleListByLevel(@Param("level") String level);
//批量更新层级
    void batchUpdateLevel(@Param("sysAclModuleList") List<SysAclModule> sysAclModuleList);
//    获取所有层级
    List<SysAclModule> getAllAclModule();

    int countByParentId(@Param("aclModuleId") int aclModuleId);
}