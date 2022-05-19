package com.tangym.tangram.component;

import java.util.Map;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 */

public class Printer implements ExeComponent {
    @Override
    public void preExecute() {
        System.out.printf("preExecute\n");
    }

    @Override
    public Map<String, ?> execute(Map<String, ?> map) throws InterruptedException {
        int i = 0;
        while(i<30){
            System.out.printf("a:"+i++);
            System.out.printf("\n");
            Thread.sleep(1000);
        }
        return null;
    }
}
