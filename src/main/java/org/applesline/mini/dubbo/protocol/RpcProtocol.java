package org.applesline.mini.dubbo.protocol;

/**
 * @author liuyaping
 * 创建时间：2020年06月10日
 */
public class RpcProtocol {

    private final int ver = 0x20200610;
    private int requestId;
    private byte type;
    private int length;
    private Object body;

    public int getVer() {
        return ver;
    }

    public byte getType() {
        return type;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "RpcProtocol{" +
                "ver=" + ver +
                ", requestId=" + requestId +
                ", type=" + type +
                ", length=" + length +
                ", body=" + body +
                '}';
    }
}

