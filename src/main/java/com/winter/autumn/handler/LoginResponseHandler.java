package com.winter.autumn.handler;

import com.winter.autumn.message.LoginResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
@Slf4j
/**
 * @author HuangShk
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponseMessage> {
    private final AtomicBoolean atomicBoolean;
    private final CountDownLatch countDownLatch;

    public LoginResponseHandler(AtomicBoolean atomicBoolean, CountDownLatch countDownLatch) {
        this.atomicBoolean = atomicBoolean;
        this.countDownLatch = countDownLatch;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponseMessage msg) throws Exception {
        log.debug("接收到消息, {}", msg);
        atomicBoolean.set(msg.isSuccess());
        countDownLatch.countDown();
    }
}
