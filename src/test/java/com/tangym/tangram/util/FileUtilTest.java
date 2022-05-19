package com.tangym.tangram.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@SpringBootTest
@Slf4j
class FileUtilTest {

    @Test
    void file() {
        File directory = new File(".");
        String path = null;
        try {
            path = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        path = path + File.separator +"Test";
        File file = new File(path);
        //如果文件夹不存在则创建
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        } else {
            System.out.println("//目录存在");
        }
    }
}
