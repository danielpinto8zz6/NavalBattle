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
import android.widget.Toast;

import java.io.Serializable;

public class GameActivity extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView imageViewPlayer = (ImageView) findViewById(R.id.player_avatar);
        ImageView imageViewOpponent = (ImageView) findViewById(R.id.opponent_avatar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String base64 = prefs.getString("avatar", "");

        if (base64.length() > 0) {
            Bitmap bitmap = Utils.decodeBase64(base64);

            Drawable drawableAvatar = new BitmapDrawable(getResources(), bitmap);

            imageViewPlayer.setImageDrawable(drawableAvatar);
        }

        final GridView gridview = (GridView) findViewById(R.id.game_board);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int column = position % 8;
                int row = (int) Math.ceil(position / 8);
                Toast.makeText(GameActivity.this, "Line : " + row + " Column : " + column,
                        Toast.LENGTH_SHORT).show();
                ImageView imageView = (ImageView) v;
                imageView.setImageResource(R.drawable.player_avatar);
            }
        });

        int bordersSize = Utils.convertDpToPixel(32);
        int actionbarSize = Utils.convertDpToPixel(56);

        int width = getResources().getDisplayMetrics().widthPixels - bordersSize;
        int height = getResources().getDisplayMetrics().heightPixels - bordersSize;

        if (width < height)
            gridview.setColumnWidth(width / 8);
        else
            gridview.setColumnWidth((height - actionbarSize) / 8);


    }
}
