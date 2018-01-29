package com.orshoham.statsme;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class TennisScoreCalculates {

    int myGamePoint = 0;
    int mySetScore = 0;
    int rivalGamePoint = 0;
    int rivalSetScore = 0;
    int[] myGameScore = {0, 0, 0, 0, 0};
    int[] rivalGameScore = {0, 0, 0, 0, 0};
    int countSets = 1;
    int countGames=0;
    int countPoints=0;
    boolean tieBreakFlag = false;

    int countMyWinners = 0;
    int countMyForced = 0;
    int countMyUNForced = 0;
    int countRivalWinners = 0;
    int countRivalForced = 0;
    int countRivalUNForced = 0;

    int winOrLoss = 3;

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

    public int getWinOrLoss(){
        return winOrLoss;
    }


    public void addMyPoint() {
        countPoints++;
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
        countPoints++;
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


    //count number of my winners, add it to Firebase and return it to the screen (GAME LIVE)
    public int addMyWinners(){
        countMyWinners++;
        FirebaseGame.addMyWinnersFirebase(countSets, countMyWinners);
        return countMyWinners;
    }

    public int addMyForced(){
        countMyForced++;
        FirebaseGame.addMyForcedFirebase(countSets, countMyForced);
        return countMyForced;
    }

    public int addMyUNForced(){
        countMyUNForced++;
        FirebaseGame.addMyUNForcedFirebase(countSets, countMyUNForced);
        return countMyUNForced;
    }

    public int addRivalWinners(){
        countRivalWinners++;
        FirebaseGame.addRivalWinnersFirebase(countSets, countRivalWinners);
        return countRivalWinners;
    }

    public int addRivalForced(){
        countRivalForced++;
        FirebaseGame.addRivalForcedFirebase(countSets, countRivalForced);
        return countRivalForced;
    }

    public int addRivalUNForced(){
        countRivalUNForced++;
        FirebaseGame.addMyUNForcedFirebase(countSets, countRivalUNForced);
        return countRivalUNForced;
    }

    /*
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
    }*/

    public boolean checkWin(){
        boolean flagCheckWin = false;
        if (mySetScore == 2){
            flagCheckWin = true;
            winOrLoss = 1;
        }
        if (rivalSetScore == 2){
            flagCheckWin = true;
            winOrLoss = 2;
        }
        return flagCheckWin;
    }

    public void uploadPoint (boolean myPoint){
        if (myPoint == true){
            FirebaseGame.addMyGamePointFirebase(countSets,countGames,countPoints, myGameScore[countSets], myGamePoint);
        } else {
            FirebaseGame.addRivalGamePointFirebase(countSets,countGames, countPoints, rivalGameScore[countSets], rivalGamePoint);
        }
        FirebaseGame.updateFinalResults(mySetScore,rivalSetScore);
    }

    public void zeroAllStatsInFirebase(){
        for (int i=1;i<=3;i++){
            FirebaseGame.addMyWinnersFirebase(i, 0);
            FirebaseGame.addMyForcedFirebase(i, 0);
            FirebaseGame.addMyUNForcedFirebase(i, 0);
            FirebaseGame.addRivalWinnersFirebase(i, 0);
            FirebaseGame.addRivalForcedFirebase(i, 0);
            FirebaseGame.addRivalUNForcedFirebase(i, 0);
        }
    }

    public int[] updateGameSQL()
    {
        GamesSQL game = new GamesSQL();
        //game.setGameNumber();
        game.setMySet1(myGameScore[1]);
        game.setRivalSet1(rivalGameScore[1]);
        game.setMySet2(myGameScore[2]);
        game.setRivalSet2(rivalGameScore[2]);
        game.setMySet3(myGameScore[3]);
        game.setRivalSet3(rivalGameScore[3]);
        game.setMyWinners(getMyWinners());
        game.setMyForced(getMyForced());
        game.setMyUNForced(getMyUNForced());
        game.setRivalWinners(getRivalWinners());
        game.setRivalForced(getRivalForced());
        game.setRivalUNForced(getRivalUNForced());
        game.setWinOrLoss(getWinOrLoss());
        int oneGame[] = new int[13];
        //oneGame[0]=game.getGameNumber();
        oneGame[0] = game.getMySet1();
        oneGame[1] = game.getRivalSet1();
        oneGame[2] = game.getMySet2();
        oneGame[3] = game.getRivalSet2();
        oneGame[4] = game.getMySet3();
        oneGame[5] = game.getRivalSet3();
        oneGame[6] = game.getMyWinners();
        oneGame[7] = game.getMyForced();
        oneGame[8] = game.getMyUNForced();
        oneGame[9] = game.getRivalWinners();
        oneGame[10] = game.getRivalForced();
        oneGame[11] = game.getRivalUNForced();
        oneGame[12] = game.getWinOrLoss();

        return oneGame;

    }

}
