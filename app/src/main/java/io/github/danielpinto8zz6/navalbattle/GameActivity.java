package io.github.danielpinto8zz6.navalbattle;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private ImageAdapter playerImageAdapter;
    private ImageAdapter opponentImageAdapter;
    private GridView playerGridView;
    private GridView opponentGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setupToolbar();

        setupGrids();

        if (savedInstanceState != null) {
            playerImageAdapter.setBoard(savedInstanceState.getIntArray("player_board"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray("player_board", playerImageAdapter.getBoard());
    }

    public void setupGrids() {
        playerGridView = (GridView) findViewById(R.id.player_game_board);
        playerGridView.setAdapter(playerImageAdapter = new ImageAdapter(this));

        playerGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                playerImageAdapter.changeResource(position, R.drawable.player_avatar);
            }
        });

        playerImageAdapter.changeResource(4, R.drawable.ic_launcher_background);

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

        opponentGridView = (GridView) findViewById(R.id.opponent_game_board);
        opponentGridView.setAdapter(opponentImageAdapter = new ImageAdapter(this));

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String playerAvatarBase64 = prefs.getString("avatar", "");
        // String opponentAvatarBase64 = getOpponentAvatarBase64();

        String playerUsername = prefs.getString("key_username", "");

        if (playerAvatarBase64.length() > 0) {
            Bitmap bitmap = Utils.decodeBase64(playerAvatarBase64);

            Drawable drawableAvatar = new BitmapDrawable(getResources(), bitmap);

            imageViewPlayer.setImageDrawable(drawableAvatar);
        }

        if (playerUsername.length() > 0) {
            TextView playerName = (TextView) findViewById(R.id.player_name);

            playerName.setText(playerUsername);
        }
    }
}
