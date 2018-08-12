package io.github.danielpinto8zz6.navalbattle;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Ship implements Parcelable {
    //  Destroyed flag, Position
    private ArrayList<Coordinates> positions;
    private int type;
    private int rotation = 0;

    public Ship(int type, int rotation, ArrayList<Coordinates> positions) {
        this.type = type;
        this.positions = positions;
        this.rotation = rotation;
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

    public void destroy(int x, int y) {
        for (Coordinates position : positions) {
            if (position.getX() == x && position.getX() == y) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.positions);
        dest.writeInt(this.type);
        dest.writeInt(this.rotation);
    }

    protected Ship(Parcel in) {
        this.positions = in.createTypedArrayList(Coordinates.CREATOR);
        this.type = in.readInt();
        this.rotation = in.readInt();
    }

    public static final Creator<Ship> CREATOR = new Creator<Ship>() {
        @Override
        public Ship createFromParcel(Parcel source) {
            return new Ship(source);
        }

        @Override
        public Ship[] newArray(int size) {
            return new Ship[size];
        }
    };
}
