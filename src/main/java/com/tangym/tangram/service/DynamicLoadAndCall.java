package com.tangym.tangram.service;

import com.tangym.tangram.dto.ComponentDTO;
import com.tangym.tangram.dto.NamedParam;
import com.tangym.tangram.mapper.BizClassesMapper;
import com.tangym.tangram.util.DiskClassLoader;
import com.tangym.tangram.util.FileUtil;
import com.tangym.tangram.util.SpringBootBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 * <p>
 * 代码组件的动态加载调用
 */

@Slf4j
@Component
public class DynamicLoadAndCall {
    @Autowired
    ConfigurableBeanFactory bf;

    @Resource
    private BizClassesMapper bizClassesMapper;

    private final Map<Integer, DiskClassLoader> loaders = new HashMap<>();

    public String process(ComponentDTO component, List<NamedParam> sceneParams, List<ComponentDTO> flowData) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String code = component.getCode();
        String res = "";
        if (FileUtil.validateCodeComponent(code)) {
            String codeComponentClassName = FileUtil.getCodeComponentClassName(code);
            String clzFullName = FileUtil.getComponentClassFullName(code);
            HashMap<String, String> paths = FileUtil.createTempJavaFileWithClassName(component.getBizId(), codeComponentClassName, code);
            int flag = compile(paths.get("file"));
            if (flag == 0) {
                init(paths.get("dir"), component.getBizId());
                res = exec(component.getBizId(), clzFullName);
            }
        }
        return res;
    }

    /**
     * 编译java文件
     */
    private int compile(String path) {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        int result = javaCompiler.run(null, null, null, path);
        return result;
    }

    /**
     * 加载注册初始化
     */
    private void init(String path, Integer bizId) throws ClassNotFoundException {
        ApplicationContext applicationContext = SpringBootBeanUtil.getApplicationContext();
        DiskClassLoader diskLoader;

        diskLoader = new DiskClassLoader(path);
        loaders.put(bizId, diskLoader);
        List<String> names = bizClassesMapper.selectByBizId(bizId);
        for (String clzFullName:names) {
            Class<?> clz = diskLoader.loadClass(clzFullName);
            AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
            Object bean = beanFactory.createBean(clz);
            String beanName = clz.getSimpleName() + "#" + System.identityHashCode(bean);
            beanFactory.initializeBean(bean, beanName);
            bf.registerSingleton(beanName, bean);
        }
    }

    /**
     * 执行调用
     */
    private String exec(Integer bizId, String clzFullName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DiskClassLoader loader = loaders.get(bizId);
        if (null != loader) {
            Class<?> clz = loader.loadClass(clzFullName);
            Method preExecute = clz.getDeclaredMethod("preExecute", null);
            Method execute = clz.getDeclaredMethod("execute", Map.class);
            preExecute.invoke(bf.getBean(clz), null);
            Object res = execute.invoke(bf.getBean(clz), new HashMap<>());
            if (null != res) return res.toString();
        }
        return "";
    }
}
