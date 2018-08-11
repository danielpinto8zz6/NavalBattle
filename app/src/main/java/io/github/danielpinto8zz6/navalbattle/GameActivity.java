package io.github.danielpinto8zz6.navalbattle;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class GameActivity extends AppCompatActivity implements Serializable {
    private BattleField playerBattleField;
    private BattleField opponentBattleField;
    private BattleFieldAdapter playerBattleFieldAdapter;
    private BattleFieldAdapter opponentBattleFieldAdapter;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game = new Game(this, Constants.GameMode.Local);

        playerBattleField = game.getPlayerBattleField();


        Intent i = getIntent();
        BattleField bf = (BattleField) i.getSerializableExtra("battle_field");
        game.setPlayerBattleField(bf);
        playerBattleField = bf;

        opponentBattleField = game.getOpponentBattleField();

        setupToolbar();

        setupGrids();

        if (savedInstanceState != null) {
//            playerBattleField.setField(savedInstanceState.getIntegerArrayList("player_grid"));
//            opponentBattleField.setField(savedInstanceState.getIntegerArrayList("opponent_grid"));
        }

        playerBattleFieldAdapter.notifyDataSetChanged();
        opponentBattleFieldAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        outState.putIntegerArrayList("player_grid", playerBattleField.getField());
//        outState.putIntegerArrayList("opponent_grid", opponentBattleField.getField());
    }

    public void setupGrids() {
        GridView playerGridView = (GridView) findViewById(R.id.player_game_board);
        playerGridView.setAdapter(playerBattleFieldAdapter = new BattleFieldAdapter(this, playerBattleField));

        int bordersSize = Utils.convertDpToPixel(32);
        int actionbarSize = Utils.convertDpToPixel(56);

        int width = getResources().getDisplayMetrics().widthPixels - bordersSize;
        int height = (getResources().getDisplayMetrics().heightPixels - bordersSize) - actionbarSize;

        if (width < height) {
            // Center gridview
            playerGridView.setPadding(width - (height / 2) - Utils.convertDpToPixel(32), 0, 0, 0);

            playerGridView.setColumnWidth((height / 2) / 8);
        } else {
            playerGridView.setPadding((width / 2) - height, 0, 0, 0);

            playerGridView.setColumnWidth(height / 8);
        }

        final GridView opponentGridView = (GridView) findViewById(R.id.opponent_game_board);
        opponentGridView.setAdapter(opponentBattleFieldAdapter = new BattleFieldAdapter(this, opponentBattleField));

        opponentGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int x = position % 8;
                int y = (int) Math.ceil(position / 8);

                opponentBattleField.attackPosition(new Coordinates(x, y));

                opponentBattleFieldAdapter.notifyDataSetChanged();
            }
        });

        if (width < height) {
            opponentGridView.setPadding(width - (height / 2) - Utils.convertDpToPixel(32), 0, 0, 0);

            opponentGridView.setColumnWidth((height / 2) / 8);
        } else {
            opponentGridView.setColumnWidth(height / 8);
        }
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set player & opponent avatar/username

        ImageView imageViewPlayer = (ImageView) findViewById(R.id.player_avatar);
        ImageView imageViewOpponent = (ImageView) findViewById(R.id.opponent_avatar);

        TextView playerName = (TextView) findViewById(R.id.player_name);
        TextView opponentName = (TextView) findViewById(R.id.opponent_name);

        imageViewPlayer.setImageDrawable(getPlayer().getAvatar());
        imageViewOpponent.setImageDrawable(getOpponent().getAvatar());

        playerName.setText(getPlayer().getName());
        opponentName.setText(getOpponent().getName());
    }

    public Player getPlayer() {
        return game.getPlayer();
    }

    public Opponent getOpponent() {
        return game.getOpponent();
    }
}
