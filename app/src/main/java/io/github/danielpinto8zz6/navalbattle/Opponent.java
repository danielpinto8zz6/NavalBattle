package io.github.danielpinto8zz6.navalbattle;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class Opponent {
    private String name;
    private Drawable avatar;
    private BattleField battleField;

    public Opponent(Context context) {
        battleField = new BattleField();
        battleField.setShowShips(false);

        name = context.getResources().getString(R.string.computer);
        avatar = context.getResources().getDrawable(R.drawable.computer);

    }

    public String getName() {
        return name;
    }

    public Drawable getAvatar() {
        return avatar;
    }

    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }

    public BattleField getBattleField() {
        return battleField;
    }
}
