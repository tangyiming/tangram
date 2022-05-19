package com.tangym.tangram.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BizClasses implements Serializable {

    private Integer id;

    private Integer bizId;

    private Integer componentId;

    private String clzFullName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private static final long serialVersionUID = 260695760237900596L;
}
