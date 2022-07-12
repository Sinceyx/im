package com.winter.autumn.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HuangShk
 * @date 2022/5/11 11:25
 */
public class SequenceIdUtil {

	private static final AtomicInteger adder = new AtomicInteger();

	public static Integer nextSequenceId(){
		return adder.incrementAndGet();
	}

}
