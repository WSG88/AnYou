package com.anyou.yx;

public class StoreLocation {
    long id;
    String name;
    String address;
    String image;
    String mark;
    double lng;
    double lat;

    @Override
    public String toString() {
        return "StoreLocation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", image='" + image + '\'' +
                ", mark='" + mark + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
