package com.tangym.tangram.service;

import com.tangym.tangram.dto.ComponentDTO;
import com.tangym.tangram.dto.NamedParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 * <p>
 * 组件执行方法
 */

@Slf4j
@Component
public class ComponentExecutor {
    @Resource
    private HttpRequest httpRequest;
    @Resource
    private ReflectInvoke reflectInvoke;
    @Resource
    private DynamicLoadAndCall dynamicLoadAndCall;

    public String executeHttpComp(ComponentDTO component, List<NamedParam> commonParams, List<ComponentDTO> flowData) {
        try {
            return httpRequest.doPost(component, commonParams, flowData);
        } catch (Exception e) {
            log.error("http接口执行异常；{}", e.getMessage());
        }
        return null;
    }

    public String exceteJavaComp(ComponentDTO component, List<NamedParam> commonParams, List<ComponentDTO> flowData) {
        try {
            return reflectInvoke.javaComponentInvoke(component, commonParams, flowData);
        } catch (Exception e) {
            log.error("java组件执行异常；{}", e.getMessage());
        }
        return null;
    }

    public String exceteCodeComp(ComponentDTO component,  List<NamedParam> commonParams, List<ComponentDTO> flowData) {
        try {
            return dynamicLoadAndCall.process(component, commonParams, flowData);
        } catch (Exception e) {
            log.error("code组件执行异常；{}", e.getMessage());
        }
        return null;
    }
}
