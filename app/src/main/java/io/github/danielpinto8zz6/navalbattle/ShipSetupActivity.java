package io.github.danielpinto8zz6.navalbattle;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class ShipSetupActivity extends AppCompatActivity {
    BattleField battleField;
    BattleFieldAdapter adapter;
    AdapterView.OnItemClickListener onItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_setup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_setup_complete);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShipSetupActivity.this.finish();
                Intent game = new Intent(getApplicationContext(), GameActivity.class);
                game.putExtra("battle_field", battleField);
                startActivity(game);
            }
        });

        if (savedInstanceState != null)
            battleField = (BattleField) savedInstanceState.getSerializable("battlefield");
        else battleField = new BattleField();

        final GridView gridView = (GridView) findViewById(R.id.player_game_board);
        gridView.setAdapter(adapter = new BattleFieldAdapter(this, battleField));

        int bordersSize = Utils.convertDpToPixel(32);
        int actionbarSize = Utils.convertDpToPixel(56);

        int width = getResources().getDisplayMetrics().widthPixels - bordersSize;
        int height = (getResources().getDisplayMetrics().heightPixels - bordersSize) - actionbarSize;

        if (width < height) {
            // Center gridview
            gridView.setPadding(width - (height / 2) - Utils.convertDpToPixel(32), 0, 0, 0);

            gridView.setColumnWidth((height / 2) / 8);
        } else {
            gridView.setPadding((width / 2) - height, 0, 0, 0);

            gridView.setColumnWidth(height / 8);
        }

        gridView.setOnItemClickListener(onItemClickListener = new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int x = position % 8;
                int y = (int) Math.ceil(position / 8);

                int field[][] = battleField.getField();

                if (field[x][y] != R.color.ship) return;

                Ship ship = battleField.getShipAtPosition(new Coordinates(x, y));

                if (ship == null) return;

                if (!battleField.rotateShip(ship)) {
                    Toast.makeText(ShipSetupActivity.this, "Can't rotate!", Toast.LENGTH_SHORT).show();
                    return;
                }

                adapter.notifyDataSetChanged();
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                int x = position % 8;
                int y = (int) Math.ceil(position / 8);

                final int field[][] = battleField.getField();

                if (field[x][y] != R.color.ship) return true;

                final Ship ship = battleField.getShipAtPosition(new Coordinates(x, y));

                if (ship == null) return true;

                battleField.setSelectedShip(ship);

                adapter.notifyDataSetChanged();

                Toast.makeText(ShipSetupActivity.this, "Click on cell to move the ship", Toast.LENGTH_SHORT).show();

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {

                        int x = position % 8;
                        int y = (int) Math.ceil(position / 8);

                        if (!battleField.move(ship, new Coordinates(x, y))) {
                            Toast.makeText(ShipSetupActivity.this, "Can't move to that position", Toast.LENGTH_SHORT).show();
                        }

                        battleField.setSelectedShip(null);

                        adapter.notifyDataSetChanged();

                        // Re-enable clicklistener for rotate...
                        gridView.setOnItemClickListener(onItemClickListener);

                        return;
                    }
                });

                return true;
            }
        });

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_ship_setup_layout), "Click on ship to rotate!\nLong press to move!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.sure_want_to_exit)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ShipSetupActivity.this.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("battlefield", battleField);
    }
}
