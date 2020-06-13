package org.applesline.mini.dubbo.invoker;

/**
 * @author liuyaping
 * 创建时间：2020年06月06日
 */
public class Result {

    private String returnType;
    private Object data;

    public Result waitForResult(int timeout) {
        synchronized (this) {
            try {
                this.wait(timeout);
            } catch (InterruptedException e) {
                this.data = e;
                this.returnType = InterruptedException.class.getName();
            }
        }
        return this;
    }

    public void notifyResult() {
        synchronized (this) {
            this.notify();
        }
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "returnType='" + returnType + '\'' +
                ", data=" + data +
                '}';
    }
}