package io.github.danielpinto8zz6.navalbattle;

import android.os.Parcel;
import android.os.Parcelable;

public class Coordinates implements Parcelable {
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

        if (o instanceof Coordinates) {
            Coordinates c = (Coordinates) o;
            if (c.x == x && c.y == y) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.x);
        dest.writeInt(this.y);
        dest.writeByte(this.isAttacked ? (byte) 1 : (byte) 0);
    }

    protected Coordinates(Parcel in) {
        this.x = in.readInt();
        this.y = in.readInt();
        this.isAttacked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Coordinates> CREATOR = new Parcelable.Creator<Coordinates>() {
        @Override
        public Coordinates createFromParcel(Parcel source) {
            return new Coordinates(source);
        }

        @Override
        public Coordinates[] newArray(int size) {
            return new Coordinates[size];
        }
    };
}
