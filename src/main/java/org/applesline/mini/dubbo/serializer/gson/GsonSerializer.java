package org.applesline.mini.dubbo.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.applesline.mini.dubbo.serializer.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @author liuyaping
 * 创建时间：2020年06月10日
 */
public class GsonSerializer<T> implements Serializer<T> {

    public static final Logger log = LoggerFactory.getLogger(GsonSerializer.class);

    public static final Gson GSON = new GsonBuilder().create();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return GSON.toJson(obj).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public T deserialize(Class<T> type, byte[] bytes) {
        try {
            return GSON.fromJson(new String(bytes,"utf-8"),type);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

}
