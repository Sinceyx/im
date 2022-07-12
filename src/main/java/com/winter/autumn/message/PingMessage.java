package com.winter.autumn.message;

/**
 * @author HuangShk
 * @date 2022/3/11 11:19
 */
public class PingMessage extends Message{
	@Override
	public int getMessageType ( ) {
		return PING_MESSAGE;
	}
}
