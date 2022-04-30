package com.tangym.tangram.dto;

import com.tangym.tangram.entity.DfBizLine;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SceneDTO {
    private Integer id;
    private String sceneName;
    private String sceneDesc;
    private List<NamedParam> commonParams;
    private List<ComponentDTO> flowData;
    private Integer sceneStatus;
    private Integer bizlineId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createdBy;
    private DfBizLine bizLine;
}
