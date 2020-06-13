package org.applesline.mini.dubbo;

/**
 * @author liuyaping
 * 创建时间：2020年06月12日
 */
public class Message {

    private int id;
    private String name;

    public Message(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name+id;
    }
}
