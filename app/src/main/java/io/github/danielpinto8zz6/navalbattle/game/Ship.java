package io.github.danielpinto8zz6.navalbattle.game;

import java.io.Serializable;
import java.util.ArrayList;

import io.github.danielpinto8zz6.navalbattle.Coordinates;

public class Ship implements Serializable {
    //  Destroyed flag, Position
    private ArrayList<Coordinates> positions;
    private final int type;
    private int rotation;

    public Ship(int type, int rotation, ArrayList<Coordinates> positions) {
        this.type = type;
        this.positions = positions;
        this.rotation = rotation;
    }

    public int getType() {
        return type;
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
