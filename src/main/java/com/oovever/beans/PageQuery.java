package com.oovever.beans;



import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @author OovEver
 * 2018/1/15 23:29
 */
public class PageQuery {
//    page编号
    @Getter
    @Setter
    @Min(value = 1, message = "当前页码不合法")
    private int pageNo = 1;
//每页展示数量
    @Getter
    @Setter
    @Min(value = 1, message = "每页展示数量不合法")
    private int pageSize = 10;
//    偏移量
    @Setter
    private int offset;

    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }
}
