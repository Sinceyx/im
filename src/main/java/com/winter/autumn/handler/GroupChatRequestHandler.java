package com.winter.autumn.handler;

import com.winter.autumn.message.GroupChatRequestMessage;
import com.winter.autumn.message.GroupChatResponseMessage;
import com.winter.autumn.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author HuangShk
 * @date 2022/3/8 15:18
 */
@Slf4j
public class GroupChatRequestHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
	@Override
	protected void channelRead0 ( ChannelHandlerContext ctx, GroupChatRequestMessage msg ) throws Exception {
		log.debug( "收到来自 {} 发送往群聊 {} 的消息 {}", msg.getFrom(),msg.getGroupName(), msg.getContent() );
		String groupName = msg.getGroupName();
		List<Channel> membersChannel = GroupSessionFactory.getGroupSession().getMembersChannel( groupName );
		membersChannel.remove( ctx.channel() );
		for ( Channel channel : membersChannel ) {
			channel.writeAndFlush( new GroupChatResponseMessage( msg.getFrom(), msg.getContent() ,groupName) );
		}

	}
}
