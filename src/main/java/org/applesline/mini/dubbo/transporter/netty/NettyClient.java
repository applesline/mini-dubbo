package org.applesline.mini.dubbo.transporter.netty;

import com.google.inject.Inject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.applesline.mini.dubbo.ServerNode;
import org.applesline.mini.dubbo.context.Environment;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.invoker.AbstractInvoker;
import org.applesline.mini.dubbo.loadbalance.LoadBalance;
import org.applesline.mini.dubbo.proxy.ProxyFactory;
import org.applesline.mini.dubbo.transporter.Client;
import org.applesline.mini.dubbo.transporter.Transporter;
import org.applesline.mini.dubbo.transporter.codec.RpcRequestDecoder;
import org.applesline.mini.dubbo.transporter.codec.RpcRequestEncoder;
import org.applesline.mini.dubbo.transporter.handler.ReconnectHandler;
import org.applesline.mini.dubbo.transporter.handler.RpcResultHandler;
import org.applesline.mini.dubbo.transporter.handler.heartbeat.HeartbeatHandler;
import org.applesline.mini.dubbo.transporter.netty.test.ITest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public class NettyClient implements Client {

    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    private static final int MAX_RETRY_TIMES = Environment.getEnv("mini-dubbo.client.reconnect.times",Integer.class);

    private static final int RETRY_INTERVAL_TIME = Environment.getEnv("mini-dubbo.client.retry.interval.time",Integer.class);

    private static final int LENGTH_FIELD_OFFSET = Environment.getEnv("mini-dubbo.lengthfield.decoder.lengthFieldOffset",Integer.class);

    private static final int LENGTH_FIELD_LENGTH = Environment.getEnv("mini-dubbo.lengthfield.decoder.lengthFieldLength",Integer.class);

    private static final int LENGTH_AADJUSTMENT = Environment.getEnv("mini-dubbo.lengthfield.decoder.lengthAdjustment",Integer.class);

    private static final int INITIAL_BYTES_TO_STRIP = Environment.getEnv("mini-dubbo.lengthfield.decoder.initialBytesToStrip",Integer.class);

    @Inject
    private LoadBalance loadBalance;
    private ChannelFuture channelFuture;
    private EventLoopGroup workerGroup;

    @Override
    public Channel connect() throws Exception {
            workerGroup = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                            LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_AADJUSTMENT, INITIAL_BYTES_TO_STRIP));
                    ch.pipeline().addLast(new LengthFieldPrepender(LENGTH_FIELD_LENGTH));
                    ch.pipeline().addLast(new RpcRequestEncoder());
                    ch.pipeline().addLast(new RpcRequestDecoder());
                    ch.pipeline().addLast(new ReconnectHandler());
                    ch.pipeline().addLast(new HeartbeatHandler());
                    ch.pipeline().addLast(new RpcResultHandler());
                }
            });
            ServerNode serverNode = loadBalance.select();
            log.info("try to connect {}",serverNode.toString());
            channelFuture = b.connect(serverNode.getIp(), serverNode.getPort()).sync();
            return channelFuture.channel();

    }

    @Override
    public Channel reconnect(int retryTimes) {
        Channel channel;
        while (true) {
            try {
                channel = RpcContext.getBean(Transporter.class).openClient().connect();
                AbstractInvoker.setChannel(channel);
                log.info("重新连接成功");
//                testInvoker();
                break;
            } catch (Exception e) {
                if (MAX_RETRY_TIMES > 0 && retryTimes >= MAX_RETRY_TIMES) {
                    log.error("重试连接[{}]次失败，系统退出",retryTimes);
                    System.exit(-1);
                }
                log.error("客户端连接异常："+e.getMessage());
                try {
                    Thread.sleep(RETRY_INTERVAL_TIME);
                } catch (InterruptedException e1) {
                    log.error(e.getMessage(),e);
                }
                log.info("尝试第[{}]次重新连接...",++retryTimes);
            }
        }
        return channel;
    }

    private void testInvoker() {
        ITest test = ProxyFactory.getProxyObject(ITest.class);
        String word = "hello world";
        log.info("发送测试数据："+ word);
        String result = test.test(word);
        log.info("服务端的响应："+result);
    }

    @Override
    public void close() {
        channelFuture.channel().closeFuture();
        workerGroup.shutdownGracefully();
    }
}
