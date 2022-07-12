package com.winter.autumn.message;

import lombok.Data;

/**
 * @author HuangShk
 * @date 2022/5/10 16:13
 */
@Data
public class RpcRequestMessage  extends Message{
	private final  String interfaceName;

	private final String methodName;

	private final  Class<?>[] parameterTypes;

	private final  Object[] args;

	private final  Class<?> returnType;

	@Override
	public int getMessageType ( ) {
		return Message.RPC_REQUEST_MESSAGE;
	}
}
