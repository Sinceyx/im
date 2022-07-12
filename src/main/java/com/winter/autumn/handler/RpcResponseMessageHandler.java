package com.winter.autumn.handler;

import com.winter.autumn.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HuangShk
 * @date 2022/5/11 11:15
 */
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {
	public static final Map<Integer, Promise<Object>> promises = new ConcurrentHashMap<>();


	@Override
	protected void channelRead0 ( ChannelHandlerContext ctx, RpcResponseMessage msg ) throws Exception {
		Promise<Object> promise = promises.remove( msg.getSequenceId() );
		if ( msg.getException() == null ) {
			promise.setSuccess( msg.getReturnValue() );
		}else {
			promise.setFailure( msg.getException() );
		}
	}
}
