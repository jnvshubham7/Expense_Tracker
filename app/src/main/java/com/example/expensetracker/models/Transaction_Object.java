package com.example.expensetracker.models;

import io.realm.annotations.PrimaryKey;

public class Transaction_Object {

    @PrimaryKey
    private String transactionObjectId;
    private String date;
    private int remainingAmount;

    public void setTransactionObjectId(String transactionObjectId) {
        this.transactionObjectId = transactionObjectId;
    }

    public String getTransactionObjectId() {
        return transactionObjectId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setRemainingAmount(int remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public int getRemainingAmount() {
        return remainingAmount;
    }
}
