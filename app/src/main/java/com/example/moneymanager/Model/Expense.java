package com.example.moneymanager.Model;

public class Expense {

    private Integer id;
    private String type;
    private String date;
    private String num;
    private String memo;

    public Expense(Integer id, String type, String date, String num, String memo) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.num = num;
        this.memo = memo;
    }

    public Integer getId() { return id; }

    public String getType() { return type; }

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
