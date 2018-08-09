package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleField {
    private int[][] field = new int[8][8];
    private ArrayList<Ship> ships = new ArrayList<>();
    private ArrayList<Coordinates> attackedPositions = new ArrayList<>();
    private BattleFieldAdapter adapter;
    private Context context;
    private boolean showShips = true;

    public BattleField(Context c) {
        context = c;

        adapter = new BattleFieldAdapter(c, this);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                field[x][y] = R.color.white;
            }
        }

        addShip(new Ship(0, Constants.Orientation.Vertical, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(0, 2), new Coordinates(0, 3), new Coordinates(0, 4)))));
        addShip(new Ship(0, Constants.Orientation.Vertical, new ArrayList<Coordinates>(Arrays.asList(new Coordinates(6, 2), new Coordinates(6, 3), new Coordinates(6, 4)))));
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

        refreshBattleField(); // refresh the gridviewvoid

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

        refreshBattleField();
        return true;

    }

    private boolean isPositionEmpty(Coordinates position) {
        return (field[position.x][position.y] == R.color.white) ? true : false;
    }

    private void refreshBattleField() {
        adapter.notifyDataSetChanged();
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

    public BattleFieldAdapter getAdapter() {
        return adapter;
    }

    public void setShowShips(boolean showShips) {
        this.showShips = showShips;
    }

    public boolean canRotate(Ship ship) {
        Coordinates c = ship.getPositions().get(0);

//        if (ship.getOrientation() == Constants.Orientation.Horizontal) {
//            for (int y = c.y + 1; y < c.y + ship.getSize(); y++) {
//                if (field[c.x][y] != R.color.white){
//                    return false;
//                }
//            }
//        } else if (ship.getOrientation() == Constants.Orientation.Vertical) {
//            for (int x = c.x + 1; x < c.x + ship.getSize(); x++) {
//                if (field[x][c.y] != R.color.white){
//                    System.out.println("x :" + x + " y : " + c.y);
//                    return false;
//                }
//            }
//        }

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
        if (!canRotate(ship)) {
            return false;
        }

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
        refreshBattleField();

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
}
