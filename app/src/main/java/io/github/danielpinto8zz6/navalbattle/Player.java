package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private String avatarBase64;
    private Context context;
    private BattleField battleField;
    private boolean isDevice;
    private boolean isYourTurn;
    private boolean isEnemy;

    public Player(Context c) {
        context = c;

        battleField = new BattleField();

        setupProfile();

    }

    public Player(Context c, BattleField bf) {
        context = c;

        battleField = bf;

        setupProfile();
    }

    public Player(Context c, boolean isDevice, boolean isEnemy) {
        context = c;

        battleField = new BattleField();

        if (isDevice) {
            name = context.getResources().getString(R.string.computer);
            avatarBase64 = Utils.encodeTobase64(BitmapFactory.decodeResource(context.getResources(), R.drawable.computer));
        } else
            setupProfile();

        if (isEnemy) {
            name = context.getResources().getString(R.string.default_username);
            avatarBase64 = Utils.encodeTobase64(BitmapFactory.decodeResource(context.getResources(), R.drawable.opponent_avatar));
            battleField.setShowShips(false);
        }
    }

    public Player(Context c, String name, String avatarBase64) {
        context = c;

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

    private void setupProfile() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String playerAvatarBase64 = prefs.getString("avatar", "");

        String playerUsername = prefs.getString("key_username", "");

        if (playerAvatarBase64.length() > 0) {
            avatarBase64 = playerAvatarBase64;
        } else {
            avatarBase64 = Utils.encodeTobase64(BitmapFactory.decodeResource(context.getResources(), R.drawable.player_avatar));
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
        name = context.getResources().getString(R.string.computer);
        avatarBase64 = Utils.encodeTobase64(BitmapFactory.decodeResource(context.getResources(), R.drawable.computer));

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
}
