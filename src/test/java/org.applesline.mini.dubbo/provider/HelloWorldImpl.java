package org.applesline.mini.dubbo.provider;

import org.applesline.mini.dubbo.HelloWorld;
import org.applesline.mini.dubbo.Message;

/**
 * @author liuyaping
 * 创建时间：2020年06月12日
 */
public class HelloWorldImpl implements HelloWorld {
    @Override
    public void sayHelloworld() {
        System.out.println("[void] hello world");
    }

    @Override
    public String sayHelloworld(String name) {
        System.out.println("[String]:"+name + " hello world");
        return "success";
    }

    @Override
    public boolean sayHelloworld(Message message) {
        System.out.println("[Message]:" + message + " hello world");
        return true;
    }
}
