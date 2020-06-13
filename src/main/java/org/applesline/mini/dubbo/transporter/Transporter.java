package org.applesline.mini.dubbo.transporter;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public interface Transporter {

    RemoteServer openServer();

    Client openClient();

}
