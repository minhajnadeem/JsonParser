package com.example.minhaj.jsonparser;

import java.util.HashMap;

/**
 * Created by minhaj on 22/08/2017.
 */

public class Data {

    private int id;
    private String text;
    private double[] geo;
    private User user;

    public Data(int id,String text,double[] geo,User user){
        setId(id);
        setText(text);
        setGeo(geo);
        setUser(user);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double[] getGeo() {
        return geo;
    }

    public void setGeo(double[] geo) {
        this.geo = geo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
