package io.github.danielpinto8zz6.navalbattle.game;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.github.danielpinto8zz6.navalbattle.Constants.GameMode;

public class Game implements Serializable {
    private final Player player;
    private final Player opponent;
    private GameMode mode;
    private Player winner;

    public Game(Context c) {
        player = new Player(c);
        opponent = new Player(c, true, true);

        mode = GameMode.Local;
    }

    public Game(Context c, GameMode mode) {
        this.mode = mode;

        player = new Player(c);
        opponent = new Player(c, false, true);
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public boolean isGameOver() {
        if (getPlayer().getBattleField().isShipsDestroyed()) {
            winner = opponent;
            return true;
        } else if (getOpponent().getBattleField().isShipsDestroyed()) {
            winner = player;
            return true;
        }
        return false;
    }

    public Player getWinner() {
        return winner;
    }

    public String getGameSave() throws JSONException {
        // This will return a json string of the game
        Gson gson = new GsonBuilder().create();
        JSONObject json = new JSONObject();
        json.put("player_name", getPlayer().getName());
        json.put("player_battlefield", gson.toJson(getPlayer().getBattleField().getField()));
        json.put("opponent_name", getOpponent().getName());
        json.put("opponent_battlefield", gson.toJson(getOpponent().getBattleField().getField()));

        return json.toString();
    }
}
