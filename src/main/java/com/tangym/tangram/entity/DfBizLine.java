package com.tangym.tangram.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 *
 * 业务线实体
 */

@Data
public class DfBizLine implements Serializable {
    private static final long serialVersionUID = -3086477991173803692L;
    private Integer id;
    private String bizName;
    private String bizCode;
    private String baseUrl;
    private String charger;
    private String authInfo;
}
