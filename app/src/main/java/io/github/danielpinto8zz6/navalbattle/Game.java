package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import io.github.danielpinto8zz6.navalbattle.Constants.GameMode;

public class Game implements Parcelable {
    private Player player;
    private Player opponent;
    private GameMode mode;

    public Game(Context context) {
        player = new Player(context);
        opponent = new Player(context, true, true);
    }

    public Game(Context context, GameMode mode) {
        this.mode = mode;

        player = new Player(context);
        opponent = new Player(context, false, true);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.player, flags);
        dest.writeParcelable(this.opponent, flags);
        dest.writeInt(this.mode == null ? -1 : this.mode.ordinal());
    }

    protected Game(Parcel in) {
        this.player = in.readParcelable(Player.class.getClassLoader());
        this.opponent = in.readParcelable(Player.class.getClassLoader());
        int tmpMode = in.readInt();
        this.mode = tmpMode == -1 ? null : GameMode.values()[tmpMode];
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}