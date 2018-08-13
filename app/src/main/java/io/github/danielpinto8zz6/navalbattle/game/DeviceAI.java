package io.github.danielpinto8zz6.navalbattle.game;

import java.util.ArrayList;

import io.github.danielpinto8zz6.navalbattle.Coordinates;

import static io.github.danielpinto8zz6.navalbattle.Utils.generateRandomNumber;

public class DeviceAI {
    private Game game;
    private boolean isGameRunning = true;
    private GameInterface gameInterface;
    private int shots = 0;

    public DeviceAI(Game game, GameInterface gameInterface) {
        this.game = game;
        this.gameInterface = gameInterface;
    }

    public void play() {
        Thread thread = new Thread(new Runnable() {
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
                            Coordinates pos = validPositions.get(generateRandomNumber(0, validPositions.size()));
                            if (getBattleField().attackPosition(pos)) shots++;
                        }

                        game.getOpponent().setYourTurn(false);
                        game.getPlayer().setYourTurn(true);
                        shots = 0;
                        gameInterface.dataChanged();
                    }
                }
            }
        });
        thread.start();
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