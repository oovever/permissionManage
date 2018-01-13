package com.oovever.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 部门层级工具类
 * @author OovEver
 * 2018/1/14 0:18
 */
public class LevelUtil {
    public final static String SEPARATOR = ".";
//root id
    public final static String ROOT = "0";
    // 0
    // 0.1
    // 0.1.2
    // 0.1.3
    // 0.4

    /**
     * 计算层级
     * @param parentLevel 父层
     * @param parentId 父id
     * @return 当前层级
     */
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
