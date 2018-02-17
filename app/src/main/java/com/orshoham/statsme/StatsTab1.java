package com.orshoham.statsme;

import android.util.Log;

import java.util.List;

public class StatsTab1 extends Tab1MyProfile {
    private int avgWinners=0;
    private int sumWinners=0;
    private int avgUNForced=0;
    private int sumUNForced=0;
    private int avgAces=0;
    private int sumAces=0;
    private int sumWins=0;
    private int sumLoss=0;

    public int numOfGamesPlayed (DBGames dbGames){
        List<GamesSQL> gameList = dbGames.getAllGames();
        return gameList.size();

    }

    public int checkSumWins (DBGames dbGames){
        List<GamesSQL> gameList = dbGames.getSpecificRowsByWhereEquals("win_or_loss", 1);
        Log.i("winorloss listSize", Integer.toString(gameList.size()));
        for(int i=0;i<gameList.size();i++){
            sumWins++;
        }
        Log.i("sum Wins", Integer.toString(sumWins));
        return sumWins;

    }

    public int checkSumLosses (DBGames dbGames){
        List<GamesSQL> gameList = dbGames.getSpecificRowsByWhereEquals("win_or_loss", 2);

        for(int i=0;i<gameList.size();i++){
            sumLoss++;
        }
        Log.i("sum Loss", Integer.toString(sumLoss));
        return sumLoss;

    }


    public int avgWinnersPG (DBGames dbGames){
        List<GamesSQL> gameList = dbGames.getAllGames();

        for(int i=0;i<gameList.size();i++){
            sumWinners=sumWinners+gameList.get(i).getMyWinners();
        }
        if (sumWinners>0){
            avgWinners=Math.round(sumWinners)/(gameList.size());
        }
        return avgWinners;

    }

    public int avgUNForcedPG (DBGames dbGames){
        List<GamesSQL> gameList = dbGames.getAllGames();

        for(int i=0;i<gameList.size();i++){
            sumUNForced=sumUNForced+gameList.get(i).getMyUNForced();
        }
        if (sumUNForced>0){
            avgUNForced=Math.round(sumUNForced/gameList.size());
        }
        return avgUNForced;

    }

    public int avgAcesPG (DBGames dbGames){
        List<GamesSQL> gameList = dbGames.getAllGames();

        for(int i=0;i<gameList.size();i++){
            sumAces=sumAces+gameList.get(i).getMyAces();
        }
        if (sumAces>0){
            avgAces=Math.round(sumAces/gameList.size());
        }
        return avgAces;

    }

    public String checkWinnerStringForGamesList(DBGames dbGames, int gameNumber){
        List<GamesSQL> gameList = dbGames.getSpecificRowsByWhereEquals("game_number", gameNumber);
        String winStatus="";
        Log.i("checkS", Integer.toString(gameNumber));
        Log.i("checkStatusWon", Integer.toString(gameList.get(0).getWinOrLoss()));
        if (gameList.get(0).getWinOrLoss()==1){
            winStatus = "You win!";
            Log.i("checkStatusWon", Integer.toString(gameList.get(0).getWinOrLoss()));
        }
        if (gameList.get(0).getWinOrLoss()==2){
            winStatus = "You lost";
        }
        if (gameList.get(0).getWinOrLoss()==3){
            winStatus = "Game didn't end properly";
        }
        return winStatus;
    }
}
