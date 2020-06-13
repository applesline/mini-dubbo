package org.applesline.mini.dubbo.protocol;

import io.netty.channel.Channel;
import org.applesline.mini.dubbo.context.Environment;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.registry.AbstractRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public abstract class AbstractProtocol implements Protocol {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected Channel channel;

    public void export() {
        int port = Environment.getEnv("mini-dubbo.port",Integer.class);
        doExport(port);
        log.info("tcp server started on {}, elapsed time {}", AbstractRegistryService.getServerNode(), RpcContext.getThreadLocal().get().stop());
    }

    @Override
    public void destroy() {
        channel.closeFuture();
    }

    public abstract void doExport(int port);
}
