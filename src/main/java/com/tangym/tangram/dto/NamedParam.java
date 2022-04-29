package com.tangym.tangram.dto;

import lombok.Data;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 * 组件参数对象
 */

@Data
public class NamedParam {
    private String key;
    private Object value;
    private Integer require;
    private String desc;
    private String mapping;
}
