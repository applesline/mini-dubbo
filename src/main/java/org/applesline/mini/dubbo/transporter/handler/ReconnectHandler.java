package org.applesline.mini.dubbo.transporter.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.transporter.Client;

/**
 * @author liuyaping
 * 创建时间：2020年06月11日
 */
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Client client = RpcContext.getBean(Client.class);
        client.close();
        client.reconnect(0);
    }
}
