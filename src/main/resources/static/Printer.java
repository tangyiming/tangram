package com.ymm.qa.tangram.service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author :  唐一鸣
 * @url :  https://github.com/tangyiming
 */

public class Printer {
    public void print1() throws InterruptedException {
        int i = 0;
        while(i<30){
            System.out.printf("a:"+i++);
			print2();
            Thread.sleep(1000);
        }
    }
	
	public void print2() throws InterruptedException {
        System.out.printf("\n");
    }
}
