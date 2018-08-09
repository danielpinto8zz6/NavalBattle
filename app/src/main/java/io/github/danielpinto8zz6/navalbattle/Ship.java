package io.github.danielpinto8zz6.navalbattle;

import android.util.ArrayMap;

import java.util.ArrayList;

public class Ship {
    //  Destroyed flag, Position
    private ArrayList<Coordinates> positions = new ArrayList<>();
    private int type;
    private Constants.Orientation orientation;

    public Ship(int type, Constants.Orientation orientation, ArrayList<Coordinates> positions) {
        this.type = type;
        this.orientation = orientation;
        this.positions = positions;
    }

    public boolean isPositionDestroyed(int x, int y) {
        for (Coordinates position : positions) {
            if (position.getX() == x && position.getX() == y && position.isAttacked()) {
                return true;
            }
        }
        return false;
    }

    public boolean isDestroyed() {
        for (Coordinates position : positions) {
            if (!position.isAttacked()) {
                return false;
            }
        }
        return true;
    }

    public void destroyPosition(int x, int y) {
        for (Coordinates position : positions) {
            if (position.getX() == x && position.getX() == y) {
                position.setAttacked(true);
            }
        }
    }

    public void destroy(Coordinates c) {
        for (Coordinates position : positions) {
            if (position.getX() == c.getX() && position.getX() == c.getX()) {
                position.setAttacked(true);
            }
        }
    }

    public int getSize() {
        return positions.size();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Constants.Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Constants.Orientation orientation) {
        this.orientation = orientation;
    }

    public ArrayList<Coordinates> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Coordinates> positions) {
        this.positions = positions;
    }

    public boolean contains (Coordinates c){
        return positions.contains(c);
    }
}
