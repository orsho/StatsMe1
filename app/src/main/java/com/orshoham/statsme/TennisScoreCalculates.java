package com.orshoham.statsme;

import android.util.Log;

public class TennisScoreCalculates {

    int myGamePoint = 0;
    int myGameScore = 0;
    int mySetScore = 0;
    int rivalGamePoint = 0;
    int rivalGameScore = 0;
    int rivalSetScore = 0;

    public int getUserPoint(){
        return myGamePoint;
    }

    public int getUserGame(){
        return myGameScore;
    }

    public int getRivalPoint(){
        return rivalGamePoint;
    }

    public int getRivalGame(){
        return rivalGameScore;
    }

    public void calculatePoint (){
        if (myGamePoint == 0);
    }

    public void addUserPoint() {
        if (myGamePoint == 0){
            myGamePoint = 15;
        }
        else if (myGamePoint == 15){
            myGamePoint = 30;
        }
        else if (myGamePoint == 30){
            myGamePoint = 40;
        }
        else if (myGamePoint == 40 && rivalGamePoint < 40){
            myGamePoint = 0;
            rivalGamePoint = 0;
            addUserGame();
        }
        else if (myGamePoint == 40 && rivalGamePoint == 40){
            myGamePoint = 45;
        }
        else if (myGamePoint == 40 && rivalGamePoint > 40){
            rivalGamePoint = 40;
        }
        else if (myGamePoint == 45 && rivalGamePoint == 40){
            myGamePoint = 0;
            rivalGamePoint = 0;
            addUserGame();
        }

    }

    public void addRivalPoint() {
        if (rivalGamePoint == 0){
            rivalGamePoint = 15;
        }
        else if (rivalGamePoint == 15){
            rivalGamePoint = 30;
        }
        else if (rivalGamePoint == 30){
            rivalGamePoint = 40;
        }
        else if (rivalGamePoint == 40 && myGamePoint < 40){
            rivalGamePoint = 0;
            myGamePoint = 0;
            addRivalGame();
        }
        else if (rivalGamePoint == 40 && myGamePoint == 40){
            rivalGamePoint = 45;
        }
        else if (rivalGamePoint == 40 && myGamePoint > 40){
            myGamePoint = 40;
        }
        else if (rivalGamePoint == 45 && myGamePoint == 40){
            myGamePoint = 0;
            rivalGamePoint = 0;
            addRivalGame();
        }

    }

    public void addUserGame(){
        if (myGameScore == 0){
            myGameScore = 1;
        }
        else if (myGameScore == 1){
            myGameScore = 2;
        }
        else if (myGameScore == 2){
            myGameScore = 3;
        }
        else if (myGameScore == 3){
            myGameScore = 4;
        }
        else if (myGameScore == 4){
            myGameScore = 5;
        }
        else if (myGameScore == 5){
            myGameScore = 6;
        }

    }

    public void addRivalGame() {

    }

}
