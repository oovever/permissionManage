package com.oovever.dao;

import com.oovever.beans.PageQuery;
import com.oovever.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
//    查找客户电话与email是否存在
    SysUser findByKeyword(@Param("keyword") String keyword);
//    检查email是否存在
    int countByMail(@Param("mail") String mail, @Param("id") Integer id);
//检查电话是否存在
    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);
    int countByDeptId(@Param("deptId") int deptId);

    List<SysUser> getPageByDeptId(@Param("deptId") int deptId, @Param("page") PageQuery page);
}