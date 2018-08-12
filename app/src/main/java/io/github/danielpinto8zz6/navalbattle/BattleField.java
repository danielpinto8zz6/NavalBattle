package io.github.danielpinto8zz6.navalbattle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class BattleField implements Serializable {
    private int[][] field = new int[8][8];
    private ArrayList<Ship> ships = new ArrayList<>();
    private ArrayList<Coordinates> attackedPositions = new ArrayList<>();
    private boolean showShips = true;
    private Ship selectedShip = null;

    public BattleField() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                field[x][y] = R.color.white;
            }
        }
        addShip(new Ship(0, 0, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(1, 1)))));
        addShip(new Ship(0, 0, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(1, 3)))));

        addShip(new Ship(1, 0, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(3, 1), new Coordinates(4, 1)))));
        addShip(new Ship(1, 0, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(3, 3), new Coordinates(4, 3)))));

        addShip(new Ship(2, 0, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(6, 2), new Coordinates(6, 1), new Coordinates(6, 3)))));
        addShip(new Ship(2, 0, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(1, 6), new Coordinates(1, 5), new Coordinates(1, 7)))));

        addShip(new Ship(3, 0, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(6, 6), new Coordinates(5, 5), new Coordinates(6, 5), new Coordinates(7, 5), new Coordinates(6, 7)))));
    }

    public boolean attackPosition(Coordinates c) {
        // check if position is valid
        if (c.getX() < 0 || c.getX() >= 8 && c.getY() < 0 || c.getX() >= 8) {
            return false;
        }

        // if position is already attacked
        if (attackedPositions.contains(c)) {
            return false;
        }

        attackedPositions.add(c);

        field[c.x][c.y] = R.color.black;

//        for (Ship ship : ships) {
//            if (ship.getPositions().contains(c)) {
//                ship.destroy(c.y, c.y);
//                if (ship.isDestroyed()) {
//                    removeShip(ship);
//                }
//            }
//        }

        return true;
    }

    public boolean addShip(Ship ship) {
        for (Coordinates pos : ship.getPositions()) {
            if (!isPositionEmpty(pos.x, pos.y)) {
                return false;
            }
        }

        for (Coordinates pos : ship.getPositions()) {
            field[pos.x][pos.y] = R.color.ship;
        }

        ships.add(ship);

        return true;

    }

    public boolean removeShip(Ship ship) {
        if (ship == null) return false;

        for (Coordinates pos : ship.getPositions()) {
            field[pos.x][pos.y] = R.color.white;
        }

        ships.remove(ship);

        return true;

    }

    private boolean isPositionEmpty(int x, int y) {
        return (field[x][y] == R.color.white) ? true : false;
    }

    public int[][] getField() {
        return field;
    }

    public void setField(int[][] field) {
        this.field = field;
    }

    public int get(int x, int y) {
        // don't show your ships to your opponent
        if (!showShips && field[x][y] == R.color.ship) {
            return R.color.white;
        }

        if (selectedShip != null) {
            if (selectedShip.getPositions().contains(new Coordinates(x, y)))
                return R.color.ship_selected;
        }

        return field[x][y];
    }

    public int getSize() {
        return field.length;
    }

    public void setShowShips(boolean showShips) {
        this.showShips = showShips;
    }

    public boolean isMoveValid(ArrayList<Coordinates> pos, ArrayList<Coordinates> whiteList) {
        // check first position
        for (Coordinates c : pos) {
            if (c.x < 0 || c.y < 0 || c.x > 7 || c.y > 7)
                return false;

            // Check if position is not occupied
            if (field[c.x][c.y] != R.color.white && !whiteList.contains(new Coordinates(c.x, c.y)))
                return false;

            if (c.x > 0 && c.y > 0)
                if (field[c.x - 1][c.y - 1] != R.color.white && !whiteList.contains(new Coordinates(c.x - 1, c.y - 1)))
                    if (!pos.contains(new Coordinates(c.x - 1, c.y - 1)))
                        return false;

            if (c.x < 7 && c.y < 7)
                if (field[c.x + 1][c.y + 1] != R.color.white && !whiteList.contains(new Coordinates(c.x + 1, c.y + 1)))
                    if (!pos.contains(new Coordinates(c.x + 1, c.y + 1)))
                        return false;

            if (c.x < 7 && c.y > 0)
                if (field[c.x + 1][c.y - 1] != R.color.white && !whiteList.contains(new Coordinates(c.x + 1, c.y - 1)))
                    if (!pos.contains(new Coordinates(c.x + 1, c.y - 1)))
                        return false;

            if (c.x > 0 && c.y > 7)
                if (field[c.x - 1][c.y + 1] != R.color.white && !whiteList.contains(new Coordinates(c.x - 1, c.y + 1)))
                    if (!pos.contains(new Coordinates(c.x - 1, c.y + 1)))
                        return false;

            if (c.x > 0)
                if (field[c.x - 1][c.y] != R.color.white && !whiteList.contains(new Coordinates(c.x - 1, c.y)))
                    // If position is not part of the ship than it means position is not valid to be occupied
                    if (!pos.contains(new Coordinates(c.x - 1, c.y)))
                        return false;


            if (c.y > 0)
                if (field[c.x][c.y - 1] != R.color.white && !whiteList.contains(new Coordinates(c.x, c.y - 1)))
                    if (!pos.contains(new Coordinates(c.x, c.y - 1)))
                        return false;


            if (c.x < 7)
                if (field[c.x + 1][c.y] != R.color.white && !whiteList.contains(new Coordinates(c.x + 1, c.y)))
                    if (!pos.contains(new Coordinates(c.x + 1, c.y)))
                        return false;

            if (c.y < 7)
                if (field[c.x][c.y + 1] != R.color.white && !whiteList.contains(new Coordinates(c.x, c.y + 1)))
                    if (!pos.contains(new Coordinates(c.x, c.y + 1)))
                        return false;
        }

        return true;
    }


    public boolean moveShip(Ship ship, ArrayList<Coordinates> newPositions) {
        if (!isMoveValid(newPositions, ship.getPositions()))
            return false;

        for (Coordinates c : ship.getPositions())
            field[c.x][c.y] = R.color.white;

        ship.setPositions(newPositions);

        for (Coordinates c : newPositions)
            field[c.x][c.y] = R.color.ship;
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
                        return true;
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(90);
                            return true;
                        }
                        return false;
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(90);
                            return true;
                        }
                        return false;
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y + 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(90);
                            return true;
                        }
                        return false;
                }
                break;
            case 90:
                // Rotate to 180
                switch (ship.getType()) {
                    case 0:
                        return true;
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(180);
                            return true;
                        }
                        return false;
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(180);
                            return true;
                        }
                        return false;
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y + 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y + 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(180);
                            return true;
                        }
                        return false;
                }
                break;
            case 180:
                // Rotate to 270
                switch (ship.getType()) {
                    case 0:
                        return true;
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(270);
                            return true;
                        }
                        return false;
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(270);
                            return true;
                        }
                        return false;
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x - 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y + 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(270);
                            return true;
                        }
                        return false;
                }
                break;
            case 270:
                // Rotate to 0
                switch (ship.getType()) {
                    case 0:
                        return true;
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(0);
                            return true;
                        }
                        return false;
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(0);
                            return true;
                        }
                        return false;
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y - 1));
                        if (moveShip(ship, newPositions)) {
                            ship.setRotation(0);
                            return true;
                        }
                        return false;
                }
                break;
        }
        return true;
    }

    public boolean move(Ship ship, Coordinates base) {
        ArrayList<Coordinates> newPositions = new ArrayList<>();

        switch (ship.getRotation()) {
            case 0:
                switch (ship.getType()) {
                    case 0:
                        newPositions.add(base);
                        return moveShip(ship, newPositions);
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        return moveShip(ship, newPositions);
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        return moveShip(ship, newPositions);
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y - 1));
                        return moveShip(ship, newPositions);
                }
                break;
            case 90:
                switch (ship.getType()) {
                    case 0:
                        newPositions.add(base);
                        return moveShip(ship, newPositions);
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        return moveShip(ship, newPositions);
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        return moveShip(ship, newPositions);
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y + 1));
                        return moveShip(ship, newPositions);
                }
                break;
            case 180:
                switch (ship.getType()) {
                    case 0:
                        newPositions.add(base);
                        return moveShip(ship, newPositions);
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        return moveShip(ship, newPositions);
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        return moveShip(ship, newPositions);
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        newPositions.add(new Coordinates(base.x, base.y + 1));
                        newPositions.add(new Coordinates(base.x + 1, base.y + 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y + 1));
                        return moveShip(ship, newPositions);
                }
                break;
            case 270:
                switch (ship.getType()) {
                    case 0:
                        newPositions.add(base);
                        return moveShip(ship, newPositions);
                    case 1:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x, base.y - 1));
                        return moveShip(ship, newPositions);
                    case 2:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        return moveShip(ship, newPositions);
                    case 3:
                        newPositions.add(base);
                        newPositions.add(new Coordinates(base.x - 1, base.y));
                        newPositions.add(new Coordinates(base.x + 1, base.y));
                        newPositions.add(new Coordinates(base.x - 1, base.y - 1));
                        newPositions.add(new Coordinates(base.x - 1, base.y + 1));
                        return moveShip(ship, newPositions);
                }
                break;
        }
        return true;
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

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public void set(int x, int y, int res) {
        field[x][y] = res;
    }

    public void setSelectedShip(Ship ship) {
        this.selectedShip = ship;
    }

    public boolean isShipsDestroyed() {
        for (Ship ship : ships) {
            if (ship.isDestroyed())
                return false;
        }
        return true;
    }

    public ArrayList<Coordinates> getValidPositions() {
        ArrayList<Coordinates> validPositions = new ArrayList<>();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (field[x][y] != R.color.attacked || field[x][y] != R.color.destroyed)
                    validPositions.add(new Coordinates(x, y));
            }
        }

        return validPositions;
    }
}
