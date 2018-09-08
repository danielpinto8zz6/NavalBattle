package io.github.danielpinto8zz6.navalbattle.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import io.github.danielpinto8zz6.navalbattle.Constants;
import io.github.danielpinto8zz6.navalbattle.NavalBattle;
import io.github.danielpinto8zz6.navalbattle.Network.Communication;
import io.github.danielpinto8zz6.navalbattle.Utils;
import io.github.danielpinto8zz6.navalbattle.game.BattleField;
import io.github.danielpinto8zz6.navalbattle.game.BattleFieldAdapter;
import io.github.danielpinto8zz6.navalbattle.game.Coordinates;
import io.github.danielpinto8zz6.navalbattle.game.DeviceAI;
import io.github.danielpinto8zz6.navalbattle.game.Game;
import io.github.danielpinto8zz6.navalbattle.R;
import io.github.danielpinto8zz6.navalbattle.game.Ship;

import static io.github.danielpinto8zz6.navalbattle.Constants.GameMode.Local;
import static io.github.danielpinto8zz6.navalbattle.Constants.GameMode.Network;
import static io.github.danielpinto8zz6.navalbattle.Utils.convertDpToPixel;
import static io.github.danielpinto8zz6.navalbattle.Utils.getFileContentFromInternalStorage;
import static io.github.danielpinto8zz6.navalbattle.Utils.writeFileOnInternalStorage;

public class GameActivity extends AppCompatActivity {
    private DeviceAI device;
    private Game game;
    private GridView gridViewPlayer;
    private GridView gridViewOpponent;
    private BattleFieldAdapter battleFieldAdapterPlayer;
    private BattleFieldAdapter battleFieldAdapterOpponent;
    private ImageView imageViewOpponent;
    private TextView opponentName;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Constants.GameMode mode = (Constants.GameMode) getIntent().getSerializableExtra("game_mode");

        if (mode == Local) {
            game = new Game(this);
            device = new DeviceAI(this);
            game.getPlayer().setYourTurn(true);
        } else {
            game = new Game(this, mode);

            if (savedInstanceState == null) {
                Communication communication = new Communication(getNavalBattle().getSocket(), this);
                getNavalBattle().setCommunication(communication);
                communication.sendProfile(game.getPlayer().getName(), game.getPlayer().getAvatarBase64());
                if (Objects.requireNonNull(getIntent().getExtras()).getBoolean("is_server"))
                    game.getPlayer().setYourTurn(true);
            }
        }

        if (savedInstanceState != null) {
            game = (Game)
                    savedInstanceState.getSerializable("game_obj");
        } else {
            game.getPlayer().setBattleField((BattleField) getIntent().getSerializableExtra("battle_field"));
        }

        setupToolbar();

        setupGrids();

        if (game.getPlayer().isYourTurn()) {
            gridViewOpponent.setBackground(getDrawable(R.drawable.grid_border_green));
            gridViewPlayer.setBackgroundColor(0x00000000);
        } else {
            if (game.getOpponent().getBattleField().getShipsHitten() == 3) {
                game.getOpponent().getBattleField().setShipsHitten(0);
                wannaMoveShip();
            } else {
                gridViewOpponent.setBackgroundColor(0x00000000);
                gridViewPlayer.setBackground(getDrawable(R.drawable.grid_border_red));
                if (game.getMode() == Local) {
                    device.play(game);
                }
            }
        }

        battleFieldAdapterPlayer.notifyDataSetChanged();
        battleFieldAdapterOpponent.notifyDataSetChanged();

        if (mode == Network && savedInstanceState == null) {
            Log.d("Naval Battle", "waiting opponent to connect");
            getCommunication().sendMessage("");
            dialog = new ProgressDialog(GameActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getResources().getString(R.string.waiting_for_opponent));
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("game_obj", game);
    }

    @SuppressWarnings({"SuspiciousNameCombination", "UnusedAssignment"})
    private void setupGrids() {
        int bordersSize = convertDpToPixel(36);
        int actionbarSize = convertDpToPixel(56);

        int height = (getResources().getDisplayMetrics().heightPixels - bordersSize) - actionbarSize;

        gridViewPlayer = findViewById(R.id.gridview_player);
        int count = 8;
        gridViewPlayer.setNumColumns(count);

        gridViewOpponent = findViewById(R.id.gridview_opponent);
        gridViewOpponent.setNumColumns(count);

        int imageDimension;
        if (getResources().getConfiguration().orientation == 1) {
            imageDimension = (height / 2) / count;
            ViewGroup.LayoutParams layoutParams = gridViewPlayer.getLayoutParams();
            layoutParams.width = (height / 2);
            layoutParams.height = (height / 2);
            gridViewPlayer.setLayoutParams(layoutParams);

            layoutParams = gridViewOpponent.getLayoutParams();
            layoutParams.width = (height / 2);
            layoutParams.height = (height / 2);
            gridViewOpponent.setLayoutParams(layoutParams);
        } else {
            imageDimension = height / count;
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

        AdapterView.OnItemClickListener onItemClickListener;
        gridViewOpponent.setOnItemClickListener(onItemClickListener = new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (!game.getPlayer().isYourTurn()) return;

                int x = position % 8;
                int y = (int) Math.ceil(position / 8);

                if (game.getOpponent().getBattleField().attackPosition(new Coordinates(x, y))) {
                    game.getOpponent().getBattleField().addGivenShot(new Coordinates(x, y));
                    battleFieldAdapterOpponent.notifyDataSetChanged();
                    game.getPlayer().setShots(game.getPlayer().getShots() + 1);
                }

                if (game.isGameOver()) {
                    gameOver();
                }

                if (game.getPlayer().getShots() >= 3) {
                    game.getPlayer().setShots(0);

                    if (game.getOpponent().getBattleField().getDestroyedShip() != null) {
                        game.getOpponent().getBattleField().removeShip(game.getOpponent().getBattleField().getDestroyedShip());
                        game.getOpponent().getBattleField().setDestroyedShip(null);

                        vibrate();
                        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                        gridViewOpponent.startAnimation(shake);
                    }

                    game.getOpponent().getBattleField().clearGivenShots();
                    battleFieldAdapterOpponent.notifyDataSetChanged();

                    if (game.getOpponent().getBattleField().getShipsHitten() == 3 && !game.getOpponent().getBattleField().isShipsHitten()) {
                        game.getOpponent().getBattleField().setShipsHitten(0);
//                        game.getPlayer().setYourTurn(false);
//                        gridViewOpponent.setBackgroundColor(0x00000000);
                        wannaMoveShip();
                    } else {
                        game.getOpponent().getBattleField().setShipsHitten(0);
                        opponentPlay();
                    }
                }
            }
        });

        gridViewPlayer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                if (!game.getPlayer().isMovingShip()) return true;

                int x = position % 8;
                int y = (int) Math.ceil(position / 8);

                final int field[][] = game.getPlayer().getBattleField().getField();

                if (field[x][y] != R.drawable.ship && field[x][y] != R.drawable.ship_destroyed)
                    return true;

                final Ship ship = game.getPlayer().getBattleField().getShipAtPosition(new Coordinates(x, y));

                if (ship == null) return true;

                if (ship.isHitten()) {
                    Toast.makeText(GameActivity.this, R.string.choose_healthy_ship, Toast.LENGTH_LONG).show();
                    return true;
                }

                game.getPlayer().getBattleField().setSelectedShip(ship);

                battleFieldAdapterPlayer.notifyDataSetChanged();

                Toast.makeText(GameActivity.this, R.string.click_to_move, Toast.LENGTH_SHORT).show();

                gridViewPlayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        int x = position % 8;
                        int y = (int) Math.ceil(position / 8);

                        game.getPlayer().getBattleField().setSelectedShip(null);

                        if (game.getPlayer().getBattleField().move(ship, new Coordinates(x, y))) {
                            Toast.makeText(GameActivity.this, R.string.cant_move_ship, Toast.LENGTH_SHORT).show();
                        } else {
                            if (game.getMode() == Network)
                                getCommunication().sendPlayerBattleField(game.getPlayer().getBattleField());
                        }

                        gridViewPlayer.setOnItemClickListener(null);

                        game.getPlayer().setMovingShip(false);

                        battleFieldAdapterPlayer.notifyDataSetChanged();

                        opponentPlay();

                    }
                });

                return true;
            }
        });
    }

    private void wannaMoveShip() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        game.getPlayer().setMovingShip(true);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        game.getPlayer().setMovingShip(false);
                        opponentPlay();
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage(getString(R.string.you_can_move_ship)).setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setTitle(getString(R.string.tree_successful_shots))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }

    public void gameOver() {
        battleFieldAdapterOpponent.notifyDataSetChanged();
        battleFieldAdapterPlayer.notifyDataSetChanged();
        gridViewPlayer.setBackgroundColor(0x00000000);
        gridViewOpponent.setBackgroundColor(0x00000000);

        Drawable drawable = new BitmapDrawable(getResources(), game.getWinner().getAvatar());

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(GameActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(GameActivity.this);
        }
        builder.setCancelable(false);
        builder.setTitle(getResources().getString(R.string.game_over))
                .setMessage(game.getWinner().getName() + " " + getResources().getString(R.string.has_won))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(drawable)
                .show();

        saveGameToHistory();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Set player & opponent avatar/username

        ImageView imageViewPlayer = findViewById(R.id.player_avatar);
        imageViewOpponent = findViewById(R.id.opponent_avatar);

        TextView playerName = findViewById(R.id.player_name);
        opponentName = findViewById(R.id.opponent_name);

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

    public void disconnected() {
        Toast.makeText(GameActivity.this, R.string.opponent_disconnected, Toast.LENGTH_LONG).show();
        game.setMode(Local);
        game.getOpponent().setAvatarBase64(Utils.encodeTobase64(BitmapFactory.decodeResource(getResources(), R.drawable.player_avatar)));
        game.getOpponent().setName(getResources().getString(R.string.device));
        imageViewOpponent.setImageDrawable(getResources().getDrawable(R.drawable.device));
        opponentName.setText(getResources().getString(R.string.device));
        getCommunication().stop();
        getNavalBattle().setCommunication(null);
        device = new DeviceAI(this);
        opponentPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isFinishing())
            if (game.getMode() == Network)
                if (getCommunication() != null) {
                    Log.d(getString(R.string.app_name), "Closing communication");
                    getCommunication().stop();
                }
    }

    private NavalBattle getNavalBattle() {
        return ((NavalBattle) getApplication());
    }

    private Communication getCommunication() {
        return getNavalBattle().getCommunication();
    }

    public void playerPlay(BattleField battleField) {
        game.getPlayer().setBattleField(battleField);
        battleFieldAdapterPlayer.setBattleField(battleField);
        game.getPlayer().getBattleField().setShowShips(true);
        game.getPlayer().setYourTurn(true);
        game.getOpponent().setYourTurn(false);
        gridViewOpponent.setBackground(getDrawable(R.drawable.grid_border_green));
        gridViewPlayer.setBackgroundColor(0x00000000);
        battleFieldAdapterPlayer.notifyDataSetChanged();
    }

    public void playerPlay() {
        game.getPlayer().setYourTurn(true);
        game.getOpponent().setYourTurn(false);
        gridViewOpponent.setBackground(getDrawable(R.drawable.grid_border_green));
        gridViewPlayer.setBackgroundColor(0x00000000);
        battleFieldAdapterPlayer.notifyDataSetChanged();
    }

    private void opponentPlay() {
        game.getPlayer().setYourTurn(false);
        game.getOpponent().setYourTurn(true);
        gridViewOpponent.setBackgroundColor(0x00000000);
        gridViewPlayer.setBackground(getDrawable(R.drawable.grid_border_red));

        if (game.getMode() == Local) {
            device.play(game);
        } else {
            getCommunication().sendOpponentBattleField(game.getOpponent().getBattleField());
        }
    }

    public void setOpponentProfile(String name, String avatar) {
        imageViewOpponent.setImageBitmap(Utils.decodeBase64(avatar));
        opponentName.setText(name);
        game.getOpponent().setName(name);
        game.getOpponent().setAvatarBase64(avatar);
    }

    public void setOpponentBattleField(BattleField battleField) {
        game.getOpponent().setBattleField(battleField);
        battleFieldAdapterOpponent.setBattleField(battleField);
        game.getOpponent().getBattleField().setShowShips(false);
        battleFieldAdapterOpponent.notifyDataSetChanged();

        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public void connect() {
        getCommunication().sendProfile(game.getPlayer().getName(), game.getPlayer().getAvatarBase64());
        getCommunication().sendPlayerBattleField(game.getPlayer().getBattleField());
    }

    private void saveGameToHistory() {
        String gameSave = null;
        try {
            gameSave = game.getGameSave();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String history = getFileContentFromInternalStorage(this, "history");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        ArrayList<String> games = gson.fromJson(history, type);
        if (games == null) games = new ArrayList<>();

        games.add(gameSave);

        String json = gson.toJson(games);
        writeFileOnInternalStorage(this, "history", json);

        Log.d("Naval battle", "Saving game...");
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Objects.requireNonNull(v).vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            Objects.requireNonNull(v).vibrate(500);
        }
    }
}
