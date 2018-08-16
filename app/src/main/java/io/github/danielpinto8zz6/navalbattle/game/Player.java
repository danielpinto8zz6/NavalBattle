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
    private boolean isDevice;
    private boolean isYourTurn = false;
    private boolean isEnemy = false;

    public Player(Context c) {
        battleField = new BattleField();

        setupProfile(c);
    }

    public Player(Context c, BattleField bf) {
        battleField = bf;

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
            battleField.setShowShips(false);
            name = c.getResources().getString(R.string.opponent);
            avatarBase64 = Utils.encodeTobase64(BitmapFactory.decodeResource(c.getResources(), R.drawable.opponent_avatar));
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
        if (!isEnemy) {
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
        } else {
            if (!isEnemy) {
                avatarBase64 = Utils.encodeTobase64(BitmapFactory.decodeResource(c.getResources(), R.drawable.opponent_avatar));
                name = c.getResources().getString(R.string.opponent);
            }
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

    public void setAvatarBase64(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
    }

    public void setName(String name) {
        this.name = name;
    }
}
