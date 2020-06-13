package org.applesline.mini.dubbo.loadbalance.support;

import org.applesline.mini.dubbo.ServerNode;
import org.applesline.mini.dubbo.loadbalance.AbstractLoadBalance;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    public ServerNode doSelect() {
        long index = System.currentTimeMillis()%SERVER_NODES.size();
        return SERVER_NODES.get((int)index);
    }

}
