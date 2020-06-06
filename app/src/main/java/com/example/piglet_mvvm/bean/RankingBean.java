package com.example.piglet_mvvm.bean;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/29
 */
public class RankingBean {
    /**
     * "coinCount": 9722,
     * "level": 98,
     * "rank": 1,
     * "userId": 20382,
     * "username": "g**eii"
     */

    private int coinCount;
    private int level;
    private int rank;
    private int userId;
    private String username;

    public int getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
