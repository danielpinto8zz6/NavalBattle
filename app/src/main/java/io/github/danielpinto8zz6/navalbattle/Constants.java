package io.github.danielpinto8zz6.navalbattle;

public class Constants {
    public static final int REQUEST_PICK_AVATAR = 333;
    public static final int REQUEST_IMAGE_CAPTURE = 444;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1234;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 5678;

    enum OpponentType {
        Computer, Player
    }

    public enum GameMode {
        Local,
        Network
    }

    public enum BattleFieldType {
        Player,
        Opponent
    }

    public enum Orientation {
        Horizontal,
        Vertical
    }
}
