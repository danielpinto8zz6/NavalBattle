package io.github.danielpinto8zz6.navalbattle.game;

import java.io.Serializable;
import java.util.ArrayList;

import io.github.danielpinto8zz6.navalbattle.Coordinates;

public class Ship implements Serializable {
    //  Destroyed flag, Position
    private ArrayList<Coordinates> positions;
    private int type;
    private int rotation = 0;

    public Ship(int type, int rotation, ArrayList<Coordinates> positions) {
        this.type = type;
        this.positions = positions;
        this.rotation = rotation;
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

    public ArrayList<Coordinates> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Coordinates> positions) {
        this.positions = positions;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}
