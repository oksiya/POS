package com.example.pos;

public class Item {

    private String category;
    private String name;
    private int code;
    private double price;

    public Item(String category, String name, int code, double price) {
        this.category = category;
        this.name = name;
        this.code = code;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toString()
    {
        return name.toUpperCase() + "\t" + code + "\t "
                + price;
    }
}
