package io.github.danielpinto8zz6.navalbattle.Network;

import java.net.Socket;

import io.github.danielpinto8zz6.navalbattle.activities.MainActivity;

import static io.github.danielpinto8zz6.navalbattle.Constants.socketServerPORT;

public class Client {
    private Thread thread;
    private Socket socket;
    private String strIP;
    private MainActivity activity;

    public Client(final MainActivity activity, final String strIP) {
        this.strIP = strIP;
        this.activity = activity;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(strIP, socketServerPORT);
                } catch (Exception e) {
                    socket = null;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (socket == null)
                            activity.ErrorConnecting();
                        else {
                            activity.getNavalBattle().setSocket(socket);
                            activity.Connected(false);
                        }
                    }
                });
                return;
            }
        });
        thread.start();
    }
}
