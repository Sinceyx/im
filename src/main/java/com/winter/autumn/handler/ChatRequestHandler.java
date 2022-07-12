package com.winter.autumn.handler;

import com.winter.autumn.message.ChatRequestMessage;
import com.winter.autumn.message.ChatResponseMessage;
import com.winter.autumn.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuangShk
 * @date 2022/3/8 11:23
 */
@Slf4j
public class ChatRequestHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
	@Override
	protected void channelRead0 ( ChannelHandlerContext ctx, ChatRequestMessage msg ) throws Exception {
		log.debug ( "接收到聊天消息, {}", msg );
		Channel channel = SessionFactory.getSession ().getChannel ( msg.getTo () );
		if ( channel != null ) {
			channel.writeAndFlush ( new ChatResponseMessage ( msg.getFrom (), msg.getContent () ) );
		} else {
			ctx.writeAndFlush ( new ChatResponseMessage ( false, "对方用户不在线" ) );
		}

	}
}
