package io.github.danielpinto8zz6.navalbattle.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import io.github.danielpinto8zz6.navalbattle.activities.MainActivity;

import static io.github.danielpinto8zz6.navalbattle.Constants.socketServerPORT;

public class Server {
    private MainActivity activity;
    private ServerSocket serverSocket;
    private Socket socket;
    private Thread thread;

    public Server(final MainActivity activity) {
        this.activity = activity;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(socketServerPORT);
                    socket = serverSocket.accept();
                    serverSocket.close();
                    serverSocket = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    socket = null;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (socket == null)
                            activity.ErrorConnecting();
                        else {
                            activity.getNavalBattle().setSocket(socket);
                            activity.Connected(true);
                        }
                    }
                });
            }
        });
        thread.start();
    }

    public void stop() {
        thread.interrupt();

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
