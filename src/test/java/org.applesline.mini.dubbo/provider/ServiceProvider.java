package org.applesline.mini.dubbo.provider;


import org.applesline.mini.dubbo.context.Configuration;
import org.applesline.mini.dubbo.context.Environment;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.protocol.Protocol;
import org.applesline.mini.dubbo.registry.redis.RedisRegistryService;

/**
 * @author liuyaping
 * 创建时间：2020年06月05日
 */
public class ServiceProvider {

    public static void main(String[] args) throws Exception {
        Environment.loadConfig("/test.properties");
        RpcContext.setConfiguration(new Configuration(RedisRegistryService.class),new ServiceConfig());
        Protocol rpcProtocol = RpcContext.getBean(Protocol.class);
        rpcProtocol.export();
    }

}
