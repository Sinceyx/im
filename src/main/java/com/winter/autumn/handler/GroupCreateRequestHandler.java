package com.winter.autumn.handler;

import com.winter.autumn.message.GroupChatResponseMessage;
import com.winter.autumn.message.GroupCreateRequestMessage;
import com.winter.autumn.message.GroupCreateResponseMessage;
import com.winter.autumn.server.session.Group;
import com.winter.autumn.server.session.GroupSession;
import com.winter.autumn.server.session.GroupSessionFactory;
import com.winter.autumn.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

/**
 * @author HuangShk
 * @date 2022/3/8 14:06
 */
@Slf4j
public class GroupCreateRequestHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
	@Override
	protected void channelRead0 ( ChannelHandlerContext ctx, GroupCreateRequestMessage msg ) throws Exception {
		log.debug ( "{}", msg );

		String userName = (String) SessionFactory.getSession().getAttribute( ctx.channel(), "userName" );

		String groupName = msg.getGroupName ();
		//check groupName
		GroupSession groupSession = GroupSessionFactory.getGroupSession ();
		Group group = groupSession.createGroup ( groupName, msg.getMembers () );
		if ( group == null ) {
			Set<String> members = groupSession.getMembers( groupName );
			//group创建成功,获取member's channel
			List<Channel> membersChannel = groupSession.getMembersChannel( groupName );
			log.debug( membersChannel.toString() );
			for ( Channel channel : membersChannel ) {
				channel.writeAndFlush( new GroupChatResponseMessage( userName,userName + "邀请您加入了群聊 : " + groupName +"\r\n一起加入群聊的人还有:" + members.toString(),groupName) );
			}
			ctx.writeAndFlush( new GroupCreateResponseMessage( true, "群聊"+ groupName +"创建成功!" ) );
		}else{
			ctx.writeAndFlush( new GroupCreateResponseMessage( false, "群聊"+ groupName +"已经存在，创建群聊失败!" ) );
		}
	}
}
