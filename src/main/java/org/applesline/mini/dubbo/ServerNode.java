package org.applesline.mini.dubbo;

/**
 * @author liuyaping
 * 创建时间：2020年06月02日
 */
public class ServerNode {

    private String ip;
    private int port;

    public ServerNode(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return ip+":"+port;
    }

}
