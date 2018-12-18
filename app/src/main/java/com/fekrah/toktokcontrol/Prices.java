package com.fekrah.toktokcontrol;

import java.io.Serializable;

public class Prices implements Serializable {

    private String extra_km_price;
    private String min_distance;
    private String min_distance_price;

    public Prices() {
    }

    public Prices(String extra_km_price, String min_distance, String min_distance_price) {
        this.extra_km_price = extra_km_price;
        this.min_distance = min_distance;
        this.min_distance_price = min_distance_price;
    }

    public String getExtra_km_price() {
        return extra_km_price;
    }

    public void setExtra_km_price(String extra_km_price) {
        this.extra_km_price = extra_km_price;
    }

    public String getMin_distance() {
        return min_distance;
    }

    public void setMin_distance(String min_distance) {
        this.min_distance = min_distance;
    }

    public String getMin_distance_price() {
        return min_distance_price;
    }

    public void setMin_distance_price(String min_distance_price) {
        this.min_distance_price = min_distance_price;
    }


}
