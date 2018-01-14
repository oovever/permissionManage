package com.oovever.dto;

import com.google.common.collect.Lists;
import com.oovever.model.SysDept;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 部门层级dto
 * @author OovEver
 * 2018/1/14 15:00
 */
@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {
    private List<DeptLevelDto> deptList = Lists.newArrayList();

    /**
     * 返回前端需要各式的dto
     * @param dept 原部门队形
     * @return 返回转化后的前端需要的dto
     */
    public static DeptLevelDto adapt(SysDept dept) {
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }
}
