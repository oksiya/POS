package com.example.pos;

import androidx.annotation.NonNull;

public class SSRecords {

    private  String account;
    private  String date;
    private  String category;
    private  String name;
    private  int code;
    private  Double debit;
    private  Double credit;
    private  Double price;
    private int quantity;


    public SSRecords(String account, String date, String category, String name, int code, Double debit, Double credit) {
        this.account = account;
        this.date = date;
        this.category = category;
        this.name = name;
        this.code = code;
        this.debit = debit;
        this.credit = credit;
    }

    public SSRecords(String category, String name, int code, Double price, int quantity) {
        this.date = date;
        this.category = category;
        this.name = name;
        this.code = code;
        this.price = price;
        this.quantity = quantity;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String toSales() {
        return account + " " +date + " " +category + " " +name + " " +code + " " +debit + " " +credit;
    }

    public String toStock() {
        return category + " " +name + " " +code + " " +price;
    }

}
