package com.winter.autumn.handler;

import com.winter.autumn.message.LoginRequestMessage;
import com.winter.autumn.message.LoginResponseMessage;
import com.winter.autumn.server.service.UserServiceFactory;
import com.winter.autumn.server.session.SessionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * @author HuangShk
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        log.debug("接收到登录消息, {}", msg);
        boolean login = UserServiceFactory.getUserService().login(msg.getUsername(), msg.getPassword());
        LoginResponseMessage message = new LoginResponseMessage(login, login ? "登录成功" : "用户名或者密码错误");
        ctx.writeAndFlush(message);
        //登陆成功后，将用户channel存入session
        SessionFactory.getSession().bind(ctx.channel(), msg.getUsername());
        SessionFactory.getSession ().setAttribute(ctx.channel(),"userName", msg.getUsername());
    }
}
