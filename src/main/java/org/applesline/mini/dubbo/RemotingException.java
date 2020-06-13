package org.applesline.mini.dubbo;

/**
 * @author liuyaping
 * 创建时间：2020年06月06日
 */
public class RemotingException extends RuntimeException {

    public RemotingException(String message) {
        super("[server client error] " + message);
    }
}
