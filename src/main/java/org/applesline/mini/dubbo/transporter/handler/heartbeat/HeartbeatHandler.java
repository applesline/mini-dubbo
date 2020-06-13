package org.applesline.mini.dubbo.transporter.handler.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateHandler;
import org.applesline.mini.dubbo.common.Constants;
import org.applesline.mini.dubbo.context.Environment;
import org.applesline.mini.dubbo.protocol.RpcProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author liuyaping
 * 创建时间：2020年06月08日
 */
public class HeartbeatHandler extends IdleStateHandler {

    public static final Logger log = LoggerFactory.getLogger(HeartbeatHandler.class);

    private static final int READER_IDLE_TIME = Environment.getEnv("mini-dubbo.heartbeat.readerIdleTime",Integer.class);

    private static final int WRITER_IDLE_TIME = Environment.getEnv("mini-dubbo.heartbeat.writerIdleTime",Integer.class);

    private static final int ALL_IDLE_TIME = Environment.getEnv("mini-dubbo.heartbeat.allIdleTime",Integer.class);

    public HeartbeatHandler() {
        super(READER_IDLE_TIME,WRITER_IDLE_TIME,ALL_IDLE_TIME, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcProtocol protocol = (RpcProtocol)msg;
        if (protocol.getType() == Constants.getType(Heartbeat.class)) {
            Heartbeat heartbeat = (Heartbeat) protocol.getBody();
            if (!"ping".equals(heartbeat.getType())) {
                return;
            }
            heartbeat = new Heartbeat("pong");
            protocol.setBody(heartbeat);
//            log.info("服务端响应心跳包：{}",heartbeat.toString());
            ctx.channel().writeAndFlush(protocol);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().closeFuture();
    }
}
