package org.applesline.mini.dubbo.protocol;

/**
 * 1.服务启动后服务地址被注册到注册中心并提供服务
 * 2.服务关闭
 *
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public interface Protocol {

    /**
     * 启动服务端对外提供服务。
     */
    void export();

    /**
     * 关闭服务
     */
    void destroy();

}
