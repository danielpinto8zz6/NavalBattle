package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;

import java.io.Serializable;

import io.github.danielpinto8zz6.navalbattle.Constants.GameMode;

public class Game implements Serializable {
    private Context context;
    private Player player;
    private Player opponent;
    private GameMode mode;

    public Game(Context context) {
        this.context = context;

        player = new Player(context);
        opponent = new Player(context, false, true);
    }

    public Game(Context context, GameMode mode) {
        this.context = context;
        this.mode = mode;

        player = new Player(context);
        opponent = new Player(context, false, true);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }
}
