package io.github.danielpinto8zz6.navalbattle;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements GameInterface {
    private DeviceAI device;
    private Game game;
    private int shots = 0;
    private Handler mHandler;
    private int count = 8;
    private GridView gridViewPlayer;
    private GridView gridViewOpponent;
    private BattleFieldAdapter battleFieldAdapterPlayer;
    private BattleFieldAdapter battleFieldAdapterOpponent;
    private int imageDimension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game = new Game(this);

        if (savedInstanceState != null) {
            game = (Game)
                    savedInstanceState.getSerializable("game_obj");
        } else {
            BattleField bf = (BattleField) getIntent().getSerializableExtra("battle_field");
            if (bf != null)
                game.getPlayer().setBattleField(bf);
        }

        setupToolbar();

        setupGrids();

        mHandler = new Handler(Looper.getMainLooper()) {
        };

        device = new DeviceAI(game, this);
        Thread thread = new Thread(device);
        thread.start();

        GameActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                battleFieldAdapterPlayer.notifyDataSetChanged();
            }
        });

        battleFieldAdapterPlayer.notifyDataSetChanged();
        battleFieldAdapterOpponent.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("game_obj", game);
    }

    public void setupGrids() {
        int bordersSize = Utils.convertDpToPixel(36);
        int actionbarSize = Utils.convertDpToPixel(56);

        int height = (getResources().getDisplayMetrics().heightPixels - bordersSize) - actionbarSize;

        gridViewPlayer = findViewById(R.id.gridview_player);
        gridViewPlayer.setNumColumns(count);

        gridViewOpponent = findViewById(R.id.gridview_opponent);
        gridViewOpponent.setNumColumns(count);

        if (getResources().getConfiguration().orientation == 1) {
            imageDimension = (int) ((height / 2) / count);
            ViewGroup.LayoutParams layoutParams = gridViewPlayer.getLayoutParams();
            layoutParams.width = (height / 2);
            layoutParams.height = (height / 2);
            gridViewPlayer.setLayoutParams(layoutParams);

            layoutParams = gridViewOpponent.getLayoutParams();
            layoutParams.width = (height / 2);
            layoutParams.height = (height / 2);
            gridViewOpponent.setLayoutParams(layoutParams);
        } else {
            imageDimension = (int) (height / count);
            ViewGroup.LayoutParams layoutParams = gridViewPlayer.getLayoutParams();
            layoutParams.width = height;
            layoutParams.height = height;
            gridViewPlayer.setLayoutParams(layoutParams);

            layoutParams = gridViewOpponent.getLayoutParams();
            layoutParams.width = height;
            layoutParams.height = height;
            gridViewOpponent.setLayoutParams(layoutParams);
        }

        gridViewPlayer.setAdapter(battleFieldAdapterPlayer = new BattleFieldAdapter(this, game.getPlayer().getBattleField(), imageDimension));

        gridViewOpponent.setAdapter(battleFieldAdapterOpponent = new BattleFieldAdapter(this, game.getOpponent().getBattleField(), imageDimension));

        gridViewOpponent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (!game.getPlayer().isYourTurn()) return;

                int x = position % 8;
                int y = (int) Math.ceil(position / 8);

                if (game.getOpponent().getBattleField().attackPosition(new Coordinates(x, y)))
                    shots++;

                battleFieldAdapterOpponent.notifyDataSetChanged();

                if (shots >= 3) {
                    game.getPlayer().setYourTurn(false);
                    game.getOpponent().setYourTurn(true);
                    shots = 0;
                    gridViewOpponent.setBackgroundColor(0x00000000);
                    gridViewPlayer.setBackground(getDrawable(R.drawable.grid_border_red));
                    return;
                }
            }
        });
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

        imageViewPlayer.setImageBitmap(game.getPlayer().getAvatar());
        imageViewOpponent.setImageBitmap(game.getOpponent().getAvatar());

        playerName.setText(game.getPlayer().getName());
        opponentName.setText(game.getOpponent().getName());
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.sure_want_to_exit)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GameActivity.this.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public void dataChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                battleFieldAdapterPlayer.notifyDataSetChanged();
                if (game.getPlayer().isYourTurn()) {
                    gridViewOpponent.setBackground(getDrawable(R.drawable.grid_border_green));
                    gridViewPlayer.setBackgroundColor(0x00000000);
                }
            }
        });
    }
}
