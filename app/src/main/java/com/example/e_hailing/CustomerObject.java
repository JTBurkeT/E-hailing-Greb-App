package com.example.e_hailing;

public class CustomerObject {
     private String userName, email,password,status,expectedArrivalTime,StartingLongLa,DestinationLongLa;
     private int capacity;

    public CustomerObject() {
    }

    public CustomerObject(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.status = "";
        this.expectedArrivalTime = "";
        StartingLongLa = "";
        DestinationLongLa = "";
        this.capacity =0;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public String getExpectedArrivalTime() {
        return expectedArrivalTime;
    }

    public String getStartingLongLa() {
        return StartingLongLa;
    }

    public String getDestinationLongLa() {
        return DestinationLongLa;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setExpectedArrivalTime(String expectedArrivalTime) {
        this.expectedArrivalTime = expectedArrivalTime;
    }

    public void setStartingLongLa(String startingLongLa) {
        StartingLongLa = startingLongLa;
    }

    public void setDestinationLongLa(String destinationLongLa) {
        DestinationLongLa = destinationLongLa;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
