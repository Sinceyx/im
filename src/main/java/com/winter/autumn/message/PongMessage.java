package com.winter.autumn.message;

/**
 * @author HuangShk
 * @date 2022/3/11 11:20
 */
public class PongMessage extends Message  {
	@Override
	public int getMessageType ( ) {
		return PONG_MESSAGE;
	}
}
