package com.tangym.tangram.controller;

import com.alibaba.fastjson.JSONObject;
import com.tangym.tangram.dto.CmpSceneQuery;
import com.tangym.tangram.mapper.DfSceneMapper;
import com.tangym.tangram.dto.ApiResponse;
import com.tangym.tangram.entity.DfComponent;
import com.tangym.tangram.entity.DfScene;
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
 *
 * 测试场景管理接口
 */

@RequestMapping("/scene")
@RestController
@Slf4j
public class SceneController {
    @Resource
    private DfSceneMapper dfSceneMapper;
    @Resource
    private ComponentExecutor componentExecutor;

    @PostMapping("/add")
    public ApiResponse<?> add(@RequestBody DfScene dfScene){
        int i = dfSceneMapper.insert(dfScene);
        if(i>0) return ApiResponse.succResponse();
        return ApiResponse.failResponse(-1);
    }

    @PostMapping("/update")
    public ApiResponse<?> update(@RequestBody DfScene dfScene) {
        int i = dfSceneMapper.updateByPrimaryKey(dfScene);
        if (i > 0) return ApiResponse.succResponse();
        return ApiResponse.failResponse(-1);
    }

    @PostMapping("/updateStatus")
    public ApiResponse<?> updateStatus(@RequestBody DfScene dfScene) {
        if (dfScene.getSceneStatus() == 0) {
            dfScene.setSceneStatus(1);
        } else {
            dfScene.setSceneStatus(0);
        }
        int i = dfSceneMapper.updateByPrimaryKey(dfScene);
        if (i > 0) return ApiResponse.succResponse();
        return ApiResponse.failResponse(-1);
    }

    @PostMapping("/query")
    public ApiResponse<?> query(@RequestBody CmpSceneQuery cmpSceneQuery) {
        List<DfScene> list = dfSceneMapper.queryBy(cmpSceneQuery);
        return ApiResponse.succResponse(list);
    }

    @PostMapping("/delete")
    public ApiResponse<?> delete(@RequestBody DfScene dfScene) {
        int i = dfSceneMapper.deleteByPrimaryKey(dfScene.getId());
        if (i > 0) return ApiResponse.succResponse();
        return ApiResponse.failResponse(-1);
    }

    @PostMapping("/exec")
    public ApiResponse<?> exec(@RequestBody List<DfComponent> scene){
        scene.forEach(cmp->{
            String res;
            if (cmp.getCompType() == 0) {
                res = componentExecutor.executeHttpComp(cmp,scene);
            } else {
                res = componentExecutor.exceteJavaComp(cmp,scene);
            }
            cmp.setOutput(res);
        });
        return ApiResponse.succResponse(JSONObject.toJSONString(scene));
    }
}
