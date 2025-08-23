package com.example.marpelleh;
import java.util.Random;

public class Tass {
    private Random random;

    public Tass(){
        random = new Random();
    }
    public int shans(){
        return random.nextInt(6)+ 1;
    }
}
