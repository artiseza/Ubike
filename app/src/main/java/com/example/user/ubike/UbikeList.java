package com.example.user.ubike;

/**
 * Created by asus on 26/04/2017.
 */

public class UbikeList {
    private int type;
    private String name;
    private String total;
    private String available;
    private String time;

    public UbikeList(int type, String name, String total,String available,String time) {
        this.type = type;
        this.name = name;
        this.total = total;
        this.available=available;
        this.time = time;
        // TODO Auto-generated constructor stub
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type = type;
    }
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    public String getTotal(){
        return total;
    }
    public void setTotal(String total){this.total = total;}
    public String getAvailable(){
        return available;
    }
    public void setAvailable(String available){this.available = available;}
    public String getTime(){
        return time;
    }
    public void setTime(String time){this.time = time;}


}
