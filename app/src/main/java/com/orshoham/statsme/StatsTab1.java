package com.orshoham.statsme;

import android.util.Log;

import java.util.List;

public class StatsTab1 extends Tab1MyProfile {
    private double avgWinners=0;
    private double sumWinners=0;
    private double avgUNForced=0;
    private double sumUNForced=0;
    private int sumWins=0;
    private int sumLoss=0;

    public int checkSumWins (DBGames dbGames){
        List<GamesSQL> gameList = dbGames.getSpecificRowsByWhereEquals("win_or_loss", 1);

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


    public double avgWinnersPG (DBGames dbGames){
        List<GamesSQL> gameList = dbGames.getAllGames();

        for(int i=0;i<gameList.size();i++){
            sumWinners=sumWinners+gameList.get(i).getMyWinners();
        }
        avgWinners=(Math.round(1.0 *sumWinners)/(gameList.size()));
        return avgWinners;

    }

    public double avgUNForcedPG (DBGames dbGames){
        List<GamesSQL> gameList = dbGames.getAllGames();

        for(int i=0;i<gameList.size();i++){
            sumUNForced=sumUNForced+gameList.get(i).getMyUNForced();
        }
        avgUNForced=(1.0 * sumUNForced/(gameList.size()));
        return avgUNForced;

    }
}