package io.github.danielpinto8zz6.navalbattle.activities;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.github.danielpinto8zz6.navalbattle.Constants;
import io.github.danielpinto8zz6.navalbattle.NavalBattle;
import io.github.danielpinto8zz6.navalbattle.Network.Communication;
import io.github.danielpinto8zz6.navalbattle.Utils;
import io.github.danielpinto8zz6.navalbattle.game.BattleField;
import io.github.danielpinto8zz6.navalbattle.game.BattleFieldAdapter;
import io.github.danielpinto8zz6.navalbattle.Coordinates;
import io.github.danielpinto8zz6.navalbattle.game.DeviceAI;
import io.github.danielpinto8zz6.navalbattle.game.Game;
import io.github.danielpinto8zz6.navalbattle.game.GameInterface;
import io.github.danielpinto8zz6.navalbattle.R;

import static io.github.danielpinto8zz6.navalbattle.Constants.GameMode.Local;
import static io.github.danielpinto8zz6.navalbattle.Constants.GameMode.Network;
import static io.github.danielpinto8zz6.navalbattle.Utils.convertDpToPixel;

public class GameActivity extends AppCompatActivity implements GameInterface {
    private DeviceAI device;
    private Game game;
    private int shots = 0;
    private int count = 8;
    private GridView gridViewPlayer;
    private GridView gridViewOpponent;
    private BattleFieldAdapter battleFieldAdapterPlayer;
    private BattleFieldAdapter battleFieldAdapterOpponent;
    private int imageDimension;
    private Constants.GameMode mode;
    private Communication communication;
    private ImageView imageViewPlayer;
    private ImageView imageViewOpponent;
    private TextView playerName;
    private TextView opponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mode = (Constants.GameMode) getIntent().getSerializableExtra("game_mode");

        if (mode == Local) {
            game = new Game(this);
            device = new DeviceAI(game, this);
            game.getPlayer().setYourTurn(true);
        } else {
            game = new Game(this, mode);

            NavalBattle navalBattle = (NavalBattle) getApplicationContext();
            communication = new Communication(navalBattle.getSocket(), this);

            if (getIntent().getExtras().getBoolean("is_server"))
                game.getPlayer().setYourTurn(true);
        }

        if (savedInstanceState != null)

        {
            game = (Game)
                    savedInstanceState.getSerializable("game_obj");
        } else {
            BattleField bf = (BattleField) getIntent().getSerializableExtra("battle_field");
            if (bf != null)
                game.getPlayer().setBattleField(bf);
        }

        setupToolbar();

        setupGrids();

        if (game.getPlayer().isYourTurn()) {
            gridViewOpponent.setBackground(

                    getDrawable(R.drawable.grid_border_green));
            gridViewPlayer.setBackgroundColor(0x00000000);
        } else {
            gridViewPlayer.setBackground(

                    getDrawable(R.drawable.grid_border_red));
            gridViewOpponent.setBackgroundColor(0x00000000);
        }

        battleFieldAdapterPlayer.notifyDataSetChanged();
        battleFieldAdapterOpponent.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("game_obj", game);
    }

    public void setupGrids() {
        int bordersSize = convertDpToPixel(36);
        int actionbarSize = convertDpToPixel(56);

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
                    shots = 0;
                    opponentPlay();
                }
            }
        });
    }

    private void opponentPlay() {
        game.getPlayer().setYourTurn(false);
        game.getOpponent().setYourTurn(true);
        gridViewOpponent.setBackgroundColor(0x00000000);
        gridViewPlayer.setBackground(getDrawable(R.drawable.grid_border_red));

        if (mode == Local) {
            device.play();
        } else {

        }

    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set player & opponent avatar/username

        imageViewPlayer = (ImageView) findViewById(R.id.player_avatar);
        imageViewOpponent = (ImageView) findViewById(R.id.opponent_avatar);

        playerName = (TextView) findViewById(R.id.player_name);
        opponentName = (TextView) findViewById(R.id.opponent_name);

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

    public void errorReceiving() {
        Toast.makeText(GameActivity.this, "Error", Toast.LENGTH_SHORT);
    }

    @Override
    public void disconnected() {
        Toast.makeText(GameActivity.this, R.string.opponent_disconnected, Toast.LENGTH_LONG).show();
        game.setMode(Local);
        mode = Local;
        game.getOpponent().setDevice(true);
        game.getOpponent().setAvatarBase64(Utils.encodeTobase64(BitmapFactory.decodeResource(getResources(), R.drawable.player_avatar)));
        game.getOpponent().setName(getResources().getString(R.string.computer));
        imageViewOpponent.setImageDrawable(getResources().getDrawable(R.drawable.computer));
        opponentName.setText(getResources().getString(R.string.computer));

        device = new DeviceAI(game, this);
        opponentPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mode == Network)
            communication.stop();
    }
}
