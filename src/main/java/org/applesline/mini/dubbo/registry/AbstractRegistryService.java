package org.applesline.mini.dubbo.registry;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.applesline.mini.dubbo.ServerNode;
import org.applesline.mini.dubbo.context.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public abstract class AbstractRegistryService implements RegistryService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected static ServerNode serverNode;

    protected static final Cache<String,List<ServerNode>> CACHE = CacheBuilder.newBuilder().build();

    static {
        try {
            serverNode = new ServerNode(InetAddress.getLocalHost().getHostAddress(), Environment.getEnv("mini-dubbo.port",Integer.class));
        } catch (UnknownHostException e) {
        }
    }

    public void register() {
        doRegister();
    }

    @Override
    public List<ServerNode> getRegistedServers() {
        String rootPath = getRootPath();
        List<ServerNode> serverNodes = CACHE.getIfPresent(rootPath);
        if (serverNodes == null) {
            serverNodes = getServerNodes();
            CACHE.put(rootPath,serverNodes);
        }
        return CACHE.getIfPresent(rootPath);
    }

    public abstract void doRegister();

    public abstract List<ServerNode> getServerNodes();

    public abstract String getRootPath();

    public Cache getCache() {
        return CACHE;
    }

    public static ServerNode getServerNode() {
        return serverNode;
    }

}
