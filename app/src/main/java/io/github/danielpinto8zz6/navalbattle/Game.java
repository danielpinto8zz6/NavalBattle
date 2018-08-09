package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;

public class Game {
    private Player player;
    private Opponent opponent;

    public Game(Context context, Constants.GameMode mode) {
        if (mode == Constants.GameMode.Network) {
            opponent = new Opponent(context, Constants.OpponentType.Player);
        } else {
            opponent = new Opponent(context, Constants.OpponentType.Computer);
        }
        player = new Player(context);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Opponent getOpponent() {
        return opponent;
    }

    public BattleField getOpponentBattleField() {
        return opponent.getBattleField();
    }

    public void setOpponentBattleField(BattleField battleField) {
        opponent.setBattleField(battleField);
    }

    public BattleField getPlayerBattleField() {
        return player.getBattleField();
    }

    public void setPlayerBattleField(BattleField battleField) {
        player.setBattleField(battleField);
    }

    public void run() {

    }
}
