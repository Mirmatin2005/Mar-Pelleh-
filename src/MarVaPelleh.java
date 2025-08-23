package com.example.marpelleh;
import  java.util.HashMap;
import  java.util.Map;

public class MarVaPelleh {
    private Map<Integer, Integer> harekat;

    public MarVaPelleh(){
        harekat = new HashMap<>();

        //Mar ha:
        harekat.put(22, 7);
        harekat.put(34, 11);
        harekat.put(98, 26);
        harekat.put(78, 67);
        harekat.put(48, 41);
        harekat.put(55, 29);

        //Pelle ha:
        harekat.put(3, 80);
        harekat.put(20, 37);
        harekat.put(16, 61);
        harekat.put(81, 96);
        harekat.put(39, 57);
        harekat.put(5, 19);
        harekat.put(44, 66);
    }

    public int checkPosition(int position){
        if(harekat.containsKey(position)){
            return harekat.get(position);
        }
        return position;
    }
}
