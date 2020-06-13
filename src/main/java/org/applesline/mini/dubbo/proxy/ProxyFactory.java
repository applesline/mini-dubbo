package org.applesline.mini.dubbo.proxy;

import org.applesline.mini.dubbo.common.Util;
import org.applesline.mini.dubbo.context.RpcContext;
import org.applesline.mini.dubbo.invoker.Invocation;
import org.applesline.mini.dubbo.invoker.Invoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author liuyaping
 * 创建时间：2020年06月03日
 */
public class ProxyFactory<T> {

    public static <T> T getProxyObject(Class<T> clz) {
        return (T)Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("toString".equals(method.getName())
                        || "equals".equals(method.getName())
                        || "hashCode".equals(method.getName())) {
                    return method.invoke(this,args);
                }
                Invocation invocation = new Invocation();

                invocation.setInterfaceName(clz.getName());
                invocation.setMethodName(method.getName());
                invocation.setParameterType(Util.getParameterTypes(method.getParameterTypes()));
                invocation.setArguments(args);

                return RpcContext.getBean(Invoker.class).invoke(invocation);
            }
        });
    }



}
