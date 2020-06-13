package org.applesline.mini.dubbo.transporter.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.protocol.RpcProtocol;
import org.applesline.mini.dubbo.serializer.Serializer;

import java.util.List;

/**
 * @author liuyaping
 * 创建时间：2020年06月04日
 */
public class RpcRequestEncoder extends MessageToMessageEncoder<RpcProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol protocol, List out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();

        byteBuf.writeInt(protocol.getVer());
        byteBuf.writeInt(protocol.getRequestId());
        byteBuf.writeByte(protocol.getType());

        Serializer serializer = RpcContext.getBean(Serializer.class);
        byte[] bodyBytes = serializer.serialize(protocol.getBody());
        byteBuf.writeInt(bodyBytes.length);
        byteBuf.writeBytes(bodyBytes);

        out.add(byteBuf);
    }

}
