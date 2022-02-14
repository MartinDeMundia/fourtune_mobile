package com.coreictconsultancy.fourtune.pojo;
import java.io.Serializable;

public class Res_Sub_Wallets implements Serializable {
    String id, state, game, coins ,user_id ;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getGame() {
        return game;
    }
    public void setGame(String game) {
        this.game = game;
    }

    public String getCoins() {
        return coins;
    }
    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getUserId() {
        return user_id;
    }
    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

}
