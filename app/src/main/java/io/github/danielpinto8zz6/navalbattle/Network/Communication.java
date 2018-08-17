package io.github.danielpinto8zz6.navalbattle.Network;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import io.github.danielpinto8zz6.navalbattle.Constants;
import io.github.danielpinto8zz6.navalbattle.R;
import io.github.danielpinto8zz6.navalbattle.activities.GameActivity;
import io.github.danielpinto8zz6.navalbattle.game.BattleField;
import io.github.danielpinto8zz6.navalbattle.game.Opponent;
import io.github.danielpinto8zz6.navalbattle.game.Player;

public class Communication {
    private Socket socket;
    private PrintWriter output = null;
    private BufferedReader input = null;
    private Thread thread;
    private GameActivity activity;
    private boolean connected = false;

    public Communication(final Socket socket, final GameActivity activity) {
        this.socket = socket;
        this.activity = activity;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    input = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));
                    output = new PrintWriter(socket.getOutputStream());
                    while (!Thread.currentThread().isInterrupted()) {
                        String read = input.readLine();
                        if (!connected) {
                            connected = true;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    activity.connect();
                                }
                            });
                        }
                        if (read == null) {
                            Log.d(activity.getResources().getString(R.string.app_name), "Opponent disconnected");

                            stop();

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    activity.disconnected();
                                }
                            });
                        }
                        Log.d(activity.getResources().getString(R.string.app_name), "Message received");
                        decodeMessage(read);
                    }
                } catch (Exception e) {
                    Log.d(activity.getResources().getString(R.string.app_name), e.getMessage());
                }
            }
        });
        thread.start();
    }

    public void stop() {
        thread.interrupt();
        try {
            if (output != null)
                output.close();
            if (input != null)
                input.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(final String message) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(activity.getResources().getString(R.string.app_name), "Sending message");
                    output.println(message);
                    output.flush();
                } catch (Exception e) {
                    Log.d(activity.getResources().getString(R.string.app_name), "Error sending message");
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void sendProfile(String name, String avatarBase64) {
        JSONObject json = new JSONObject();
        try {
            json.put("command", "profile_update");
            json.put("name", name);
            json.put("avatar", avatarBase64);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage(json.toString());
    }

    public void decodeMessage(String message) {
        JSONObject json = null;
        try {
            json = new JSONObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (json == null) return;

        String command = null;

        try {
            command = json.getString("command");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (command == null) return;

        switch (command) {

            case "opponent_battle_field_update":
                Gson gsonO = new Gson();
                try {
                    final BattleField battleField = gsonO.fromJson(json.getString("opponent_battle_field"), BattleField.class);
                    System.out.println(json.getString("opponent_battle_field"));
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.playerPlay(battleField);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "player_battle_field_update":
                Gson gsonP = new Gson();
                try {
                    final int[][] field = gsonP.fromJson(json.getString("player_battle_field"), int[][].class);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.setOpponentBattleField(field);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "profile_update":
                String name = "";
                String avatarBase64 = "";
                try {
                    name = json.getString("name");
                    avatarBase64 = json.getString("avatar");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String n = name;
                final String a = avatarBase64;

                if (name != "" && avatarBase64 != "") {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.setOpponentProfile(n, a);
                        }
                    });
                }
                break;
        }

    }

    public void sendOpponentBattleField(BattleField battleField) {
        Gson gson = new GsonBuilder().create();
        JSONObject json = new JSONObject();

        try {
            json.put("command", "opponent_battle_field_update");
            json.put("opponent_battle_field", gson.toJson(battleField));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage(json.toString());
    }

    public void sendPlayerBattleField(int[][] field) {
        Gson gson = new GsonBuilder().create();
        JSONObject json = new JSONObject();

        try {
            json.put("command", "player_battle_field_update");
            json.put("player_battle_field", gson.toJson(field));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage(json.toString());
    }
}
