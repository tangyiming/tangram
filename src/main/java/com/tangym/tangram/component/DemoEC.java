package com.tangym.tangram.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 * <p>
 * 一个简单的纯java组件demo
 */

@Slf4j
@Component
public class DemoEC implements ExeComponent {
    @Override
    public void preExecute() {
        ExeComponent.super.preExecute();
    }

    @Override
    public Map<String, ?> execute(Map<String, ?> map) {
        Object p = map.get("lang");
        log.info(p.toString()); // 3
        Map<String, Object> res = new HashMap<>();
        res.put("jvm", "jvm");
        return res;
    }
}
