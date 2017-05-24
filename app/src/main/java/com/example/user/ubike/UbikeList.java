package com.example.user.ubike;

/**
 * Created by asus on 26/04/2017.
 */

public class UbikeList {
    private int type;
    private String name;
    private String Total;

    public UbikeList(int type, String name, String Total) {
        this.type = type;
        this.name = name;
        this.Total = Total;
        // TODO Auto-generated constructor stub
    }
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type = type;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){this.name = name;}
    public String getTotal(){
        return Total;
    }

    public void setTotal(String Total){this.Total = Total;}

}
