package io.github.danielpinto8zz6.navalbattle;

import android.app.Application;

import java.net.Socket;

public class NavalBattle extends Application {
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
