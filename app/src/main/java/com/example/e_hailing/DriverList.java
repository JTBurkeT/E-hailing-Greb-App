package com.example.e_hailing;

import java.util.ArrayList;
/*
This class is to make a driverList which help to adapt the information into the driverAdapter
The driver list implement using arraylist
 */
public class DriverList <E>{
    private ArrayList<E> dl= new ArrayList<>();
    int size=0;

    //The constructor
    public DriverList() {

    }

    //The add driver method to add the driver into the arrayList
    public boolean addDriver(E e){
        size++;
        return dl.add(e);
    }

    public int getSize(){
        return size;
    }

    public E get(int i){
        return dl.get(i);
    }



}
