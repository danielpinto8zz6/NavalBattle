package io.github.danielpinto8zz6.navalbattle.Network;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import io.github.danielpinto8zz6.navalbattle.activities.GameActivity;

public class Communication {
    private Socket socket;
    private PrintWriter output = null;
    private BufferedReader input = null;
    private Thread thread;
    private GameActivity activity;

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
                        if (read == null) {
                            Log.d("Naval Battle", "Opponent disconnected");

                            stop();

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    activity.disconnected();
                                }
                            });
                        }
                        Log.d("Naval Battle", "Message received : " + read);
                    }
                } catch (Exception e) {
                    Log.d("Naval Battle", e.getMessage());
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
                    Log.d("Naval Battle", "Sending message");
                    output.println(message);
                    output.flush();
                } catch (Exception e) {
                    Log.d("Naval Battle", "Error sending message");
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void setActivity(GameActivity activity) {
        this.activity = activity;
    }
}
