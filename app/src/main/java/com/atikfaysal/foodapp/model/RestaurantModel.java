package com.atikfaysal.foodapp.model;

public class RestaurantModel
{
    private String name,address,type;
    private int rating;


    public RestaurantModel(String value1,String value2,String value3,int value4)
    {
        this.name = value1;
        this.address = value2;
        this.type = value3;
        this.rating = value4;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
