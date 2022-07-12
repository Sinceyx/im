package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HuangShk
 * @date 2022/4/11 16:02
 */
@Slf4j
public class TestClient {
	static final Random random = new Random();
	public static void main(String[] args){
//		generateClient();

		EventLoop next = new NioEventLoopGroup().next();
		next.execute( ()-> {
			System.out.println("hahahaahahah...........");
		} );


	}

	private static void generateClient ( ) {
		AtomicInteger at = new AtomicInteger();
		ThreadPoolExecutor executor = new ThreadPoolExecutor( 128, 512
				, 60, TimeUnit.SECONDS
				, new LinkedBlockingQueue<>( 10000 )
				, new DefaultThreadFactory( "nio-", true )
				, new ThreadPoolExecutor.CallerRunsPolicy()
		);

		for ( int i = 0; i < 5; i++ ) {
			executor.execute( ( ) -> newChannel( at ) );
		}

		while ( executor.getActiveCount() > 0 ) {
			Thread.yield();
		}
		System.out.println( "finally success count: " + at.get() );
	}

	private static void newChannel ( AtomicInteger at ) {
		NioEventLoopGroup WORKER = new NioEventLoopGroup(1);
		AtomicInteger atomicInteger = new AtomicInteger();
		try {
		ChannelFuture channelFuture = new Bootstrap()
				.group(WORKER)
				.channel( NioSocketChannel.class)
				.option(ChannelOption.SO_LINGER, 0)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
								.addLast(new ChannelInboundHandlerAdapter(){
									@Override
									public void channelActive ( ChannelHandlerContext ctx ) throws Exception {
										for ( int i = 0; i < 10; i++ ) {
											ByteBuf byteBuf = ctx.alloc().buffer();
											String s = "[XY*"+ String.format( "%015d", random.nextInt() ) +"*00CD*AL,180916,064153,A,22.570512,N,113.8623267,E,0.00,154.8,0.0,11,100,100,0,0,00100018,7,0,460,1,9529,21809,155,9529,21242,132,9529,21405,131,9529,63554,131,9529,63555,130,9529,63556,118,9529,21869,116,0,12.4]";
											byteBuf.writeBytes( s.getBytes( StandardCharsets.UTF_8 ) );
											ctx.channel().writeAndFlush( byteBuf );
										}
										super.channelActive( ctx );
									}

									@Override
									public void exceptionCaught ( ChannelHandlerContext ctx, Throwable e ) throws Exception {
										System.err.println( "异常: {}"+ e.getMessage() );
										e.printStackTrace();
									}

									@Override
									public void channelReadComplete ( ChannelHandlerContext ctx ) throws Exception {
										int i = atomicInteger.incrementAndGet();
										System.out.println( "downLatch.getCount() = " + i );
										if ( i == 10 ) {
											ctx.channel().close();
										}
//
									}

									@Override
									public void channelRead ( ChannelHandlerContext ctx, Object msg ) throws Exception {
										if (  msg instanceof ByteBuf ) {
											ByteBuf m = ( (ByteBuf) msg );
											System.out.println("time: "+ LocalDateTime.now().toString() +" msg: " + msg);
											if ( m.toString( StandardCharsets.UTF_8).endsWith( "AL]" ) ) {
												at.incrementAndGet();
											}
										}
										super.channelRead( ctx, msg );
									}
								} )
						;
					}
				})
				.connect(new InetSocketAddress("124.70.8.149", 9998));

			ChannelFuture closeFuture = channelFuture.sync().channel().closeFuture();
			closeFuture.addListener( new GenericFutureListener<Future<? super Void>>() {
				@Override
				public void operationComplete ( Future<? super Void> future ) throws Exception {
					WORKER.shutdownGracefully();
				}
			} );
			closeFuture.sync();
		} catch (InterruptedException e) {
		e.printStackTrace();
	} finally {
		WORKER.shutdownGracefully();
	}
	}


}
