package io.github.danielpinto8zz6.navalbattle.game;

import java.util.ArrayList;

import io.github.danielpinto8zz6.navalbattle.Coordinates;
import io.github.danielpinto8zz6.navalbattle.activities.GameActivity;

import static io.github.danielpinto8zz6.navalbattle.Utils.generateRandomNumber;

public class DeviceAI {
    private final Game game;
    private final GameActivity activity;
    private int shots = 0;

    public DeviceAI(Game game, GameActivity activity) {
        this.game = game;
        this.activity = activity;
    }

    public void play() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.dataChanged();
                        }
                    });
                }
            }
        });
        thread.start();
    }

    private BattleField getBattleField() {
        return game.getPlayer().getBattleField();
    }
}