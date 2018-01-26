package com.orshoham.statsme;

public class GamesSQL {

    private int id;
    private int mySet1;
    private int myWinners;

    public GamesSQL(int myWinners)
    {
        this.id=id;
        //this.mySet1=mySet1;
        this.myWinners=myWinners;
    }

    public GamesSQL() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMySet1(int mySet1) {
        this.mySet1=mySet1;
    }

    public void setMyWinners(int myWinners) {
        this.myWinners=myWinners;
    }


    public int getId() {
        return id;
    }

    public int getMySet1() {
        return mySet1;
    }

    public int getMyWinners() {
        return myWinners;
    }
}

