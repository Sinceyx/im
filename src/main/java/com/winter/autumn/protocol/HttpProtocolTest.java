package com.winter.autumn.protocol;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class HttpProtocolTest {

    /**
    public static void main(String[] args) {

        new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LoggingHandler(LogLevel.DEBUG))
                                .addLast(new HttpServerCodec())
                                .addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
                                        log.debug("收到请求，请求路径：{}", msg.uri());
                                        log.debug("ctx：{}", ctx.getClass());

                                        DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
                                        byte[] bytes = "<h1>Hello,world!</h1><h1>Hello,Everybody!</h1>".getBytes();

                                        fullHttpResponse.content().writeBytes(bytes);

                                        fullHttpResponse.headers().add(CONTENT_LENGTH, bytes.length);

                                        ctx.writeAndFlush(fullHttpResponse);

                                    }
                                });
                                /*.addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        log.debug("收到消息{}",msg.getClass());
                                        //POST 请求会携带请求头
                                        if (msg instanceof HttpContent) {


                                        //httpRequest类型，处理请求行 和  请求头
                                        } else if (msg instanceof HttpRequest) {

                                            HttpRequest httpRequest = (HttpRequest) msg;
                                            log.debug("Uri: {}",httpRequest.uri());

                                        }

                                        super.channelRead(ctx, msg);

                                    }
                                });
                    }
                })
                .bind(9999);


    }*/

}
