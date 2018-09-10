package io.github.danielpinto8zz6.navalbattle.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.github.danielpinto8zz6.navalbattle.Constants.GameMode;

public class Game implements Serializable {
    private final Player player;
    private final Player opponent;
    private GameMode mode;
    private Player winner;
    private boolean over = false;
    private boolean started = false;

    public Game(GameMode mode) {
        this.mode = mode;

        player = new Player();
        opponent = new Player();
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
        if (over) return true;
        else if (getPlayer().getBattleField().isShipsDestroyed()) {
            winner = opponent;
            over = true;
            return true;
        } else if (getOpponent().getBattleField().isShipsDestroyed()) {
            winner = player;
            over = true;
            return true;
        }
        return false;
    }

    public Player getWinner() {
        return winner;
    }

    public String getGameSave() throws JSONException {
        // This will return a json string of the game
        DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm", Locale.getDefault());
        String date = df.format(Calendar.getInstance().getTime());

        Gson gson = new GsonBuilder().create();
        JSONObject json = new JSONObject();
        json.put("player_name", getPlayer().getName());
        json.put("player_battlefield", gson.toJson(getPlayer().getBattleField().getField()));
        json.put("opponent_name", getOpponent().getName());
        json.put("opponent_battlefield", gson.toJson(getOpponent().getBattleField().getField()));
        json.put("date", date);

        return json.toString();
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
