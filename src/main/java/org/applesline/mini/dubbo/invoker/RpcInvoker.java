package org.applesline.mini.dubbo.invoker;

import org.applesline.mini.dubbo.common.Constants;
import org.applesline.mini.dubbo.protocol.RpcProtocol;

/**
 * @author liuyaping
 * 创建时间：2020年06月04日
 */
public class RpcInvoker extends AbstractInvoker {

    @Override
    public void doInvoke(Invocation invocation,int requestId) {
        log.info("客户端发起rpc调用请求：{}" ,invocation.toString());
        RpcProtocol protocol = new RpcProtocol();
        protocol.setType(Constants.getType(Invocation.class));
        protocol.setRequestId(requestId);
        protocol.setBody(invocation);

        channel.writeAndFlush(protocol);
    }

}

