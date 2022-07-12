package com.winter.autumn.sevice;

/**
 * @author HuangShk
 * @date 2022/5/10 17:58
 */
public class HelloServiceImpl implements HelloService {
	@Override
	public String sayHello (String name ) {
		int i = 1 / 0;
		return "你好，"+ name +"！";
	}
}
