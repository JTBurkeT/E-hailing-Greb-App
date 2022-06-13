package com.example.e_hailing;

import java.util.ArrayList;

public class DriverList <E>{
    private ArrayList<E> dl= new ArrayList<>();
    int size=0;

    public DriverList() {

    }

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
