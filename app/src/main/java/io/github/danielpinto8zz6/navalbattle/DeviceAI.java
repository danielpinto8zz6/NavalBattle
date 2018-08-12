package io.github.danielpinto8zz6.navalbattle;

import java.util.ArrayList;

public class DeviceAI implements Runnable {
    private Game game;
    private boolean isGameRunning = true;
    private GameInterface gameInterface;
    private int result = 0;

    public DeviceAI(Game game, GameInterface gameInterface) {
        this.game = game;
        this.gameInterface = gameInterface;
    }

    @Override
    public void run() {
        while (isGameRunning) {
            // Take 3 shoots
            if (game.getOpponent().isYourTurn()) {
                while (result < 3) {
                    // 3 shots
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    ArrayList<Coordinates> validPositions = getBattleField().getValidPositions();
                    Coordinates pos = validPositions.get(Utils.generateRandomNumber(0, validPositions.size()));
                    if (getBattleField().attackPosition(pos)) result++;
                    gameInterface.dataChanged();
                }

                game.getOpponent().setYourTurn(false);
                game.getPlayer().setYourTurn(true);
                result = 0;
            }
        }


    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        isGameRunning = gameRunning;
    }

    public BattleField getBattleField() {
        return game.getPlayer().getBattleField();
    }
}