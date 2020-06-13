package org.applesline.mini.dubbo.common;

import org.applesline.mini.dubbo.invoker.Invocation;

import java.lang.reflect.Method;

/**
 * @author liuyaping
 * 创建时间：2020年06月10日
 */
public class Util {

    public static String[] getParameterTypes(Class[] classType) {
        String[] parameterType = new String[classType.length];
        for (int i=0;i<classType.length;i++) {
            parameterType[i] = classType[i].getName();
        }
        return parameterType;
    }

    public static Method getTargetMethod(Class clz, Invocation invocation, String[] parameterTypes) {
        for (Method method : clz.getDeclaredMethods()) {
            if (method.getName().equals(invocation.getMethodName())
                    && method.getParameterTypes().length == parameterTypes.length) {
                if (parameterTypes.length == 0) {
                    return method;
                }
                for (Class<?> parameterTypeClass : method.getParameterTypes()) {
                    for (String parameterType : parameterTypes) {
                        if (parameterType.equals(parameterTypeClass.getName())) {
                            return method;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Class toClass(String type) {
        switch (type) {
            case "byte":
                return Byte.class;
            case "int":
                return Integer.class;
            case "short":
                return Short.class;
            case "long":
                return Long.class;
            case "double":
                return Double.class;
            case "float":
                return Float.class;
            case "boolean":
                return Boolean.class;
            case "char":
                return Character.class;
        }
        return null;
    }
}
