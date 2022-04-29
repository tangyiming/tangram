package com.tangym.tangram.controller;

import com.alibaba.fastjson.JSONObject;
import com.tangym.tangram.dto.ApiResponse;
import com.tangym.tangram.dto.CmpSceneQuery;
import com.tangym.tangram.entity.DfComponent;
import com.tangym.tangram.mapper.DfComponentMapper;
import com.tangym.tangram.service.ComponentExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 *
 * 组件管理接口
 */

@Slf4j
@RestController
@RequestMapping("/component")
public class ComponentController {
    @Resource
    private DfComponentMapper dfComponentMapper;
    @Resource
    private ComponentExecutor componentExecutor;

    @PostMapping("/query")
    public ApiResponse<?> query(@RequestBody CmpSceneQuery query) {
        List<DfComponent> list = dfComponentMapper.queryBy(query);
        return ApiResponse.succResponse(list);
    }

    @PostMapping("/add")
    public ApiResponse<?> add(@RequestBody DfComponent dfComponent) {
        int insert = dfComponentMapper.insert(dfComponent);
        if (insert > 0) return ApiResponse.succResponse();
        return ApiResponse.failResponse(-1);
    }

    @PostMapping("/update")
    public ApiResponse<?> update(@RequestBody DfComponent dfComponent) {
        int i = dfComponentMapper.updateByPrimaryKey(dfComponent);
        if (i > 0) return ApiResponse.succResponse();
        return ApiResponse.failResponse(-1);
    }

    @PostMapping("/updateStatus")
    public ApiResponse<?> updateStatus(@RequestBody DfComponent dfComponent) {
        if (dfComponent.getCompStatus() == 0) {
            dfComponent.setCompStatus(1);
        } else {
            dfComponent.setCompStatus(0);
        }
        int i = dfComponentMapper.updateByPrimaryKey(dfComponent);
        if (i > 0) return ApiResponse.succResponse();
        return ApiResponse.failResponse(-1);
    }

    @PostMapping("/delete")
    public ApiResponse<?> delete(@RequestBody DfComponent dfComponent) {
        int i = dfComponentMapper.deleteByPrimaryKey(dfComponent.getId());
        if (i > 0) return ApiResponse.succResponse();
        return ApiResponse.failResponse(-1);
    }

    @PostMapping("/execute")
    public ApiResponse<?> execute(@RequestBody DfComponent dfComponent) {
        String res;
        if (dfComponent.getCompType() == 0) {
            res = componentExecutor.executeHttpComp(dfComponent, null);
        } else {
            res = componentExecutor.exceteJavaComp(dfComponent, null);
        }
        dfComponent.setOutput(res);
        return ApiResponse.succResponse(dfComponent);
    }
}
