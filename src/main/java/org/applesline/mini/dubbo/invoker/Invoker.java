package org.applesline.mini.dubbo.invoker;

/**
 * @author liuyaping
 * 创建时间：2020年06月04日
 */
public interface Invoker {

    Object invoke(Invocation invocation);

}
