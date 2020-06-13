package org.applesline.mini.dubbo.transporter.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.applesline.mini.dubbo.common.Constants;
import org.applesline.mini.dubbo.common.Util;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.invoker.Invocation;
import org.applesline.mini.dubbo.invoker.Result;
import org.applesline.mini.dubbo.protocol.RpcProtocol;
import org.applesline.mini.dubbo.transporter.handler.heartbeat.Heartbeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuyaping
 * 创建时间：2020年06月04日
 */
public class RpcInvocationHandler extends ChannelInboundHandlerAdapter {

    public static final Logger log = LoggerFactory.getLogger(RpcInvocationHandler.class);

    public static final Gson GSON = new GsonBuilder().create();

    public static final AtomicInteger errorCount = new AtomicInteger(0);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.WRITER_IDLE){
                RpcProtocol protocol = new RpcProtocol();
                protocol.setType(Constants.getType(Heartbeat.class));
                protocol.setRequestId(1);
                Heartbeat heartbeat = new Heartbeat("ping");
                protocol.setBody(heartbeat);
//                log.info("客户端请求心跳包：{}",heartbeat.toString());
                ctx.channel().writeAndFlush(protocol);
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcProtocol protocol = (RpcProtocol)msg;
        log.info("服务端收到rpc调用请求:{}",protocol.toString());
        if (protocol.getType() == Constants.getType(Invocation.class)) {
            Invocation invocation = (Invocation) protocol.getBody();

            String[] parameterTypes = invocation.getParameterType();
            Object[] arguments = invocation.getArguments();
            for (int i=0;i<parameterTypes.length;i++) {
                Class parameterType = Util.toClass(parameterTypes[i]);
                if (parameterType == null) {
                    parameterType = Class.forName(parameterTypes[i]);
                }
                if (parameterType != String.class) {
                    arguments[i] = GSON.fromJson(String.valueOf(arguments[i]),parameterType);
                }
            }

            Class clz = Class.forName(invocation.getInterfaceName());
            Method method = Util.getTargetMethod(clz,invocation,parameterTypes);
            Result result = new Result();
            if (method != null) {
                try {
                    result.setReturnType(method.getReturnType().getName());
                    result.setData(method.invoke(RpcContext.getBean(clz),arguments));
                } catch (Exception e) {
                    result.setData(e.getCause().toString());
                    result.setReturnType(String.class.getName());

                }
            } else {
                result.setReturnType(String.class.getName());
                result.setData("["+invocation.getMethodName()+"] method not found");
            }

            protocol.setBody(result);
            protocol.setType(Constants.getType(Result.class));
            log.info("服务端发送rpc结果：{}",result);
            ctx.channel().writeAndFlush(protocol);
        } else {
            ctx.fireChannelRead(msg);
        }

    }

}
