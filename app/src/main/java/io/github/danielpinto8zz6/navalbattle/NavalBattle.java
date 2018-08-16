package io.github.danielpinto8zz6.navalbattle;

import android.app.Application;

import java.net.Socket;

import io.github.danielpinto8zz6.navalbattle.Network.Communication;
import io.github.danielpinto8zz6.navalbattle.activities.GameActivity;

public class NavalBattle extends Application {
    private Socket socket;
    private Communication communication;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    public Communication getCommunication() {
        return communication;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }
}
