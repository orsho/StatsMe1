package com.orshoham.statsme;

import android.util.Log;

public class GamesSQL {

    private int id;
    private int gameNumber;
    private int mySet1;
    private int rivalSet1;
    private int mySet2;
    private int rivalSet2;
    private int mySet3;
    private int rivalSet3;
    private int myWinners;
    private int myForced;
    private int myUNForced;
    private int rivalWinners;
    private int rivalForced;
    private int rivalUNForced;
    private int winOrLoss;

    /*
    public GamesSQL(int mySet1, int rivalSet1, int mySet2, int rivalSet2, int mySet3, int rivalSet3,
                    int myWinners, int myForced, int myUNForced, int rivalWinners, int rivalForced, int rivalUNForced)
    {*/
    public GamesSQL(int[] oneGame){
        this.id=id;
        this.gameNumber=0;
        this.mySet1=oneGame[0];
        this.rivalSet1=oneGame[1];
        this.mySet2=oneGame[2];
        this.rivalSet2=oneGame[3];
        this.mySet3=oneGame[4];
        this.rivalSet3=oneGame[5];
        this.myWinners=oneGame[6];
        this.myForced=oneGame[7];
        this.myUNForced=oneGame[8];
        this.rivalWinners=oneGame[9];
        this.rivalForced=oneGame[10];
        this.rivalUNForced=oneGame[11];
        this.winOrLoss=oneGame[12];
    }

    public GamesSQL() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber=gameNumber;
    }

    public void setMySet1(int mySet1) {
        this.mySet1=mySet1;
    }

    public void setRivalSet1(int rivalSet1) {
        this.rivalSet1=rivalSet1;
    }

    public void setMySet2(int mySet2) {
        this.mySet2=mySet2;
    }

    public void setRivalSet2(int rivalSet2) {
        this.rivalSet2=rivalSet2;
    }

    public void setMySet3(int mySet3) {
        this.mySet3=mySet3;
    }

    public void setRivalSet3(int rivalSet3) {
        this.rivalSet3=rivalSet3;
    }

    public void setMyWinners(int myWinners) {
        this.myWinners=myWinners;
    }

    public void setMyForced(int myForced) {
        this.myForced=myForced;
    }

    public void setMyUNForced(int myUNForced) {
        this.myUNForced=myUNForced;
    }

    public void setRivalWinners(int rivalWinners) {
        this.rivalWinners=rivalWinners;
    }

    public void setRivalForced(int rivalForced) {
        this.rivalForced=rivalForced;
    }

    public void setRivalUNForced(int rivalUNForced) {
        this.rivalUNForced=rivalUNForced;
    }

    public void setWinOrLoss(int winOrLoss){
        this.winOrLoss=winOrLoss;
    }


    public int getId() {
        return id;
    }

    public int getGameNumber(){
        return gameNumber;
    }

    public int getMySet1() {
        return mySet1;
    }

    public int getRivalSet1() {
        return rivalSet1;
    }

    public int getMySet2() {
        return mySet2;
    }

    public int getRivalSet2() {
        return rivalSet2;
    }

    public int getMySet3() {
        return mySet3;
    }

    public int getRivalSet3() {
        return rivalSet3;
    }

    public int getMyWinners() {
        return myWinners;
    }

    public int getMyForced() {
        return myForced;
    }

    public int getMyUNForced() {
        return myUNForced;
    }

    public int getRivalWinners() {
        return rivalWinners;
    }

    public int getRivalForced() {
        return rivalForced;
    }

    public int getRivalUNForced() {
        return rivalUNForced;
    }

    public int getWinOrLoss(){
        return winOrLoss;
    }
}

