package io.github.danielpinto8zz6.navalbattle.game;

import android.util.Log;

import java.util.ArrayList;

import io.github.danielpinto8zz6.navalbattle.activities.GameActivity;

import static io.github.danielpinto8zz6.navalbattle.Utils.generateRandomNumber;

public class DeviceAI {
    private final GameActivity activity;

    public DeviceAI(GameActivity activity) {
        this.activity = activity;
    }

    public void play(final Game game) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (game.getOpponent().isYourTurn()) {
                    Log.d("Naval Battle", "Shoots : " + game.getOpponent().getShots());
                    // Wait a bit
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (game.getOpponent().getShots() < 3) {
                        ArrayList<Coordinates> validPositions = game.getPlayer().getBattleField().getValidPositions();
                        Coordinates pos = validPositions.get(generateRandomNumber(0, validPositions.size()));
                        if (game.getPlayer().getBattleField().attackPosition(pos))
                            game.getOpponent().setShots(game.getOpponent().getShots() + 1);
                    }

                    game.getOpponent().setShots(0);

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.playerPlay();
                        }
                    });
                }
            }
        });
        thread.start();
    }
}