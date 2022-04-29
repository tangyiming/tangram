package com.tangym.tangram.controller;

import com.tangym.tangram.dto.ApiResponse;
import com.tangym.tangram.entity.DfBizLine;
import com.tangym.tangram.mapper.DfBizLineMapper;
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
 * 业务线/团队 管理接口
 */

@Slf4j
@RestController
@RequestMapping("/biz")
public class BizLineController {
    @Resource
    private DfBizLineMapper dfBizLineMapper;

    @PostMapping("/add")
    public ApiResponse<?> add(@RequestBody DfBizLine dfBizLine) {
        int i = dfBizLineMapper.insert(dfBizLine);
        if (i > 0) return ApiResponse.succResponse();
        return ApiResponse.failResponse("fail");
    }

    @PostMapping("/list")
    public ApiResponse<?> list() {
        List<DfBizLine> dfBizLines = dfBizLineMapper.selectAll();
        return ApiResponse.succResponse(dfBizLines);
    }

    @PostMapping("/update")
    public ApiResponse<?> update(@RequestBody DfBizLine dfBizLine) {
        int i = dfBizLineMapper.updateByPrimaryKey(dfBizLine);
        if (i > 0) return ApiResponse.succResponse();
        return ApiResponse.failResponse(-1);
    }

    @PostMapping("/delete")
    public ApiResponse<?> delete(@RequestBody DfBizLine dfBizLine) {
        int i = dfBizLineMapper.deleteByPrimaryKey(dfBizLine.getId());
        if (i > 0) return ApiResponse.succResponse();
        return ApiResponse.failResponse(-1);
    }

}
