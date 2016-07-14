/**
  * Copyright 2016 aTool.org 
  */
package com.xiangsk.myhelper.bean;

/**
 * Auto-generated: 2016-06-30 17:24:15
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class WeatherDataRsp {

    private String desc;
    private int status;
    private Data data;
    public void setDesc(String desc) {
         this.desc = desc;
     }
     public String getDesc() {
         return desc;
     }

    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setData(Data data) {
         this.data = data;
     }
     public Data getData() {
         return data;
     }

}