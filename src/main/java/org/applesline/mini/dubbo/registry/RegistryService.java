package org.applesline.mini.dubbo.registry;

import org.applesline.mini.dubbo.ServerNode;

import java.util.List;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public interface RegistryService {

    void register();

    void unregister();

    List<ServerNode> getRegistedServers();
}
