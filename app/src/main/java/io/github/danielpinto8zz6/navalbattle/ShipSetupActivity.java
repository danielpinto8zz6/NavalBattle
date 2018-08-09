package io.github.danielpinto8zz6.navalbattle;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class ShipSetupActivity extends AppCompatActivity {

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_setup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        player = new Player(this);

        GridView playerGridView = (GridView) findViewById(R.id.player_game_board);
        playerGridView.setAdapter(player.getBattleField().getAdapter());

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

        playerGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int x = position % 8;
                int y = (int) Math.ceil(position / 8);

                int field[][] = player.getBattleField().getField();

                if (field[x][y] != R.color.ship) {
                    return;
                }

                Ship ship = player.getBattleField().getShipAtPosition(new Coordinates(x, y));

                if (ship == null) {
                    return;
                }

                if (!player.getBattleField().rotateShip(ship)) {
                    Toast.makeText(ShipSetupActivity.this, "Can't rotate!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toast.makeText(ShipSetupActivity.this, "Click on ship to rotate!", Toast.LENGTH_LONG).show();
    }

}
