package org.applesline.mini.dubbo;

/**
 * @author liuyaping
 * 创建时间：2020年06月12日
 */
public interface HelloWorld {

    void sayHelloworld();

    String sayHelloworld(String name);

    boolean sayHelloworld(Message message);

}
