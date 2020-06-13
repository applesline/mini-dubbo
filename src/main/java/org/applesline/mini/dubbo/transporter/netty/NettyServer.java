package org.applesline.mini.dubbo.transporter.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.applesline.mini.dubbo.context.Environment;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.registry.RegistryService;
import org.applesline.mini.dubbo.transporter.RemoteServer;
import org.applesline.mini.dubbo.transporter.codec.RpcRequestDecoder;
import org.applesline.mini.dubbo.transporter.codec.RpcRequestEncoder;
import org.applesline.mini.dubbo.transporter.handler.RpcInvocationHandler;
import org.applesline.mini.dubbo.transporter.handler.heartbeat.HeartbeatHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public class NettyServer implements RemoteServer {

    public static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    private static final int LENGTH_FIELD_OFFSET = Environment.getEnv("mini-dubbo.lengthfield.decoder.lengthFieldOffset",Integer.class);

    private static final int LENGTH_FIELD_LENGTH = Environment.getEnv("mini-dubbo.lengthfield.decoder.lengthFieldLength",Integer.class);

    private static final int LENGTH_AADJUSTMENT = Environment.getEnv("mini-dubbo.lengthfield.decoder.lengthAdjustment",Integer.class);

    private static final int INITIAL_BYTES_TO_STRIP = Environment.getEnv("mini-dubbo.lengthfield.decoder.initialBytesToStrip",Integer.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private ChannelFuture channelFuture;

    public Channel bind(int port) throws Exception {
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup)
                .option(ChannelOption.SO_BACKLOG,Environment.getEnv("mini-dubbo.server.so_backlog",Integer.class))
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    public void initChannel(NioSocketChannel ch){
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                                LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_AADJUSTMENT, INITIAL_BYTES_TO_STRIP));
                        ch.pipeline().addLast(new LengthFieldPrepender(LENGTH_FIELD_LENGTH));
                        ch.pipeline().addLast(new RpcRequestDecoder());
                        ch.pipeline().addLast(new RpcRequestEncoder());
                        ch.pipeline().addLast(new HeartbeatHandler());
                        ch.pipeline().addLast(new RpcInvocationHandler());
                    }
                });
        channelFuture = serverBootstrap.bind(port).sync();
        return channelFuture.channel();

    }

    @Override
    public void close() {
        RpcContext.getBean(RegistryService.class).unregister();
        channelFuture.channel().closeFuture();
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
