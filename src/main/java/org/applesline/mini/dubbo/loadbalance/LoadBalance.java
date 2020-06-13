package org.applesline.mini.dubbo.loadbalance;

import org.applesline.mini.dubbo.ServerNode;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public interface LoadBalance {

    ServerNode select();
}
