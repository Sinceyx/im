package com.winter.autumn.handler;

import com.winter.autumn.message.GroupCreateResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuangShk
 * @date 2022/3/8 15:14
 */
@Slf4j
public class GroupCreateResponseHandler extends SimpleChannelInboundHandler<GroupCreateResponseMessage> {
	@Override
	protected void channelRead0 ( ChannelHandlerContext channelHandlerContext, GroupCreateResponseMessage msg ) throws Exception {
		log.debug( "群聊创建结果:{}" , msg.getReason());
	}
}
