package org.applesline.mini.dubbo.transporter.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.applesline.mini.dubbo.common.Constants;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.protocol.RpcProtocol;
import org.applesline.mini.dubbo.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author liuyaping
 * 创建时间：2020年06月04日
 */
public class RpcRequestDecoder extends MessageToMessageDecoder<ByteBuf> {

    public static final Logger log = LoggerFactory.getLogger(RpcProtocol.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        RpcProtocol request = new RpcProtocol();

        msg.readInt();
        request.setRequestId(msg.readInt());
        request.setType(msg.readByte());
        request.setLength(msg.readInt());
        byte[] bytes = new byte[request.getLength()];
        msg.readBytes(bytes);

        Serializer serializer = RpcContext.getBean(Serializer.class);
        request.setBody(serializer.deserialize(Constants.getClassType(request.getType()),bytes));
        out.add(request);
    }
}
