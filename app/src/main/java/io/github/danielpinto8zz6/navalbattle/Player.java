package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;

public class Player implements Parcelable {
    private String name;
    private String avatarBase64;
    private BattleField battleField;
    private boolean isDevice;
    private boolean isYourTurn;
    private boolean isEnemy;

    public Player(Context c) {
        battleField = new BattleField();

        setupProfile(c);
    }

    public Player(Context c, boolean isDevice, boolean isEnemy) {
        battleField = new BattleField();

        if (isDevice) {
            name = c.getResources().getString(R.string.computer);
            avatarBase64 = Utils.encodeTobase64(BitmapFactory.decodeResource(c.getResources(), R.drawable.computer));
        } else
            setupProfile(c);

        if (isEnemy) {
            name = c.getResources().getString(R.string.default_username);
            avatarBase64 = Utils.encodeTobase64(BitmapFactory.decodeResource(c.getResources(), R.drawable.opponent_avatar));
            battleField.setShowShips(false);
        }
    }

    public Player(String name, String avatarBase64) {
        battleField = new BattleField();

        this.isEnemy = true;
        this.name = name;
        this.avatarBase64 = avatarBase64;
        battleField.setShowShips(false);
    }


    public String getName() {
        return name;
    }

    public String getAvatarBase64() {
        return avatarBase64;
    }

    public BattleField getBattleField() {
        return battleField;
    }

    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }

    private void setupProfile(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

        String playerAvatarBase64 = prefs.getString("avatar", "");

        String playerUsername = prefs.getString("key_username", "");

        if (playerAvatarBase64.length() > 0) {
            avatarBase64 = playerAvatarBase64;
        } else {
            avatarBase64 = Utils.encodeTobase64(BitmapFactory.decodeResource(c.getResources(), R.drawable.player_avatar));
        }

        if (playerUsername.length() > 0) {
            name = playerUsername;
        } else {
            name = "Player";
        }
    }

    public boolean isDevice() {
        return isDevice;
    }

    public void setDevice(boolean device) {
        isDevice = device;
    }

    public boolean isYourTurn() {
        return isYourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        isYourTurn = yourTurn;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    public Bitmap getAvatar() {
        return Utils.decodeBase64(avatarBase64);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatarBase64 (String avatarBase64) {
        this.avatarBase64 = avatarBase64;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.avatarBase64);
        dest.writeParcelable(this.battleField, flags);
        dest.writeByte(this.isDevice ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isYourTurn ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEnemy ? (byte) 1 : (byte) 0);
    }

    protected Player(Parcel in) {
        this.name = in.readString();
        this.avatarBase64 = in.readString();
        this.battleField = in.readParcelable(BattleField.class.getClassLoader());
        this.isDevice = in.readByte() != 0;
        this.isYourTurn = in.readByte() != 0;
        this.isEnemy = in.readByte() != 0;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel source) {
            return new Player(source);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}