package io.github.danielpinto8zz6.navalbattle.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import io.github.danielpinto8zz6.navalbattle.R;

import static io.github.danielpinto8zz6.navalbattle.Utils.generateRandomNumber;

public class BattleField implements Serializable {
    private int[][] field = new int[8][8];
    private final ArrayList<Ship> ships = new ArrayList<>();
    private boolean showShips = true;
    private Ship selectedShip = null;
    private final ArrayList<Coordinates> givenShots = new ArrayList<>();
    private int shipsHitten = 0;
    private boolean shipGotDestroyed = false;
    private boolean removeShips = false;

    public BattleField() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                field[x][y] = R.color.water;
            }
        }

        // Setup ships from one random predefined setup
        shipsSetup(generateRandomNumber(1, 5));

        // Rotate ships setup random
        rotateShipsRandom();
    }

    private void shipsSetup(int setup) {
        switch (setup) {
            case 1:
                addShip(new Ship(0, 0, new ArrayList<>(Collections.singletonList(new Coordinates(1, 1)))));
                addShip(new Ship(0, 0, new ArrayList<>(Collections.singletonList(new Coordinates(1, 3)))));
                addShip(new Ship(1, 0, new ArrayList<>(Arrays.asList(new Coordinates(3, 1), new Coordinates(4, 1)))));
                addShip(new Ship(1, 0, new ArrayList<>(Arrays.asList(new Coordinates(3, 3), new Coordinates(4, 3)))));
                addShip(new Ship(2, 0, new ArrayList<>(Arrays.asList(new Coordinates(6, 2), new Coordinates(6, 1), new Coordinates(6, 3)))));
                addShip(new Ship(2, 0, new ArrayList<>(Arrays.asList(new Coordinates(1, 6), new Coordinates(1, 5), new Coordinates(1, 7)))));
                addShip(new Ship(3, 0, new ArrayList<>(Arrays.asList(new Coordinates(6, 6), new Coordinates(5, 5), new Coordinates(6, 5), new Coordinates(7, 5), new Coordinates(6, 7)))));
                break;
            case 2:
                addShip(new Ship(0, 0, new ArrayList<>(Collections.singletonList(new Coordinates(0, 0)))));
                addShip(new Ship(0, 0, new ArrayList<>(Collections.singletonList(new Coordinates(7, 1)))));
                addShip(new Ship(1, 0, new ArrayList<>(Arrays.asList(new Coordinates(3, 2), new Coordinates(4, 2)))));
                addShip(new Ship(1, 0, new ArrayList<>(Arrays.asList(new Coordinates(6, 7), new Coordinates(7, 7)))));
                addShip(new Ship(2, 0, new ArrayList<>(Arrays.asList(new Coordinates(0, 6), new Coordinates(0, 5), new Coordinates(0, 7)))));
                addShip(new Ship(2, 0, new ArrayList<>(Arrays.asList(new Coordinates(3, 6), new Coordinates(2, 6), new Coordinates(4, 6)))));
                addShip(new Ship(3, 0, new ArrayList<>(Arrays.asList(new Coordinates(6, 4), new Coordinates(5, 4), new Coordinates(7, 4), new Coordinates(7, 3), new Coordinates(7, 5)))));
                break;
            case 3:
                addShip(new Ship(0, 0, new ArrayList<>(Collections.singletonList(new Coordinates(7, 1)))));
                addShip(new Ship(0, 0, new ArrayList<>(Collections.singletonList(new Coordinates(6, 7)))));
                addShip(new Ship(1, 0, new ArrayList<>(Arrays.asList(new Coordinates(3, 3), new Coordinates(4, 3)))));
                addShip(new Ship(1, 0, new ArrayList<>(Arrays.asList(new Coordinates(6, 3), new Coordinates(7, 3)))));
                addShip(new Ship(2, 0, new ArrayList<>(Arrays.asList(new Coordinates(2, 6), new Coordinates(2, 5), new Coordinates(2, 7)))));
                addShip(new Ship(2, 0, new ArrayList<>(Arrays.asList(new Coordinates(5, 5), new Coordinates(4, 5), new Coordinates(6, 5)))));
                addShip(new Ship(3, 0, new ArrayList<>(Arrays.asList(new Coordinates(1, 1), new Coordinates(0, 0), new Coordinates(1, 0), new Coordinates(2, 0), new Coordinates(1, 2)))));
                break;
            case 4:
                addShip(new Ship(0, 0, new ArrayList<>(Collections.singletonList(new Coordinates(0, 0)))));
                addShip(new Ship(0, 0, new ArrayList<>(Collections.singletonList(new Coordinates(7, 7)))));
                addShip(new Ship(1, 0, new ArrayList<>(Arrays.asList(new Coordinates(7, 0), new Coordinates(7, 1)))));
                addShip(new Ship(1, 0, new ArrayList<>(Arrays.asList(new Coordinates(3, 6), new Coordinates(4, 6)))));
                addShip(new Ship(2, 0, new ArrayList<>(Arrays.asList(new Coordinates(0, 5), new Coordinates(0, 4), new Coordinates(0, 6)))));
                addShip(new Ship(2, 0, new ArrayList<>(Arrays.asList(new Coordinates(5, 4), new Coordinates(4, 4), new Coordinates(6, 4)))));
                addShip(new Ship(3, 0, new ArrayList<>(Arrays.asList(new Coordinates(3, 1), new Coordinates(2, 1), new Coordinates(4, 1), new Coordinates(4, 0), new Coordinates(4, 2)))));
                break;
            case 5:
                addShip(new Ship(0, 0, new ArrayList<>(Collections.singletonList(new Coordinates(0, 3)))));
                addShip(new Ship(0, 0, new ArrayList<>(Collections.singletonList(new Coordinates(7, 3)))));
                addShip(new Ship(1, 0, new ArrayList<>(Arrays.asList(new Coordinates(1, 0), new Coordinates(2, 0)))));
                addShip(new Ship(1, 0, new ArrayList<>(Arrays.asList(new Coordinates(3, 5), new Coordinates(4, 5)))));
                addShip(new Ship(2, 0, new ArrayList<>(Arrays.asList(new Coordinates(1, 6), new Coordinates(1, 5), new Coordinates(1, 7)))));
                addShip(new Ship(2, 0, new ArrayList<>(Arrays.asList(new Coordinates(6, 6), new Coordinates(6, 5), new Coordinates(6, 7)))));
                addShip(new Ship(3, 0, new ArrayList<>(Arrays.asList(new Coordinates(4, 2), new Coordinates(4, 1), new Coordinates(4, 3), new Coordinates(3, 3), new Coordinates(5, 3)))));
                break;
        }
    }

    public boolean attackPosition(Coordinates c) {
        // check if position is valid
        if (c.x < 0 || c.x >= 8 && c.y < 0 || c.y >= 8) {
            return false;
        }

        if (field[c.x][c.y] == R.color.attacked || field[c.x][c.y] == R.drawable.ship_destroyed || field[c.x][c.y] == R.drawable.ship_sunk) {
            return false;
        }

        if (field[c.x][c.y] == R.drawable.ship) {
            field[c.x][c.y] = R.drawable.ship_destroyed;

            Ship ship = getShipAtPosition(c);

            shipsHitten++;

            ship.setHitten(true);

            if (isShipDestroyed(ship)) {
                shipGotDestroyed = true;
                ship.setDestroyed(true);
            }

            return true;
        }

        field[c.x][c.y] = R.color.attacked;

        return true;
    }

    public void removeDestroyedShipsFromMap() {
        for (Ship ship : ships) {
            if (ship.isDestroyed() && !ship.isRemoved())
                for (Coordinates c : ship.getPositions()) {
                    if (removeShips)
                        field[c.x][c.y] = R.color.water;
                    else
                        field[c.x][c.y] = R.drawable.ship_sunk;
                    ship.setRemoved(true);
                }
        }
    }

    private boolean isShipDestroyed(Ship sh) {
        if (sh == null) return false;

        for (Coordinates c : sh.getPositions()) {
            if (field[c.x][c.y] != R.drawable.ship_destroyed)
                return false;
        }
        return true;
    }

    private void addShip(Ship ship) {
        for (Coordinates pos : ship.getPositions()) {
            if (!isPositionEmpty(pos.x, pos.y)) {
                return;
            }
        }

        for (Coordinates pos : ship.getPositions()) {
            field[pos.x][pos.y] = R.drawable.ship;
        }

        ships.add(ship);

    }

    private void rotateShipsRandom() {
        for (Ship ship : ships) {
            for (int i = 0; i < generateRandomNumber(0, 3); i++) {
                if (!rotateShip(ship)) break;
            }
        }
    }

    private boolean isPositionEmpty(int x, int y) {
        return field[x][y] == R.color.water;
    }

    public int[][] getField() {
        return field;
    }

    public void setField(int[][] field) {
        this.field = field;
    }

    public int get(int x, int y) {
        // Don't show anything while you're still shooting
        if (givenShots.contains(new Coordinates(x, y))) {
            return R.drawable.crosshair;
        }

        // don't show your ships to your opponent
        if (!showShips && field[x][y] == R.drawable.ship) {
            return R.color.water;
        }

        if (selectedShip != null) {
            if (selectedShip.getPositions().contains(new Coordinates(x, y)))
                return R.drawable.ship_selected;
        }

        return field[x][y];
    }

    public void setShowShips(boolean showShips) {
        this.showShips = showShips;
    }

    private boolean isMoveValid(ArrayList<Coordinates> pos, ArrayList<Coordinates> whiteList) {
        // check first position
        for (Coordinates c : pos) {
            if (c.x < 0 || c.y < 0 || c.x > 7 || c.y > 7)
                return false;

            // Check if position is not occupied
            if (field[c.x][c.y] == R.drawable.ship && !whiteList.contains(new Coordinates(c.x, c.y)) ||
                    field[c.x][c.y] == R.drawable.ship_destroyed && !whiteList.contains(new Coordinates(c.x, c.y)) ||
                    field[c.x][c.y] == R.drawable.ship_selected && !whiteList.contains(new Coordinates(c.x, c.y)))
                return false;

            if (c.x > 0 && c.y > 0)
                if (field[c.x - 1][c.y - 1] == R.drawable.ship && !whiteList.contains(new Coordinates(c.x - 1, c.y - 1)) ||
                        field[c.x - 1][c.y - 1] == R.drawable.ship_destroyed && !whiteList.contains(new Coordinates(c.x - 1, c.y - 1)) ||
                        field[c.x - 1][c.y - 1] == R.drawable.ship_selected && !whiteList.contains(new Coordinates(c.x - 1, c.y - 1)))
                    if (!pos.contains(new Coordinates(c.x - 1, c.y - 1)))
                        return false;

            if (c.x < 7 && c.y < 7)
                if (field[c.x + 1][c.y + 1] == R.drawable.ship && !whiteList.contains(new Coordinates(c.x + 1, c.y + 1)) ||
                        field[c.x + 1][c.y + 1] == R.drawable.ship_destroyed && !whiteList.contains(new Coordinates(c.x + 1, c.y + 1)) ||
                        field[c.x + 1][c.y + 1] == R.drawable.ship_selected && !whiteList.contains(new Coordinates(c.x + 1, c.y + 1)))
                    if (!pos.contains(new Coordinates(c.x + 1, c.y + 1)))
                        return false;

            if (c.x < 7 && c.y > 0)
                if (field[c.x + 1][c.y - 1] == R.drawable.ship && !whiteList.contains(new Coordinates(c.x + 1, c.y - 1)) ||
                        field[c.x + 1][c.y - 1] == R.drawable.ship_destroyed && !whiteList.contains(new Coordinates(c.x + 1, c.y - 1)) ||
                        field[c.x + 1][c.y - 1] == R.drawable.ship_selected && !whiteList.contains(new Coordinates(c.x + 1, c.y - 1)))
                    if (!pos.contains(new Coordinates(c.x + 1, c.y - 1)))
                        return false;

            if (c.x > 0 && c.y < 7)
                if (field[c.x - 1][c.y + 1] == R.drawable.ship && !whiteList.contains(new Coordinates(c.x - 1, c.y + 1)) ||
                        field[c.x - 1][c.y + 1] == R.drawable.ship_destroyed && !whiteList.contains(new Coordinates(c.x - 1, c.y + 1)) ||
                        field[c.x - 1][c.y + 1] == R.drawable.ship_selected && !whiteList.contains(new Coordinates(c.x - 1, c.y + 1)))
                    if (!pos.contains(new Coordinates(c.x - 1, c.y + 1)))
                        return false;

            if (c.x > 0)
                if (field[c.x - 1][c.y] == R.drawable.ship && !whiteList.contains(new Coordinates(c.x - 1, c.y)) ||
                        field[c.x - 1][c.y] == R.drawable.ship_destroyed && !whiteList.contains(new Coordinates(c.x - 1, c.y)) ||
                        field[c.x - 1][c.y] == R.drawable.ship_selected && !whiteList.contains(new Coordinates(c.x - 1, c.y)))
                    // If position is not part of the ship than it means position is not valid to be occupied
                    if (!pos.contains(new Coordinates(c.x - 1, c.y)))
                        return false;


            if (c.y > 0)
                if (field[c.x][c.y - 1] == R.drawable.ship && !whiteList.contains(new Coordinates(c.x, c.y - 1)) ||
                        field[c.x][c.y - 1] == R.drawable.ship_destroyed && !whiteList.contains(new Coordinates(c.x, c.y - 1)) ||
                        field[c.x][c.y - 1] == R.drawable.ship_selected && !whiteList.contains(new Coordinates(c.x, c.y - 1)))
                    if (!pos.contains(new Coordinates(c.x, c.y - 1)))
                        return false;


            if (c.x < 7)
                if (field[c.x + 1][c.y] == R.drawable.ship && !whiteList.contains(new Coordinates(c.x + 1, c.y)) ||
                        field[c.x + 1][c.y] == R.drawable.ship_destroyed && !whiteList.contains(new Coordinates(c.x + 1, c.y)) ||
                        field[c.x + 1][c.y] == R.drawable.ship_selected && !whiteList.contains(new Coordinates(c.x + 1, c.y)))
                    if (!pos.contains(new Coordinates(c.x + 1, c.y)))
                        return false;

            if (c.y < 7)
                if (field[c.x][c.y + 1] == R.drawable.ship && !whiteList.contains(new Coordinates(c.x, c.y + 1)) ||
                        field[c.x][c.y + 1] == R.drawable.ship_destroyed && !whiteList.contains(new Coordinates(c.x, c.y + 1)) ||
                        field[c.x][c.y + 1] == R.drawable.ship_selected && !whiteList.contains(new Coordinates(c.x, c.y + 1)))
                    if (!pos.contains(new Coordinates(c.x, c.y + 1)))
                        return false;
        }

        return true;
    }

    private boolean moveShip(Ship ship, ArrayList<Coordinates> newPositions) {
        if (!isMoveValid(newPositions, ship.getPositions()))
            return false;

        for (Coordinates c : ship.getPositions())
            field[c.x][c.y] = R.color.water;

        ship.setPositions(newPositions);

        for (Coordinates c : newPositions)
            field[c.x][c.y] = R.drawable.ship;
        return true;
    }

    public boolean rotateShip(Ship ship) {
        ArrayList<Coordinates> newPositions = new ArrayList<>();
        Coordinates base = ship.getPositions().get(0);

        switch (ship.getRotation()) {
            case 0:
                // Rotate to 90
                switch (ship.getType()) {
                    case 0:
                        return false;
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(90);
                            return false;
                        }
                        return true;
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(90);
                            return false;
                        }
                        return true;
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y + 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(90);
                            return false;
                        }
                        return true;
                }
                break;
            case 90:
                // Rotate to 180
                switch (ship.getType()) {
                    case 0:
                        return false;
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(180);
                            return false;
                        }
                        return true;
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(180);
                            return false;
                        }
                        return true;
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y + 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y + 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(180);
                            return false;
                        }
                        return true;
                }
                break;
            case 180:
                // Rotate to 270
                switch (ship.getType()) {
                    case 0:
                        return false;
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(270);
                            return false;
                        }
                        return true;
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(270);
                            return false;
                        }
                        return true;
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x - 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y + 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(270);
                            return false;
                        }
                        return true;
                }
                break;
            case 270:
                // Rotate to 0
                switch (ship.getType()) {
                    case 0:
                        return false;
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(0);
                            return false;
                        }
                        return true;
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(0);
                            return false;
                        }
                        return true;
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y - 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(0);
                            return false;
                        }
                        return true;
                }
                break;
        }
        return false;
    }

    public boolean move(Ship ship, Coordinates base) {
        ArrayList<Coordinates> newPositions = new ArrayList<>();

        // make sure pieces not destroyed
        for (Coordinates c : ship.getPositions()) {
            if (field[c.x][c.y] == R.drawable.ship_destroyed)
                return true;
        }

        switch (ship.getRotation()) {
            case 0:
                switch (ship.getType()) {
                    case 0:
                        newPositions.add(base);
                        return !moveShip(ship, newPositions);
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        return !moveShip(ship, newPositions);
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        return !moveShip(ship, newPositions);
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y - 1));
                        return !moveShip(ship, newPositions);
                }
                break;
            case 90:
                switch (ship.getType()) {
                    case 0:
                        newPositions.add(base);
                        return !moveShip(ship, newPositions);
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        return !moveShip(ship, newPositions);
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        return !moveShip(ship, newPositions);
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y + 1));
                        return !moveShip(ship, newPositions);
                }
                break;
            case 180:
                switch (ship.getType()) {
                    case 0:
                        newPositions.add(base);
                        return !moveShip(ship, newPositions);
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        return !moveShip(ship, newPositions);
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        return !moveShip(ship, newPositions);
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y + 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y + 1));
                        return !moveShip(ship, newPositions);
                }
                break;
            case 270:
                switch (ship.getType()) {
                    case 0:
                        newPositions.add(base);
                        return !moveShip(ship, newPositions);
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        return !moveShip(ship, newPositions);
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        return !moveShip(ship, newPositions);
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x - 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y + 1));
                        return !moveShip(ship, newPositions);
                }
                break;
        }
        return false;
    }

    public Ship getShipAtPosition(Coordinates c) {
        for (Ship ship : ships) {
            for (Coordinates pos : ship.getPositions()) {
                if (pos.x == c.x && pos.y == c.y) {
                    return ship;
                }
            }
        }
        return null;
    }

    public void setSelectedShip(Ship ship) {
        this.selectedShip = ship;
    }

    public boolean isShipsDestroyed() {
        for (Ship ship : ships) {
            if (!ship.isDestroyed())
                return false;
        }
        return true;
    }

    public ArrayList<Coordinates> getValidPositions() {
        ArrayList<Coordinates> validPositions = new ArrayList<>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (field[x][y] == R.color.water || field[x][y] == R.drawable.ship)
                    validPositions.add(new Coordinates(x, y));
            }
        }

        return validPositions;
    }

    public void clearGivenShots() {
        givenShots.clear();
    }

    public void addGivenShot(Coordinates c) {
        givenShots.add(c);
    }

    public int getShipsHitten() {
        return shipsHitten;
    }

    public void setShipsHitten(int shipsHitten) {
        this.shipsHitten = shipsHitten;
    }

    public boolean isShipsHitten() {
        for (Ship ship : ships) {
            if (!ship.isHitten())
                return false;
        }
        return true;
    }

    public boolean isShipGotDestroyed() {
        return shipGotDestroyed;
    }

    public void setShipGotDestroyed(boolean shipGotDestroyed) {
        this.shipGotDestroyed = shipGotDestroyed;
    }
}
