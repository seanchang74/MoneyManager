package com.example.moneymanager.Model;

public class Expense {

    private String type;
    private String date;
    private String num;
    private String memo;

    public Expense(String type, String date, String num, String memo) {
        this.type = type;
        this.date = date;
        this.num = num;
        this.memo = memo;
    }

    public String getDate() {
        return date;
    }

    public String getNum() {
        return num;
    }

    public String getMemo() {
        return memo;
    }
}
