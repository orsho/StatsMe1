package com.orshoham.statsme;

import android.util.Log;

import static com.orshoham.statsme.FirebaseGame.countGames;

public class TennisScoreCalculates {

    int myGamePoint = 0;
    int mySetScore = 0;
    int rivalGamePoint = 0;
    int rivalSetScore = 0;
    int[] myGameScore = {0, 0, 0, 0, 0};
    int[] rivalGameScore = {0, 0, 0, 0, 0};
    int countSets = 1;
    boolean tieBreakFlag = false;

    int countMyWinners = 0;
    int countMyForced = 0;
    int countMyUNForced = 0;
    int countRivalWinners = 0;
    int countRivalForced = 0;
    int countRivalUNForced = 0;

    public int getMyGamePoint(){
        return myGamePoint;
    }

    public int getMyGameScore(){
        return myGameScore[countSets];
    }

    public int getRivalGamePoint(){
        return rivalGamePoint;
    }

    public int getRivalGameScore(){
        return rivalGameScore[countSets];
    }

    public int getMyWinners(){
        return countMyWinners;
    }

    public int getMyForced(){
        return countMyForced;
    }

    public int getMyUNForced(){
        return countMyUNForced;
    }

    public int getRivalWinners(){
        return countRivalWinners;
    }

    public int getRivalForced(){
        return countRivalForced;
    }

    public int getRivalUNForced(){
        return countRivalUNForced;
    }


    public void addMyPoint() {
        if (tieBreakFlag == false){
            if (myGamePoint == 0){
                myGamePoint = 15;
                uploadPoint(true);
            }
            else if (myGamePoint == 15){
                myGamePoint = 30;
                uploadPoint(true);
            }
            else if (myGamePoint == 30){
                myGamePoint = 40;
                uploadPoint(true);
            }
            else if (myGamePoint == 40 && rivalGamePoint < 40){
                myGamePoint = 0;
                uploadPoint(true);
                rivalGamePoint = 0;
                addMyGameScore();
            }
            else if (myGamePoint == 40 && rivalGamePoint == 40){
                myGamePoint = 45;
                uploadPoint(true);
            }
            else if (myGamePoint == 40 && rivalGamePoint > 40){
                rivalGamePoint = 40;
                uploadPoint(false);
            }
            else if (myGamePoint == 45 && rivalGamePoint == 40){
                myGamePoint = 0;
                uploadPoint(true);
                rivalGamePoint = 0;
                addMyGameScore();
            }
        } else {
            if (myGamePoint<6){
                myGamePoint++;
                uploadPoint(true);
            } else if (myGamePoint >= 6 && (myGamePoint-rivalGamePoint) >= 1){
                myGamePoint = 0;
                uploadPoint(true);
                rivalGamePoint = 0;
                myGameScore[countSets] = 7;
                countSets++;
                mySetScore++;
                tieBreakFlag = false;
            } else if (myGamePoint >= 6 && (myGamePoint-rivalGamePoint) < 1){
                myGamePoint++;
                uploadPoint(true);
            }
        }
    }

    public void addRivalPoint() {
        if (tieBreakFlag == false){
            if (rivalGamePoint == 0){
                rivalGamePoint = 15;
                uploadPoint(false);
            }
            else if (rivalGamePoint == 15){
                rivalGamePoint = 30;
                uploadPoint(false);
            }
            else if (rivalGamePoint == 30){
                rivalGamePoint = 40;
                uploadPoint(false);
            }
            else if (rivalGamePoint == 40 && myGamePoint < 40){
                rivalGamePoint = 0;
                uploadPoint(false);
                myGamePoint = 0;
                addRivalGameScore();
            }
            else if (rivalGamePoint == 40 && myGamePoint == 40){
                rivalGamePoint = 45;
                uploadPoint(false);
            }
            else if (rivalGamePoint == 40 && myGamePoint > 40){
                myGamePoint = 40;
                uploadPoint(true);
            }
            else if (rivalGamePoint == 45 && myGamePoint == 40){
                myGamePoint = 0;
                rivalGamePoint = 0;
                uploadPoint(false);
                addRivalGameScore();
            }
        } else {
            if (rivalGamePoint<6){
                rivalGamePoint++;
                uploadPoint(false);
            }
            else if (rivalGamePoint >= 6 && (rivalGamePoint-myGamePoint) >= 1){
                rivalGamePoint = 0;
                uploadPoint(false);
                myGamePoint = 0;
                rivalGameScore[countSets] = 7;
                countSets++;
                rivalSetScore++;
                tieBreakFlag = false;
            }
            else if (rivalGamePoint >= 6 && (rivalGamePoint-myGamePoint) < 1){
                rivalGamePoint++;
                uploadPoint(false);
            }
        }


    }

    public void addMyGameScore(){
        countGames++;
        if (myGameScore[countSets] < 5) {
            myGameScore[countSets]++;
        }
        else if (myGameScore[countSets] == 5 && rivalGameScore[countSets]<5){
            myGameScore[countSets] = 6;
            countSets++;
            mySetScore++;
        } else if (myGameScore[countSets] == 5 && rivalGameScore[countSets] == 5) {
            myGameScore[countSets] = 6;
        } else if (myGameScore[countSets] == 5 && rivalGameScore[countSets] == 6) {
            myGameScore[countSets] = 6;
            tieBreakFlag = true;
            myGamePoint = 0;
            rivalGamePoint = 0;
        } else if (myGameScore[countSets] == 6 && rivalGameScore[countSets] < 6) {
            myGameScore[countSets] = 7;
            countSets++;
            mySetScore++;
        }
    }

    public void addRivalGameScore() {
        countGames++;
        if (rivalGameScore[countSets] < 5){
            rivalGameScore[countSets]++;
        }
        else if (rivalGameScore[countSets] == 5 && myGameScore[countSets]<5){
            rivalGameScore[countSets] = 6;
            countSets++;
            rivalSetScore++;
        } else if (rivalGameScore[countSets] == 5 && myGameScore[countSets] == 5) {
            rivalGameScore[countSets] = 6;
        } else if (rivalGameScore[countSets] == 5 && myGameScore[countSets] == 6) {
            rivalGameScore[countSets] = 6;
            tieBreakFlag = true;
            myGamePoint = 0;
            rivalGamePoint = 0;
        } else if (rivalGameScore[countSets] == 6 && myGameScore[countSets] < 6) {
            rivalGameScore[countSets] = 7;
            countSets++;
            rivalSetScore++;
        }
    }

    //upload the number of times there is five (myWinner) in SQLite to Firebase by sets
    public void addMyWinners(int sqlCountMyWinners){
        countMyWinners = sqlCountMyWinners;
        FirebaseGame.addMyWinnersFirebase(countSets, countMyWinners);
    }

    public void addMyForced(int sqlCountMyForced){
        countMyForced = sqlCountMyForced;
        FirebaseGame.addMyForcedFirebase(countSets, countMyForced);
    }

    public void addMyUNForced(int sqlCountMyUNForced){
        countMyUNForced = sqlCountMyUNForced;
        FirebaseGame.addMyUNForcedFirebase(countSets, countMyUNForced);
    }

    public void addRivalWinners(int sqlCountRivalWinners){
        countRivalWinners = sqlCountRivalWinners;
        FirebaseGame.addRivalWinnersFirebase(countSets, countRivalWinners);
    }

    public void addRivalForced(int sqlCountRivalForced){
        countRivalForced = sqlCountRivalForced;
        FirebaseGame.addRivalForcedFirebase(countSets, countRivalForced);
    }

    public void addRivalUNForced(int sqlCountRivalUNForced){
        countRivalUNForced = sqlCountRivalUNForced;
        FirebaseGame.addRivalUNForcedFirebase(countSets, countRivalUNForced);
    }

    public boolean checkWin(){
        boolean flagCheckWin = false;
        if (mySetScore == 2){
            flagCheckWin = true;
        }
        if (rivalSetScore == 2){
            flagCheckWin = true;
        }
        return flagCheckWin;
    }

    public void uploadPoint (boolean myPoint){
        if (myPoint == true){
            FirebaseGame.addMyGamePointFirebase(countSets, myGameScore[countSets], myGamePoint);
        } else {
            FirebaseGame.addRivalGamePointFirebase(countSets, rivalGameScore[countSets], rivalGamePoint);
        }
    }

}
