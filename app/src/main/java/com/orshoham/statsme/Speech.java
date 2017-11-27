package com.orshoham.statsme;

public class Speech {

    private int id;
    private String record;

    public Speech()
    {
    }

    //  public Speech(int id,String record)
    public Speech(String record)
    {
        this.id=id;
        this.record=record;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecord(String record) {
        this.record = record;
    }


    public int getId() {
        return id;
    }

    public String getRecord() {
        return record;
    }
}