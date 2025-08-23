package com.example.marpelleh;

public class Player {
    public String name;
    public int position;
    public String color;

    public  Player (String name, String color){
        this.name = name;
        this.color = color;
        this.position = 1;
    }

    public void move(int steps){
        position += steps;
        if(position > 100){
            position = 100;
        }
    }

    public void setPosition(int newPosition){
        this.position = newPosition;
    }

    public String getName(){
        return name;
    }
    public String getColor(){
        return color;
    }
    public  int getPosition(){
        return position;
    }
}
