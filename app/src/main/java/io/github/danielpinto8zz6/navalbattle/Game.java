package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;

import java.io.Serializable;

import io.github.danielpinto8zz6.navalbattle.Constants.GameMode;

public class Game implements Serializable {
    private Player player;
    private Player opponent;
    private GameMode mode;

    public Game(Context c) {
        player = new Player(c);
        opponent = new Player(c, true, true);
        player.setYourTurn(true);
    }

    public Game(Context c, GameMode mode) {
        this.mode = mode;

        player = new Player(c);
        opponent = new Player(c, false, true);
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
