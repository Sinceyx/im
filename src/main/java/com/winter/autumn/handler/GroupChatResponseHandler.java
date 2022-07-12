package com.winter.autumn.handler;

import com.winter.autumn.message.GroupChatResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuangShk
 */
@Slf4j
public class GroupChatResponseHandler extends SimpleChannelInboundHandler<GroupChatResponseMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatResponseMessage msg) throws Exception {

        log.debug( "收到来自 {} 在群聊中 {} 发送的消息 ： {}", msg.getFrom(), msg.getGroupName(), msg.getContent() );

    }
}
