package com.coreictconsultancy.fourtune.pojo;
import java.io.Serializable;

public class Res_Coins_Earned implements Serializable {
    String id, date, game, level, coins ;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getGame() {
        return game;
    }
    public void setGame(String game) {
        this.game = game;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }

    public String getCoins() {
        return coins;
    }
    public void setCoins(String coins) {
        this.coins = coins;
    }



}
