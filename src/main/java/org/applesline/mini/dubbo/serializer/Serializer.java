package org.applesline.mini.dubbo.serializer;

/**
 * @author liuyaping
 * 创建时间：2020年06月10日
 */
public interface Serializer<T> {

    byte[] serialize(Object obj);

    T deserialize(Class<T> type,byte[] bytes);
}
