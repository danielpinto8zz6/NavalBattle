package io.github.danielpinto8zz6.navalbattle.game;

import android.content.Context;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClient {

    private static final int PORT = 9988;
    private static final int TIMEOUT = 60000;

    private ServerSocket serverSocket;
    private Socket socket;
    private OutputStream os;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String ip;
    private Context context;
    private boolean isServer;

    public ServerClient(Context context, String ip, boolean isServer) {
        this.ip = ip;
        this.context = context;
        this.isServer = isServer;
    }

    public boolean init() {
        if (isServer) {
            return initServer();
        }

        return initClient();
    }

    private boolean initClient() {
        try {
            socket = new Socket(ip, PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (socket == null) {
            return false;
        }
        return true;
    }

    private boolean initServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(TIMEOUT);
            socket = serverSocket.accept();
            serverSocket.close();
            serverSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void initStreams() throws IOException {
        os = socket.getOutputStream();
        output = new ObjectOutputStream(os);
        input = new ObjectInputStream(socket.getInputStream());
    }

    public void terminate() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBytes(byte[] myByteArray, int start, int len) throws IOException {
        if (len < 0)
            throw new IllegalArgumentException("Negative length not allowed");
        if (start < 0 || start >= myByteArray.length)
            throw new IndexOutOfBoundsException("Out of bounds: " + start);


        DataOutputStream dos = new DataOutputStream(os);

        dos.writeInt(len);
        if (len > 0) {
            dos.write(myByteArray, start, len);
        }
    }

    public byte[] readBytes() throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        int len = dis.readInt();
        byte[] data = new byte[len];
        if (len > 0) {
            dis.readFully(data);
        }
        return data;
    }


    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public void setInput(ObjectInputStream input) {
        this.input = input;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
