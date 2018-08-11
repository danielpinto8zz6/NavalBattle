package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleField {
    private int[][] field = new int[8][8];
    private ArrayList<Ship> ships = new ArrayList<>();
    private ArrayList<Coordinates> attackedPositions = new ArrayList<>();
    private boolean showShips = true;

    public BattleField() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                field[x][y] = R.color.white;
            }
        }
        addShip(new Ship(0, Constants.Orientation.Vertical, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(1, 1)))));
        addShip(new Ship(0, Constants.Orientation.Vertical, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(1, 3)))));

        addShip(new Ship(1, Constants.Orientation.Horizontal, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(3, 1), new Coordinates(4, 1)))));
        addShip(new Ship(1, Constants.Orientation.Horizontal, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(3, 3), new Coordinates(4, 3)))));

        addShip(new Ship(2, Constants.Orientation.Vertical, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(6, 1), new Coordinates(6, 2), new Coordinates(6, 3)))));
        addShip(new Ship(2, Constants.Orientation.Vertical, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(1, 5), new Coordinates(1, 6), new Coordinates(1, 7)))));

        addShip(new Ship(3, Constants.Orientation.Vertical, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(5, 5), new Coordinates(6, 5), new Coordinates(7, 5), new Coordinates(6, 6), new Coordinates(6, 7)))));
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

        for (Ship ship : ships) {
            if (ship.getPositions().contains(c)) {
                ship.destroy(c);
                break;
            }
        }

        return true;
    }

    public boolean addShip(Ship ship) {
        for (Coordinates pos : ship.getPositions()) {
            if (!isPositionEmpty(pos)) {
                return false;
            }
        }

        for (Coordinates pos : ship.getPositions()) {
            field[pos.x][pos.y] = R.color.ship;
        }

        ships.add(ship);

        return true;

    }

    private boolean isPositionEmpty(Coordinates position) {
        return (field[position.x][position.y] == R.color.white) ? true : false;
    }

    public int[][] getField() {
        return field;
    }

    public void setField(int[][] field) {
        this.field = field;
    }

    public int get(Coordinates c) {
        // don't show your ships to your opponent
        if (!showShips && field[c.getX()][c.getY()] == R.color.ship) {
            return R.color.white;
        }
        return field[c.getX()][c.getY()];
    }

    public int getSize() {
        return field.length;
    }

    public void setShowShips(boolean showShips) {
        this.showShips = showShips;
    }

    public boolean isMoveValid(ArrayList<Coordinates> pos, ArrayList<Coordinates> whiteList) {
        int auxField[][] = field;
        for (Coordinates c : whiteList) {
            auxField[c.x][c.y] = R.color.white;
        }
        // check first position
        for (Coordinates c : pos) {
            // Check if position is not occupied
            if (auxField[c.x][c.y] != R.color.white)
                return false;

            if (c.x > 0 && c.y > 0)
                if (auxField[c.x - 1][c.y - 1] != R.color.white)
                    if (!pos.contains(new Coordinates(c.x - 1, c.y - 1)))
                        return false;

            if (c.x < 7 && c.y < 7)
                if (auxField[c.x + 1][c.y + 1] != R.color.white)
                    if (!pos.contains(new Coordinates(c.x + 1, c.y + 1)))
                        return false;

            if (c.x < 7 && c.y > 0)
                if (auxField[c.x + 1][c.y - 1] != R.color.white)
                    if (!pos.contains(new Coordinates(c.x + 1, c.y - 1)))
                        return false;

            if (c.x > 0 && c.y > 7)
                if (auxField[c.x - 1][c.y + 1] != R.color.white)
                    if (!pos.contains(new Coordinates(c.x - 1, c.y + 1)))
                        return false;

            if (c.x > 0)
                if (auxField[c.x - 1][c.y] != R.color.white)
                    // If position is not part of the ship than it means position is not valid to be occupied
                    if (!pos.contains(new Coordinates(c.x - 1, c.y)))
                        return false;


            if (c.y > 0)
                if (field[c.x][c.y - 1] != R.color.white)
                    if (!pos.contains(new Coordinates(c.x, c.y - 1)))
                        return false;


            if (c.x < 7)
                if (auxField[c.x + 1][c.y] != R.color.white)
                    if (!pos.contains(new Coordinates(c.x + 1, c.y)))
                        return false;

            if (c.y < 7)
                if (auxField[c.x][c.y + 1] != R.color.white)
                    if (!pos.contains(new Coordinates(c.x, c.y + 1)))
                        return false;
        }

        return true;
    }


    public boolean moveShip(Ship ship, ArrayList<Coordinates> newPositions) {
        if (isMoveValid(newPositions, ship.getPositions())) {
            for (Coordinates c : ship.getPositions())
                field[c.x][c.y] = R.color.white;

            ship.setPositions(newPositions);

            for (Coordinates c : newPositions)
                field[c.x][c.y] = R.color.ship;
            return true;
        }

        return false;
    }

    public boolean canRotate(Ship ship) {
        Coordinates c = ship.getPositions().get(0);

        if (ship.getOrientation() == Constants.Orientation.Vertical) {
            // Move to horizontal if possible
            if (c.x + ship.getSize() > 8) {
                return false;
            }

            for (int x = c.x + 1; x < c.x + ship.getSize(); x++) {
                if (field[x][c.y] != R.color.white) {
                    if (c.y > 0) {
                        if (field[x][c.y + 1] != R.color.white) {
                            return false;
                        }
                    }
                    if (c.y < 7) {
                        if (field[x][c.y + 1] != R.color.white) {
                            return false;
                        }
                    }
                }
            }

            if (c.x + ship.getSize() < 8) {
                if (field[c.x + ship.getSize()][c.y] != R.color.white) {
                    if (c.y > 0) {
                        if (field[c.x + ship.getSize()][c.y - 1] != R.color.white) {
                            return false;
                        }
                    }
                    if (c.y < 7) {
                        if (field[c.x + ship.getSize()][c.y + 1] != R.color.white) {
                            return false;
                        }
                    }
                }
            }

        } else if (ship.getOrientation() == Constants.Orientation.Horizontal) {
            if (c.y + ship.getSize() > 8) {
                return false;
            }

            for (int y = c.y + 1; y < c.y + ship.getSize(); y++) {
                if (field[c.x][y] != R.color.white) {
                    if (c.x > 0) {
                        if (field[c.x + 1][y] != R.color.white) {
                            return false;
                        }
                    }
                    if (c.x < 7) {
                        if (field[c.x + 1][y] != R.color.white) {
                            return false;
                        }
                    }
                }
            }

            if (c.y + ship.getSize() < 8) {
                if (field[c.x][c.y + ship.getSize()] != R.color.white) {
                    if (c.x > 0) {
                        if (field[c.x - 1][c.y + ship.getSize()] != R.color.white) {
                            return false;
                        }
                    }
                    if (c.y < 7) {
                        if (field[c.x + 1][c.y + ship.getSize()] != R.color.white) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean rotateShip(Ship ship) {
        if (ship.getType() == 3) return false;

        if (!canRotate(ship)) return false;

        ArrayList<Coordinates> newPositions = new ArrayList<>();
        Coordinates c = ship.getPositions().get(0);

        // Clear old position on map
        for (Coordinates position : ship.getPositions()) {
            field[position.x][position.y] = R.color.white;
        }

        if (ship.getOrientation() == Constants.Orientation.Horizontal) {
            for (int y = c.y; y < c.y + ship.getSize(); y++) {
                newPositions.add(new Coordinates(c.x, y));
                field[c.x][y] = R.color.ship;
            }

            ship.setOrientation(Constants.Orientation.Vertical);

        } else if (ship.getOrientation() == Constants.Orientation.Vertical) {
            for (int x = c.x; x < c.x + ship.getSize(); x++) {
                newPositions.add(new Coordinates(x, c.y));
                field[x][c.y] = R.color.ship;
            }

            ship.setOrientation(Constants.Orientation.Horizontal);
        }

        ship.setPositions(newPositions);

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
}
