package com.winter.autumn.protocol;

import com.winter.autumn.conf.Config;
import com.winter.autumn.message.Message;
import com.winter.autumn.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
@ChannelHandler.Sharable
@Slf4j
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf,Message> {


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {

        ByteBuf buffer = ctx.alloc().buffer();

        //魔数  BABY
        buffer.writeBytes("BABY".getBytes());
        //版本号
        buffer.writeBytes(new byte[]{'V', '2'});
        Serializer.Algorithm algorithm = Config.getAlgorithm();
        //序列化算法 0 Jdk 1 Json 2 Hessian 3 Protobuf
        buffer.writeByte(algorithm.ordinal());
        //指令类型
        buffer.writeByte(msg.getMessageType());
        //请求序号
        buffer.writeInt(msg.getSequenceId());

        byte[] bytes = algorithm.serialize( msg );

        //正文长度 对象转数组，需要序列化
        buffer.writeInt(bytes.length);
        //消息正文
        buffer.writeBytes(bytes);

        out.add(buffer);

    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        CharSequence charSequence = in.readCharSequence(4, StandardCharsets.UTF_8);

        String version = in.readBytes(2).toString(StandardCharsets.UTF_8);

        byte serializer = in.readByte();

        byte messageType = in.readByte();

        int sequenceId = in.readInt();

        int length = in.readInt();


        byte[] bytes = new byte[length];
        ByteBuf buf = in.readBytes(bytes,0,length);
        Object message = Serializer.Algorithm.values()[serializer].deserialize( Message.getMessageClass( messageType ), bytes );
        out.add(message);


        log.info("读取到的信息: 魔数 {} ，版本号 {}， 序列化方式 {},消息类型 {}。请求序号 {}，消息长度 {}、消息体 {}",
                charSequence.toString(),version,serializer,messageType,sequenceId,length,message);

    }
}
