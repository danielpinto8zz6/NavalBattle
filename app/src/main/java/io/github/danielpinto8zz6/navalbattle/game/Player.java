package io.github.danielpinto8zz6.navalbattle.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import java.io.Serializable;

import io.github.danielpinto8zz6.navalbattle.R;
import io.github.danielpinto8zz6.navalbattle.Utils;

public class Player implements Serializable {
    private String name;
    private String avatarBase64;
    private BattleField battleField;
    private int shots = 0;
    private boolean movingShip = false;
    private boolean yourTurn;

    public Player (String name, String avatarBase64) {
        this.name = name;
        this.avatarBase64 = avatarBase64;
    }

    public Player() {
        battleField = new BattleField();
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

    public Bitmap getAvatar() {
        return Utils.decodeBase64(avatarBase64);
    }

    public void setAvatarBase64(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShots() {
        return shots;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public boolean isMovingShip() {
        return movingShip;
    }

    public void setMovingShip(boolean movingShip) {
        this.movingShip = movingShip;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }
}
