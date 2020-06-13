package org.applesline.mini.dubbo.transporter;

import io.netty.channel.Channel;

/**
 * @author liuyaping
 * 创建时间：2020年06月11日
 */
public interface Client extends Node {

    Channel connect() throws Exception;

    Channel reconnect(int retryTimes);

}
