package com.winter.autumn.handler;

import com.winter.autumn.server.session.SessionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * @author HuangShk
 */
public class ExceptionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("{}出现异常，异常信息 {}",ctx,cause);

        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("服务器{}断开连接",ctx.channel());
        SessionFactory.getSession().unbind( ctx.channel() );

        super.channelInactive(ctx);
    }
}
