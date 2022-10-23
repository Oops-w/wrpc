package com.w.wrpc.pojo;

import java.io.Serializable;

/**
 * @author wsy
 * @date 2021/9/21 8:51 下午
 * @Description
 */
public class InfoUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public InfoUser(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public InfoUser() {
    }
}
