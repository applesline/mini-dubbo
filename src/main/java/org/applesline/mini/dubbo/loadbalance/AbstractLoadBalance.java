package org.applesline.mini.dubbo.loadbalance;


import org.applesline.mini.dubbo.ServerNode;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.registry.RegistryService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    protected static final List<ServerNode> SERVER_NODES = new ArrayList<ServerNode>();

    public ServerNode select() {
        if (SERVER_NODES.isEmpty()) {
            RegistryService registryService = RpcContext.getBean(RegistryService.class);
            SERVER_NODES.addAll(registryService.getRegistedServers());
        }
        return doSelect();
    }

    public abstract ServerNode doSelect();

}
