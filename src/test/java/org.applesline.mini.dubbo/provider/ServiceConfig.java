package org.applesline.mini.dubbo.provider;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.applesline.mini.dubbo.HelloWorld;

/**
 * @author liuyaping
 * 创建时间：2020年06月08日
 */
public class ServiceConfig extends AbstractModule {

    @Override
    protected void configure() {
        bind(HelloWorld.class).to(HelloWorldImpl.class).in(Singleton.class);
    }

}
