package com.example.minhaj.jsonparser;

/**
 * Created by minhaj on 22/08/2017.
 */

public class User {

    private String name;
    private int followersCount;

    public User(String name,int followersCount){
        setName(name);
        setFollowersCount(followersCount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }
}
