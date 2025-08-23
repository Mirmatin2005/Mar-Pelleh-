package com.example.marpelleh;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<Player> players = new ArrayList<>();
        players.add(new Player("matin", "red"));
        players.add(new Player("parsa", "green"));

        Game game = new Game(players);

        while (! game.isItOver()){
            game.nobat();
        }

        System.out.println("***Finish***");
    }
}
