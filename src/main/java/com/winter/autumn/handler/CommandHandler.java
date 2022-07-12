package com.winter.autumn.handler;

import com.winter.autumn.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author HuangShk
 */
public class CommandHandler extends ChannelInboundHandlerAdapter {
    private final CountDownLatch countDownLatch;
    private final AtomicBoolean atomicBoolean;

    public CommandHandler(CountDownLatch countDownLatch, AtomicBoolean atomicBoolean) {
        this.countDownLatch = countDownLatch;
        this.atomicBoolean = atomicBoolean;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//                                    建立连接，登录
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入用户名");
            String userName = scanner.nextLine();
            System.out.println("请输入密码");
            String password = scanner.nextLine();
            LoginRequestMessage loginRequestMessage = new LoginRequestMessage(userName, password, "");
            ctx.writeAndFlush(loginRequestMessage);
            System.out.println("等待后续操作...");
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!atomicBoolean.get()) {
                ctx.channel().close();
                return;
            }
            while (true) {
                System.out.println("==================================");
                System.out.println("send [username] [content]");
                System.out.println("gsend [group name] [content]");
                System.out.println("gcreate [group name] [m1,m2,m3...]");
                System.out.println("gmembers [group name]");
                System.out.println("gjoin [group name]");
                System.out.println("gquit [group name]");
                System.out.println("quit");
                System.out.println("==================================");
                String command = scanner.nextLine();
                String[] commands = command.split(" ");
                Message message = null;

                try {
                    switch (commands[0]) {
                        case "send":
                            message = new ChatRequestMessage(userName, commands[1], commands[2]);
                            break;
                        case "gsend":
                            message = new GroupChatRequestMessage(userName, commands[1], commands[2]);
                            break;
                        case "gcreate":
                            HashSet<String> set = new HashSet<>(Arrays.asList(commands[2].split( "," )));
                            set.add(userName);
                            message = new GroupCreateRequestMessage(commands[1], set);
                            break;
                        case "gmembers":
                            message = new GroupMembersRequestMessage(commands[1]);
                            break;
                        case "gjoin":
                            message = new GroupJoinRequestMessage(userName, commands[1]);
                            break;
                        case "gquit":
                            message = new GroupQuitRequestMessage(userName, commands[1]);
                            break;
                        case "quit":
                            ctx.channel().close();
                            break;
                        default:
                            System.out.println( "命令输入不正确,请验证!" );
                            break;
                    }
                    if (message != null) {
                        ctx.writeAndFlush(message);
                    }
                } catch ( Exception e ) {
                    System.out.println( "命令输入不正确,请验证!" );
                }
            }
        }).start();
        super.channelActive(ctx);
    }
}
