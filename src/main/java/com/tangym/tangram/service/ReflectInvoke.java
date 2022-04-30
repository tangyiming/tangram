package com.tangym.tangram.service;

import com.alibaba.fastjson.JSONObject;
import com.tangym.tangram.dto.ComponentDTO;
import com.tangym.tangram.dto.NamedParam;
import com.tangym.tangram.util.SpringBootBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 * <p>
 * 本地java组件的反射调用逻辑
 */

@Slf4j
@Component
public class ReflectInvoke {

    public String javaComponentInvoke(ComponentDTO component, List<NamedParam> sceneParams, List<ComponentDTO> flowData) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String res = null;
        String className = component.getClassName();
        Class<?> clz = this.getClass().getClassLoader().loadClass("com.tangym.tangram.component." + className);
        Method method = clz.getDeclaredMethod("execute", Map.class);

        List<NamedParam> namedParam = component.getParams();

        Map<String, Object> m = new HashMap<String, Object>();
        namedParam.forEach(p -> {
            if (p.getMapping() != null) {
                String mapping = p.getMapping();
                String[] split = mapping.split(":");
                String inx = split[0];
                String key = split[1];
                if (inx.equals("SP")) {
                    String mapv = "";
                    for (NamedParam sp : sceneParams) {
                        if (sp.getKey().equals(key)) {
                            mapv = sp.getValue().toString();
                        }
                    }
                    m.put(p.getKey(), mapv);
                } else {
                    List<NamedParam> output = flowData.get(Integer.parseInt(inx)).getOutput();
                    String mapv = "";
                    for (NamedParam out : output) {
                        if (out.getKey().equals(key)) {
                            mapv = out.getValue().toString();
                        }
                        m.put(p.getKey(), mapv);
                    }
                }
            } else {
                m.put(p.getKey(), p.getValue());
            }

        });
        Map ma = new HashMap<>();
        ma = JSONObject.parseObject(JSONObject.toJSONString(m), ma.getClass());
        ApplicationContext applicationContext = SpringBootBeanUtil.getApplicationContext();
        res = method.invoke(applicationContext.getBean(clz), ma).toString();
        return res;
    }
}
