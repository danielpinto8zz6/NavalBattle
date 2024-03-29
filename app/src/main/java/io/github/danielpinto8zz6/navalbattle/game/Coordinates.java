package io.github.danielpinto8zz6.navalbattle.game;

import java.io.Serializable;

public class Coordinates implements Serializable {
    public final int x;
    public final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Coordinates) {
            Coordinates c = (Coordinates) o;
            return c.x == x && c.y == y;
        }

        return false;
    }
}
