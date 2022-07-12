package com.winter.autumn.handler;

import com.winter.autumn.message.ChatResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * @author HuangShk
 */
public class ChatResponseHandler extends SimpleChannelInboundHandler<ChatResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatResponseMessage msg) throws Exception {
        if ( msg.isSuccess () ) {
            log.debug("收到来自{}的消息， 他说：{}", msg.getFrom(), msg.getContent());
        }else{
            log.debug("{}", msg);
        }
    }
}
