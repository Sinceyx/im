package com.winter.autumn.handler;

import com.winter.autumn.message.RpcRequestMessage;
import com.winter.autumn.message.RpcResponseMessage;
import com.winter.autumn.sevice.ServiceFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

/**
 * @author HuangShk
 * @date 2022/5/10 17:44
 */
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
	@Override
	protected void channelRead0 ( ChannelHandlerContext ctx, RpcRequestMessage msg ) throws Exception {
		RpcResponseMessage responseMessage = new RpcResponseMessage();
		responseMessage.setSequenceId( msg.getSequenceId() );
		Object service = ServiceFactory.getService( msg.getInterfaceName() );
		try {
			Method method = service.getClass().getMethod( msg.getMethodName(), msg.getParameterTypes() );
			responseMessage.setReturnValue( method.invoke( service, msg.getArgs() ));
		} catch ( Exception e ) {
			e.printStackTrace();
			responseMessage.setException( new Exception( "远程调用出错：" + e.getCause().getMessage() ) );
		}
		ctx.channel().writeAndFlush( responseMessage );
	}
}
