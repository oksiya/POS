package com.example.pos;

public class Customer {
    private String tittle;
    private String name;
    private  String surname;
    private String phone;
    private double amount;

    public Customer(String tittle, String name, String surname, String phone, double amount) {
        this.tittle = tittle;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.amount = amount;
    }

    public Customer(String tittle, String name, String surname, String phone) {
        this.tittle = tittle;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "tittle='" + tittle + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
