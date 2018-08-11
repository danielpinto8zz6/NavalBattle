package io.github.danielpinto8zz6.navalbattle;

import java.io.Serializable;

public class Coordinates implements Serializable {
    public int x;
    public int y;
    private boolean isAttacked = false;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isAttacked() {
        return isAttacked;
    }

    public void setAttacked(boolean attacked) {
        isAttacked = attacked;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Coordinates){
            Coordinates c = (Coordinates)o;
            if (c.x == x && c.y == y) {
                return true;
            }
        }

        return false;
    }
}
