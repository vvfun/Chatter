package com.project.chatter.bean;

import java.util.ArrayList;

public class RecordsBean<T> extends BaseBean {

    private ArrayList<T> records;

    public ArrayList<T> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<T> records) {
        this.records = records;
    }
}
