package com.tangym.tangram.util;

import com.tangym.tangram.entity.BizClasses;
import com.tangym.tangram.mapper.BizClassesMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class FileUtilTest {
    @Resource
    private BizClassesMapper bizClassesMapper;

    @Test
    void file() {
        BizClasses bizClass = bizClassesMapper.selectByComponentId(6);
    }

}
