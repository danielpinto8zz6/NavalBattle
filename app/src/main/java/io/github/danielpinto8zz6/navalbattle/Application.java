package io.github.danielpinto8zz6.navalbattle;

import android.app.Activity;

public class Application extends android.app.Application {
    private static Application obj;
    private ServerClient connection;
    private Activity activity;

    public Application() {
        obj = this;
    }

    public static ServerClient getConnection() {
        return obj.connection;
    }

    public static void setConnection(ServerClient connection) {
        obj.connection = connection;
    }

    public static void setCurrentActivity(Activity activity) {
        obj.activity = activity;
    }
}

