package com.tangym.tangram.controller;

import com.tangym.tangram.util.DiskClassLoader;
import com.tangym.tangram.util.SpringBootBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 *
 * 运行时重载外部代码文件调用分步演示接口，曾测试验证 也适用于通过注解声明的Service Mesh的rpc接口
 */

@Slf4j
@RestController
@RequestMapping("/reload/class/test")
public class ReloadClassTestController {

    @Autowired
    ConfigurableBeanFactory bf;
    DiskClassLoader diskLoader;

    @GetMapping("/compile")
    public void compile() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        int result = javaCompiler.run(null, null, null, "D:\\lib\\Printer.java");
    }

    @GetMapping("/print")
    public void print() throws InterruptedException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DiskClassLoader diskLoader = new DiskClassLoader("D:\\lib");
        Class<?> c = diskLoader.loadClass("com.ymm.qa.tangram.service.Printer");
        if (c != null) {
            try {
                Object obj = c.newInstance();
                Method method = c.getDeclaredMethod("print1", null);
                //通过反射调用Test类的say方法
                method.invoke(obj, null);
            } catch (InstantiationException | IllegalAccessException
                    | NoSuchMethodException
                    | SecurityException |
                    IllegalArgumentException |
                    InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/compileComp")
    public String compileComp() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        int result = javaCompiler.run(null, null, null, "D:\\lib\\TestRpcComp.java");
        if (result == 0) {
            return "编译成功";
        }
        return "编译失败";
    }

    @GetMapping("/load/comp")
    public void load(@RequestParam String name) throws ClassNotFoundException {
        ApplicationContext applicationContext = SpringBootBeanUtil.getApplicationContext();
        diskLoader = new DiskClassLoader("D:\\lib");
        Class<?> clz = diskLoader.loadClass(name);
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        Object bean = beanFactory.createBean(clz);
        String beanName = clz.getSimpleName() + "#" + System.identityHashCode(bean);
        beanFactory.initializeBean(bean, beanName);
        bf.registerSingleton(beanName, bean);
    }

    @GetMapping("/exec/comp")
    public void exec(@RequestParam String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Class<?> clz = diskLoader.loadClass(name);
        Method method = clz.getDeclaredMethod("execute", Map.class);
        method.invoke(bf.getBean(clz), new HashMap<>());
    }
}
