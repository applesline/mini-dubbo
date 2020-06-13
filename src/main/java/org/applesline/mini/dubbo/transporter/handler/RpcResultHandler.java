package org.applesline.mini.dubbo.transporter.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.applesline.mini.dubbo.common.Constants;
import org.applesline.mini.dubbo.invoker.AbstractInvoker;
import org.applesline.mini.dubbo.invoker.Result;
import org.applesline.mini.dubbo.protocol.RpcProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuyaping
 * 创建时间：2020年06月04日
 */
public class RpcResultHandler extends ChannelInboundHandlerAdapter {

    private static final Gson GSON = new GsonBuilder().create();

    private static final Logger log = LoggerFactory.getLogger(RpcResultHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcProtocol protocol = (RpcProtocol)msg;

        if (protocol.getType() == Constants.getType(Result.class)) {
            Result result = AbstractInvoker.getResult(protocol.getRequestId());
            result.setReturnType(((Result)protocol.getBody()).getReturnType());
            result.setData(((Result)protocol.getBody()).getData());
            result.notifyResult();
            log.info("客户端接收rpc结果：{}",result);
        } else {
            ctx.fireChannelRead(msg);
        }

    }
}
