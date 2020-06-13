package org.applesline.mini.dubbo.registry.local;

import org.applesline.mini.dubbo.ServerNode;
import org.applesline.mini.dubbo.registry.AbstractRegistryService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyaping
 * 创建时间：2020年06月08日
 */
public class LocalRegistryService extends AbstractRegistryService {

    private final String rootPath = "mini-dubbo.servers";

    @Override
    public void doRegister() {
        List<ServerNode> serverNodes = CACHE.getIfPresent(rootPath);
        if (serverNodes == null) {
            serverNodes = new ArrayList<>();
            CACHE.put(rootPath,serverNodes);
        }
        serverNodes.add(serverNode);
    }

    @Override
    public void unregister() {
        CACHE.getIfPresent(rootPath).remove(serverNode);
    }

    @Override
    public List<ServerNode> getServerNodes() {
        return CACHE.getIfPresent(rootPath);
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }
}
