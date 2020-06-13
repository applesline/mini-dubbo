package org.applesline.mini.dubbo.invoker;

import java.util.Arrays;

/**
 * @author liuyaping
 * 创建时间：2020年06月04日
 */
public class Invocation{

    /**
     * 请求的接口名称。
     */
    private String interfaceName;

    /**
     * 请求的接口方法。
     */
    private String methodName;

    /**
     * 参数类型。
     */
    private String[] parameterType;

    /**
     * 参数值。
     */
    private Object[] arguments;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getParameterType() {
        return parameterType;
    }

    public void setParameterType(String[] parameterType) {
        this.parameterType = parameterType;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "Invocation{" +
                "interfaceName='" + interfaceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterType=" + Arrays.toString(parameterType) +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }
}

