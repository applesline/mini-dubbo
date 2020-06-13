package org.applesline.mini.dubbo.transporter;

import io.netty.channel.Channel;

/**
 * @author liuyaping
 * 创建时间：2020年06月11日
 */
public interface RemoteServer extends Node {

    Channel bind(int port) throws Exception;

}
