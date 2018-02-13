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
    private int myAces;
    private int rivalAces;
    private int myDoubles;
    private int rivalDoubles;
    private int myServes;
    private int rivalServes;
    private int myFirst;
    private int rivalFirst;
    private int mySecond;
    private int rivalSecond;
    private int myNet;
    private int rivalNet;

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
        this.myAces=oneGame[13];
        this.rivalAces=oneGame[14];
        this.myDoubles=oneGame[15];
        this.rivalDoubles=oneGame[16];
        this.myServes=oneGame[17];
        this.rivalServes=oneGame[18];
        this.myFirst=oneGame[19];
        this.rivalFirst=oneGame[20];
        this.mySecond=oneGame[21];
        this.rivalSecond=oneGame[22];
        this.myNet=oneGame[23];
        this.rivalNet=oneGame[24];

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

    public void setMyAces(int myAces){
        this.myAces=myAces;
    }

    public void setRivalAces(int rivalAces){
        this.rivalAces=rivalAces;
    }

    public void setMyDoubles(int myDoubles){
        this.myDoubles=myDoubles;
    }

    public void setRivalDoubles(int rivalDoubles){
        this.rivalDoubles=rivalDoubles;
    }

    public void setMyServes(int myServes){
        this.myServes=myServes;
    }

    public void setRivalServes(int rivalServes){
        this.rivalServes=rivalServes;
    }

    public void setMyFirst(int myFirst){
        this.myFirst=myFirst;
    }

    public void setRivalFirst(int rivalFirst){
        this.rivalFirst=rivalFirst;
    }

    public void setMySecond(int mySecond){
        this.mySecond=mySecond;
    }

    public void setRivalSecond(int rivalSecond){
        this.rivalSecond=rivalSecond;
    }

    public void setMyNet(int myNet){
        this.myNet=myNet;
    }

    public void setRivalNet(int rivalNet){
        this.rivalNet=rivalNet;
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

    public int getMyAces(){
        return myAces;
    }

    public int getRivalAces(){
        return rivalAces;
    }

    public int getMyDoubles(){
        return myDoubles;
    }

    public int getRivalDoubles(){
        return rivalDoubles;
    }

    public int getMyServes(){
        return myServes;
    }

    public int getRivalServes(){
        return rivalServes;
    }

    public int getMyFirst(){
        return myFirst;
    }

    public int getRivalFirst(){
        return rivalFirst;
    }

    public int getMySecond(){
        return mySecond;
    }

    public int getRivalSecond(){
        return rivalSecond;
    }

    public int getMyNet(){
        return myNet;
    }

    public int getRivalNet(){
        return rivalNet;
    }
}

