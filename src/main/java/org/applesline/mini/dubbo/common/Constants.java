package org.applesline.mini.dubbo.common;

import org.applesline.mini.dubbo.invoker.Invocation;
import org.applesline.mini.dubbo.invoker.Result;
import org.applesline.mini.dubbo.transporter.handler.heartbeat.Heartbeat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuyaping
 * 创建时间：2020年06月10日
 */
public class Constants {

    public static final Map<Byte,Class<?>> SUPPORTED_SERIALIZE_TYPES = new ConcurrentHashMap<>();

    static {
        SUPPORTED_SERIALIZE_TYPES.put((byte)1, Invocation.class);
        SUPPORTED_SERIALIZE_TYPES.put((byte)2, Result.class);
        SUPPORTED_SERIALIZE_TYPES.put((byte)3, Heartbeat.class);
    }

    public static byte getType(Class type) {
        for (Map.Entry<Byte, Class<?>> entry : SUPPORTED_SERIALIZE_TYPES.entrySet()) {
            if (entry.getValue() == type) {
                return entry.getKey();
            }
        }
        return 0;
    }

    public static Class getClassType(byte type) {
        return SUPPORTED_SERIALIZE_TYPES.get(type);
    }
}
