package com.xiangsk.myhelper.bean;

/**
 * Created by holmes-zhenyu on 2016/7/11.
 */
public class CityBean {
    private int code;
    private String name1;
    private String name2;
    private String name3;

    public CityBean() {
    }
    public CityBean(String name3) {
        this.name3 = name3;
    }

    public String getFullName() {
        return name3 + (name2.equals(name3) ? "" : " " + name2) + (name1.equals(name2) ? "" : " " + name1);
    }

    public CityBean(int code, String name1, String name2, String name3) {
        this.code = code;
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

}
