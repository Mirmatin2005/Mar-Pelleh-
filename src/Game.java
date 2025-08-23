package com.example.marpelleh;
import java.util.List;

public class Game {
    private List<Player> players;
    private Tass tass;
    private MarVaPelleh marvapelleh;
    private int playerNum;
    private boolean isItOver;

    public Game(List<Player> players){
        this.players = players;
        this.tass = new Tass();
        this.marvapelleh = new MarVaPelleh();
        this.playerNum = 0;
        this.isItOver = false;
    }

    public boolean isItOver(){
        return isItOver;
    }

    public void nobat(){
        if (isItOver){
            return;
        }

        Player bazikon = players.get(playerNum);

        int shans = tass.shans();
        System.out.println(bazikon.getName() +" adad: " +shans);
        bazikon.move(shans);

        int newPosition = marvapelleh.checkPosition(bazikon.getPosition());
        if (newPosition != bazikon.getPosition()){
            System.out.println(bazikon.getName() + " raft be: " + newPosition);
            bazikon.setPosition(newPosition);
        }

        if (bazikon.getPosition() == 100){
            System.out.println(bazikon.getName() + " barande shod!");
            isItOver = true;
            return;
        }

        playerNum = (playerNum + 1) % players.size();

    }
}
