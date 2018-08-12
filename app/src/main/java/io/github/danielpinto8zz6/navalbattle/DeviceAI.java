package io.github.danielpinto8zz6.navalbattle;

public class DeviceAI implements Runnable {
    private Game game;

    public DeviceAI(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        game.getPlayer().getBattleField().attackPosition(new Coordinates(0,0));
    }
}
