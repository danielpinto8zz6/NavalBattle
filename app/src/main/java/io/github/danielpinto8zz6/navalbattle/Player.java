package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

public class Player {
    private BattleField battleField;
    private String name;
    private Drawable avatar;
    private Context context;

    public Player(Context c) {
        context = c;

        battleField = new BattleField(context);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String playerAvatarBase64 = prefs.getString("avatar", "");

        String playerUsername = prefs.getString("key_username", "");

        if (playerAvatarBase64.length() > 0) {
            Bitmap bitmap = Utils.decodeBase64(playerAvatarBase64);
            avatar = new BitmapDrawable(context.getResources(), bitmap);
        } else {
            avatar = context.getResources().getDrawable(R.drawable.player_avatar);
        }

        if (playerUsername.length() > 0) {
            name = playerUsername;
        } else {
            name = "Player";
        }

    }

    public String getName() {
        return name;
    }

    public Drawable getAvatar() {
        return avatar;
    }

    public BattleField getBattleField() {
        return battleField;
    }

    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }
}
