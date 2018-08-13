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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ShipSetupActivity extends AppCompatActivity {
    BattleField battleField;
    private BattleFieldAdapter adapter;
    AdapterView.OnItemClickListener onItemClickListener;
    private int imageDimension;
    private int count = 8;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship_setup);

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

        setupGrid();

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

    private void setupGrid() {
        int bordersSize = Utils.convertDpToPixel(32);
        int actionbarSize = Utils.convertDpToPixel(56);

        int width = getResources().getDisplayMetrics().widthPixels - bordersSize;
        int height = (getResources().getDisplayMetrics().heightPixels - bordersSize) - actionbarSize;

        gridView = findViewById(R.id.gridview_ship_setup);
        gridView.setNumColumns(count);

        if (getResources().getConfiguration().orientation == 1) {
            imageDimension = (int) (width / count);
            ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = width;
            gridView.setLayoutParams(layoutParams);
        } else {
            imageDimension = (int) (height / count);
            ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
            layoutParams.width = height;
            layoutParams.height = height;
            gridView.setLayoutParams(layoutParams);
        }

        adapter = new BattleFieldAdapter(this, battleField, imageDimension);
        gridView.setAdapter(adapter);

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
