package com.tangym.tangram.dto;

import com.tangym.tangram.entity.DfBizLine;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComponentDTO {
    private Integer id;
    private String compName;
    private String compDesc;
    private Integer compType;
    private List<NamedParam> params;
    private List<NamedParam> output;
    private String res;
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
