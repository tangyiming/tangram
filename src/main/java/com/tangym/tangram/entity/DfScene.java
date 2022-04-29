package com.tangym.tangram.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 *
 * 场景实体
 */

@Data
public class DfScene implements Serializable {
    private static final long serialVersionUID = -7960228345698436449L;
    private Integer id;
    private String sceneName;
    private String sceneDesc;
    private String commonParams;
    private String flowData;
    private Integer sceneStatus;
    private Integer bizlineId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createdBy;
    private DfBizLine bizLine;
}
