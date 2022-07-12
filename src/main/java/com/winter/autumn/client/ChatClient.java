package com.winter.autumn.client;

import com.winter.autumn.handler.*;
import com.winter.autumn.message.PingMessage;
import com.winter.autumn.protocol.MessageCodecSharable;
import com.winter.autumn.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author HuangShk
 */
@Slf4j
public class ChatClient {

    public static void main(String[] args) {
        NioEventLoopGroup WORKER = new NioEventLoopGroup(4);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        try {
            ChannelFuture channelFuture = new Bootstrap()
                    .group(WORKER)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtocolFrameDecoder())
                                    .addLast(new LoggingHandler(LogLevel.DEBUG))
                                    .addLast(new MessageCodecSharable())

//                                    .addLast( new IdleStateHandler( 0,3000,0, TimeUnit.MILLISECONDS ) )
                                    .addLast(new ChannelDuplexHandler(){
                                        @Override
                                        public void userEventTriggered ( ChannelHandlerContext ctx, Object evt ) throws Exception {
                                            log.warn( "客户端 {} 已经3秒没有写数据了，发一个心跳包", ctx.channel().id() );
                                            ctx.writeAndFlush( new PingMessage() );
                                            super.userEventTriggered( ctx, evt );
                                        }
                                    } )

//ssh-keygen -t rsa -C "huangshaoke@chinasofti.com" -b 4096
                                    .addLast(new CommandHandler(countDownLatch, atomicBoolean))
                                    .addLast(new LoginResponseHandler(atomicBoolean, countDownLatch))
                                    .addLast(new ChatResponseHandler())
                                    .addLast(new GroupChatResponseHandler() )
                                    .addLast(new GroupCreateResponseHandler() )
                                    .addLast(new ExceptionHandler())

                            ;
                        }
                    })
                    .connect(new InetSocketAddress("localhost", 8080));

            channelFuture.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            WORKER.shutdownGracefully();
        }

    }

}
