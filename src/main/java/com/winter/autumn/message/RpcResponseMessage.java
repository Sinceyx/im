package com.winter.autumn.message;

import lombok.Data;

/**
 * @author HuangShk
 * @date 2022/5/10 16:13
 */
@Data
public class RpcResponseMessage extends Message{
	private  Object returnValue;
	private  Exception  exception;

	@Override
	public int getMessageType ( ) {
		return RPC_RESPONSE_MESSAGE;
	}
}
