package io.github.danielpinto8zz6.navalbattle.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import io.github.danielpinto8zz6.navalbattle.R;
import io.github.danielpinto8zz6.navalbattle.game.BattleField;
import io.github.danielpinto8zz6.navalbattle.game.BattleFieldAdapter;

import static io.github.danielpinto8zz6.navalbattle.Utils.convertDpToPixel;
import static io.github.danielpinto8zz6.navalbattle.Utils.getArrayList;

public class HistoryActivity extends AppCompatActivity {
    private int imageDimension;
    private LinearLayout linearLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        linearLayout = findViewById(R.id.linearlayout);

        ArrayList<String> history = getArrayList(this, "game_history");

        if (history != null)
            for (String game : history)
                setupGrid(game);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setupGrid(String game) {
        Gson gson = new Gson();
        JSONObject json = null;
        try {
            json = new JSONObject(game);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BattleField battleFieldPlayer = new BattleField();
        BattleField battleFieldOpponent = new BattleField();

        String battleFieldPlayerStr = "";
        try {
            battleFieldPlayerStr = json.getString("player_battlefield");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String battleFieldOpponentStr = "";
        try {
            battleFieldOpponentStr = json.getString("opponent_battlefield");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        battleFieldPlayer.setField(gson.fromJson(battleFieldPlayerStr, int[][].class));
        battleFieldOpponent.setField(gson.fromJson(battleFieldOpponentStr, int[][].class));

        GridView gridViewPlayer = new GridView(this);
        gridViewPlayer.setNumColumns(8);
        gridViewPlayer.setGravity(Gravity.CENTER);
        gridViewPlayer.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

        GridView gridViewOpponent = new GridView(this);
        gridViewOpponent.setNumColumns(8);
        gridViewOpponent.setGravity(Gravity.CENTER);
        gridViewOpponent.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

        int bordersSize = convertDpToPixel(32);

        int width = getResources().getDisplayMetrics().widthPixels - bordersSize;

        imageDimension = ((width / 2) / 8);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = (width / 2);
        layoutParams.height = (width / 2);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        gridViewPlayer.setLayoutParams(layoutParams);

        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = (width / 2);
        layoutParams.height = (width / 2);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        gridViewOpponent.setLayoutParams(layoutParams);

        gridViewPlayer.setAdapter(new BattleFieldAdapter(this, battleFieldPlayer, imageDimension));
        gridViewOpponent.setAdapter(new BattleFieldAdapter(this, battleFieldOpponent, imageDimension));

        TextView textView = new TextView(this);
        try {
            textView.setText(json.getString("player_name") + " : " + json.getString("opponent_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        textView.setBackgroundColor(getResources().getColor(R.color.textview_background));
        textView.setPadding(8, 8, 8, 8);
        textView.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_WindowTitle);
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setPadding(0, 0, 0, convertDpToPixel(16));
        relativeLayout.addView(gridViewPlayer);
        relativeLayout.addView(gridViewOpponent);
        relativeLayout.addView(textView);

        linearLayout.addView(relativeLayout);
    }
}