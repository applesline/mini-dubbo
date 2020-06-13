package org.applesline.mini.dubbo.consumer;

import org.applesline.mini.dubbo.HelloWorld;
import org.applesline.mini.dubbo.Message;
import org.applesline.mini.dubbo.context.Configuration;
import org.applesline.mini.dubbo.context.Environment;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.proxy.ProxyFactory;
import org.applesline.mini.dubbo.registry.redis.RedisRegistryService;


/**
 * @author liuyaping
 * 创建时间：2020年06月03日
 */
public class ServiceConsumer {

    public static void main(String[] args) {
        Environment.loadConfig("/test.properties");
        RpcContext.setConfiguration(new Configuration(RedisRegistryService.class));
        HelloWorld helloWorld = ProxyFactory.getProxyObject(HelloWorld.class);

        for (int i=0;i<100;i++) {
            Message message = new Message(i+1,"mini-dubbo-"+(i+1));
            helloWorld.sayHelloworld();
            System.out.println(helloWorld.sayHelloworld("mini-dubbo-"+(i+1)));
            System.out.println(helloWorld.sayHelloworld(message));

        }
        System.out.println("finished");

    }
}
