package com.tangym.tangram.util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Slf4j
public class FileUtil {
    public static HashMap<String,String> createTempJavaFileWithClassName(Integer bizId, String name, String code) throws IOException {
        File directory = new File(".");
        String dirPath = null;
        try {
            dirPath = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dirPath = dirPath + File.separator + "TempClasses" + File.separator + bizId;
        File file = new File(dirPath);
        if (!file.exists() && !file.isDirectory()) {
            boolean mkdir = file.mkdirs();
            if (!mkdir) log.error("文件夹创建失败");
        }
        File temp = new File(dirPath + File.separator + name + ".java");
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
        out.write(code);
        out.close();
        HashMap<String,String> res = new HashMap<>();
        if (temp.exists()) {
            res.put("dir",dirPath);
            res.put("file", temp.getAbsolutePath());
        } else {
            log.error("文件创建失败");

        }
        return res;
    }

    /**
     * 简单校验下组件是否实现了接口ExeComponent 且是否实现了这两个方法，实现了则认为组件符合规范校验通过
     * 如有其他约束，也可以增加其他校验
     */
    public static boolean validateCodeComponent(String code) {
        JavaParser jp = new JavaParser();
        Optional<CompilationUnit> result = jp.parse(code).getResult();
        ClassOrInterfaceDeclaration declaration = (ClassOrInterfaceDeclaration) result.get().getTypes().getFirst().get();
        NodeList<ClassOrInterfaceType> implementedTypes = declaration.getImplementedTypes();
        if (implementedTypes.size() == 1 && implementedTypes.get(0).getName().toString().equals("ExeComponent")) {
            NodeList<BodyDeclaration<?>> members = result.get().getTypes().getFirst().get().getMembers();
            Set<String> rightNames = new HashSet<>();
            rightNames.add("preExecute");
            rightNames.add("execute");
            Set<String> names = new HashSet<>();
            for (BodyDeclaration<?> member : members) {
                MethodDeclaration bodyDeclaration = (MethodDeclaration) member;
                String name = bodyDeclaration.getName().toString();
                names.add(name);
            }
            return names.containsAll(rightNames);
        }
        return false;
    }

    /**
     * 获取类名
     */
    public static String getCodeComponentClassName(String code) {
        JavaParser jp = new JavaParser();
        Optional<CompilationUnit> result = jp.parse(code).getResult();
        return result.get().getTypes().getFirst().get().getName().toString();
    }

    public static String getComponentClassFullName(String code) {
        JavaParser jp = new JavaParser();
        Optional<CompilationUnit> result = jp.parse(code).getResult();
        return result.get().getTypes().getFirst().get().getFullyQualifiedName().get();
    }
}
