package com.tangym.tangram.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 * <p>
 * 组件实体
 */

@Data
public class DfComponent implements Serializable {
    private static final long serialVersionUID = -6893938350709398690L;
    private Integer id;
    private String compName;
    private String compDesc;
    private Integer compType;
    private String params;
    private String output;
    private String urlpath;
    private String className;
    private String code;
    private Integer bizId;
    private Integer compStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createdBy;
    private DfBizLine bizLine;
}
