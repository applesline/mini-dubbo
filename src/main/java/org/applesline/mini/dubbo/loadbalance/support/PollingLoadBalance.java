package org.applesline.mini.dubbo.loadbalance.support;

import org.applesline.mini.dubbo.ServerNode;
import org.applesline.mini.dubbo.loadbalance.AbstractLoadBalance;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public class PollingLoadBalance extends AbstractLoadBalance {

    private AtomicInteger index = new AtomicInteger(0);

    @Override
    public ServerNode doSelect() {
        index.compareAndSet(Integer.MAX_VALUE,0);
        return SERVER_NODES.get(index.getAndIncrement() % SERVER_NODES.size());
    }

}
