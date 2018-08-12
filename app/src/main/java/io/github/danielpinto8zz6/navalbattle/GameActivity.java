package io.github.danielpinto8zz6.navalbattle;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private BattleFieldAdapter playerBattleFieldAdapter;
    private BattleFieldAdapter opponentBattleFieldAdapter;
    private DeviceAI device;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game = new Game(this);

        if (savedInstanceState != null) {
            game.getPlayer().setBattleField((BattleField) getIntent().getParcelableExtra("battle_field_player"));
            game.getOpponent().setBattleField((BattleField) getIntent().getParcelableExtra("battle_field_opponent"));
        } else {
            BattleField bf = (BattleField) getIntent().getParcelableExtra("battle_field");
            if (bf != null)
                game.getPlayer().setBattleField(bf);
            game.getOpponent().setDevice(true);
            game.getOpponent().setAvatarBase64(Utils.encodeTobase64(BitmapFactory.decodeResource(getResources(), R.drawable.computer)));
            game.getOpponent().setName(getResources().getString(R.string.computer));
        }

        setupToolbar();

        setupGrids();

        device = new DeviceAI(game);
        Thread thread = new Thread(device);
        thread.start();


        playerBattleFieldAdapter.notifyDataSetChanged();
        opponentBattleFieldAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("battle_field_player", game.getPlayer().getBattleField());
        outState.putParcelable("battle_field_opponent", game.getOpponent().getBattleField());
    }

    public void setupGrids() {
        GridView playerGridView = (GridView) findViewById(R.id.player_game_board);
        playerGridView.setAdapter(playerBattleFieldAdapter = new BattleFieldAdapter(this, game.getPlayer().getBattleField()));

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
        opponentGridView.setAdapter(opponentBattleFieldAdapter = new BattleFieldAdapter(this, game.getOpponent().getBattleField()));

        opponentGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int x = position % 8;
                int y = (int) Math.ceil(position / 8);

                game.getOpponent().getBattleField().attackPosition(new Coordinates(x, y));

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
}
