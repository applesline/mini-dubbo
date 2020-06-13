package org.applesline.mini.dubbo.transporter.netty;

import com.google.inject.Inject;
import org.applesline.mini.dubbo.transporter.Client;
import org.applesline.mini.dubbo.transporter.RemoteServer;
import org.applesline.mini.dubbo.transporter.Transporter;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public class NettyTransporter implements Transporter {

    @Inject
    private RemoteServer remoteServer;
    @Inject
    private Client client;

    public RemoteServer openServer() {
        return remoteServer;
    }

    public Client openClient() {
        return client;
    }

}
