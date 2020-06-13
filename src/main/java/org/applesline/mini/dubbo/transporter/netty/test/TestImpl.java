package org.applesline.mini.dubbo.transporter.netty.test;

/**
 * @author liuyaping
 * 创建时间：2020年06月11日
 */
public class TestImpl implements ITest {
    @Override
    public String test(String info) {
        System.out.println("received test info ["+info+"]");
        return "success";
    }
}
