package com.tangym.tangram.component;

import java.util.Map;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 *
 * 组件的接口定义
 */

public interface ExeComponent {

    default void preExecute() {
    }

    Map<String, ?> execute(Map<String, ?> map);
}
