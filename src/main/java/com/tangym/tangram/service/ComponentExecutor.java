package com.tangym.tangram.service;

import com.tangym.tangram.entity.DfComponent;
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

    public String executeHttpComp(DfComponent component, List<DfComponent> scene) {
        try {
            return httpRequest.doPost(component, scene);
        } catch (Exception e) {
            log.error("http接口执行异常；{}", e.getMessage());
        }
        return null;
    }

    public String exceteJavaComp(DfComponent component, List<DfComponent> scene) {
        try {
            return reflectInvoke.javaComponentInvoke(component, scene);
        } catch (Exception e) {
            log.error("http接口执行异常；{}", e.getMessage());
        }
        return null;
    }
}
