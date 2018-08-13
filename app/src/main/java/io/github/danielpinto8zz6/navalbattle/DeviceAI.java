package io.github.danielpinto8zz6.navalbattle;

import java.util.ArrayList;

public class DeviceAI implements Runnable {
    private Game game;
    private boolean isGameRunning = true;
    private GameInterface gameInterface;
    private int shots = 0;

    public DeviceAI(Game game, GameInterface gameInterface) {
        this.game = game;
        this.gameInterface = gameInterface;
    }

    @Override
    public void run() {
        while (isGameRunning) {
            // Take 3 shoots
            if (game.getOpponent().isYourTurn()) {
                // Wait a bit
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (shots < 3) {
                    ArrayList<Coordinates> validPositions = getBattleField().getValidPositions();
                    Coordinates pos = validPositions.get(Utils.generateRandomNumber(0, validPositions.size()));
                    if (getBattleField().attackPosition(pos)) shots++;
                }

                game.getOpponent().setYourTurn(false);
                game.getPlayer().setYourTurn(true);
                shots = 0;
                gameInterface.dataChanged();
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