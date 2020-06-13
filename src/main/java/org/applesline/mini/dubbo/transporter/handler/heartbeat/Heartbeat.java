package org.applesline.mini.dubbo.transporter.handler.heartbeat;

/**
 * @author liuyaping
 * 创建时间：2020年06月10日
 */
public class Heartbeat {

    private String type;
    private long timestamp = System.currentTimeMillis();

    public Heartbeat(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Heartbeat{" +
                "type='" + type + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
