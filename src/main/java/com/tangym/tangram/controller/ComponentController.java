package com.tangym.tangram.controller;

import com.tangym.tangram.dto.ApiResponse;
import com.tangym.tangram.dto.CmpSceneQuery;
import com.tangym.tangram.dto.ComponentDTO;
import com.tangym.tangram.entity.DfComponent;
import com.tangym.tangram.mapper.DfComponentMapper;
import com.tangym.tangram.service.ComponentExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 * <p>
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
    public ApiResponse<?> execute(@RequestBody ComponentDTO component) {
        String res;
        if (component.getCompType() == 0) {
            res = componentExecutor.executeHttpComp(component, null, null);
        } else {
            res = componentExecutor.exceteJavaComp(component, null, null);
        }
        // JSONObject.parseObject(res,new TypeReference<List<NamedParam>>() {})
        component.setRes(res);
        return ApiResponse.succResponse(component);
    }
}
