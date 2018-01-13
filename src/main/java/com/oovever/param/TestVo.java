package com.oovever.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author OovEver
 * 2018/1/13 22:48
 */
@Getter
@Setter
public class TestVo {
    @NotBlank
    private String  msg;
    @NotNull
    private Integer id;
}
