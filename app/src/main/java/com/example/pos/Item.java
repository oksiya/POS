package com.example.pos;

import java.util.ArrayList;

public class Item {

    private String category;
    private String name;
    private int code;
    private double price;
    private int quantity;
    private int quantitySelected;


    public Item(String category, String name, int code, double price, int quantity) {
        this.category = category;
        this.name = name;
        this.code = code;
        this.price = price;
        this.quantity = quantity;
        this.quantitySelected = 0;
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
    public int getQuantity() {
        return quantity;
    }

    public int getQuantitySelected() {
        return quantitySelected;
    }

    public void setQuantitySelected(int quantitySelected) {
        this.quantitySelected = quantitySelected;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString()
    {
        return name.toUpperCase() + "\t" + code + "\t "
                + price;
    }


    public String toReceipt() {
        String columnFormat = "%-15s %-10s\n";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(columnFormat, "Name", "Age"));
        sb.append(String.format(columnFormat, name.toUpperCase(), price));
        return sb.toString();
    }

    public String toConfirm()
    {
        return "Category : "+ category+ "\n"+
                "Name : "+ name+ "\n"+
                "Code : "+ code+ "\n"+
                "Price : "+ price+ "\n"+
                "Quantity : "+ quantity;
    }
}
