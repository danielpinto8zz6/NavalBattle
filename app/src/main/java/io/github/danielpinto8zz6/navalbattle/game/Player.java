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
    private boolean isYourTurn = false;
    private boolean canMoveShip = false;

    public Player(Context c) {
        battleField = new BattleField();

        setupPlayerProfile(c);
    }

    public Player(Context c, boolean isDevice, boolean isEnemy) {
        battleField = new BattleField();

        if (isDevice) {
            name = c.getResources().getString(R.string.computer);
            avatarBase64 = Utils.encodeTobase64(BitmapFactory.decodeResource(c.getResources(), R.drawable.computer));
        } else
            setupPlayerProfile(c);

        if (isEnemy)
            battleField.setShowShips(false);

        if (isEnemy && !isDevice) {
            name = c.getResources().getString(R.string.opponent);
            avatarBase64 = Utils.encodeTobase64(BitmapFactory.decodeResource(c.getResources(), R.drawable.opponent_avatar));
        }
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

    private void setupPlayerProfile(Context c) {
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
            name = c.getResources().getString(R.string.player);
        }
    }

    public boolean isYourTurn() {
        return isYourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        isYourTurn = yourTurn;
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

    public boolean getCanMoveShip() {
        return !canMoveShip;
    }

    public void setCanMoveShip(boolean canMoveShip) {
        this.canMoveShip = canMoveShip;
    }
}
