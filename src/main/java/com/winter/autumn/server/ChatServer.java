package com.winter.autumn.server;


import com.winter.autumn.handler.*;
import com.winter.autumn.protocol.MessageCodecSharable;
import com.winter.autumn.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {


    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_HANDLER = new MessageCodecSharable();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ch.pipeline()
                            .addLast(new ProtocolFrameDecoder())
                            .addLast(LOGGING_HANDLER)
                            .addLast(MESSAGE_HANDLER)
//                            .addLast(new IdleStateHandler( 5000,0,0, TimeUnit.MILLISECONDS ) )
                            .addLast( new ChannelDuplexHandler(){
                                @Override
                                public void userEventTriggered ( ChannelHandlerContext ctx, Object evt ) throws Exception {
                                    log.warn( "已经5秒没有收到来自 {} 的数据了",ctx.channel());
                                    super.userEventTriggered( ctx, evt );
                                }
                            } )
                            .addLast(new LoginRequestHandler())
                            .addLast(new ChatRequestHandler ())
                            .addLast ( new GroupCreateRequestHandler() )
                            .addLast ( new GroupChatRequestHandler() )
                            .addLast(new ExceptionHandler ())
                    ;

                }
            });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();


        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
