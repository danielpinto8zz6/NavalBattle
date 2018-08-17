package io.github.danielpinto8zz6.navalbattle.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import io.github.danielpinto8zz6.navalbattle.R;
import io.github.danielpinto8zz6.navalbattle.game.BattleField;
import io.github.danielpinto8zz6.navalbattle.game.BattleFieldAdapter;

import static io.github.danielpinto8zz6.navalbattle.Utils.convertDpToPixel;

public class HistoryActivity extends AppCompatActivity {
    private int imageDimension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String game = prefs.getString("saved_games", "");

        Gson gson = new Gson();
        JSONObject json = null;
        try {
            json = new JSONObject(game);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BattleField battleFieldPlayer = new BattleField();
        BattleField battleFieldOpponent = new BattleField();

        GridView gridViewPlayer = findViewById(R.id.gridview_player);
        GridView gridViewOpponent = findViewById(R.id.gridview_opponent);

        gridViewPlayer.setAdapter(new BattleFieldAdapter(this, battleFieldPlayer, convertDpToPixel(190) / 8));
        gridViewOpponent.setAdapter(new BattleFieldAdapter(this, battleFieldOpponent, convertDpToPixel(190) / 8));

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

        BattleFieldAdapter battleFieldAdapterPlayer = (BattleFieldAdapter) gridViewPlayer.getAdapter();
        BattleFieldAdapter battleFieldAdapterOpponent = (BattleFieldAdapter) gridViewOpponent.getAdapter();

        battleFieldAdapterPlayer.notifyDataSetChanged();
        battleFieldAdapterOpponent.notifyDataSetChanged();
    }
}
