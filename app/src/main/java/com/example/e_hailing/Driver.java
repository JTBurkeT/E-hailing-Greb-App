package com.example.e_hailing;


/*
This class is the driver class represent the object of the driver
 */
public class Driver {
    private String name, status , currentLongLa, customer, arrivedTime;
    private int capacity,numberOfRating;
    private double rating;
    public Driver(){

    }
 //The constructor of the driver object
    public Driver(String name, String status, String currentLongLa, String customer, int capacity, double rating) {
        this.name = name;
        this.status = status;
        this.currentLongLa = currentLongLa;
        this.customer = customer;
        this.capacity = capacity;
        this.rating = rating;
        this.arrivedTime="";
        this.numberOfRating=0;
    }
//The getter and setter method of the object
    public String getCurrentLongLa() {
        return currentLongLa;
    }

    public String getName(){
        return name;
    }

    public void setArrivedTime(String i){
        arrivedTime=i;
    }

    public String getArrivedTime(){
        return arrivedTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCurrentLongLa(String currentLongLa) {
        this.currentLongLa = currentLongLa;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumberOfRating() {
        return numberOfRating;
    }

    public void setNumberOfRating(int numberOfRating) {
        this.numberOfRating = numberOfRating;
    }
}



