package io.deltawave.poke.pokemongo.service.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by will on 8/2/16.
 */
public class ServerConnectionService {

    private Client client;
    private Kryo kryo;

    private ArrayList<ServerResponseListener> serverResponseListeners;

    public ServerConnectionService() {
        serverResponseListeners = new ArrayList<>();

        client = new Client();
        client.addListener(new Listener() {
            @Override
            public void received (Connection connection, Object object) {
                for(ServerResponseListener srl: serverResponseListeners) {
                    srl.onReceivedResponse(object);
                }
            }

            @Override
            public void connected(Connection connection) {
                System.out.println("Connected to server.");
            }

            @Override
            public void disconnected(Connection connection) {
                System.out.println("Disconnected from server.");
                reconnect();
            }
        });

        this.kryo = client.getKryo();

        startServerThread();
    }

    public void startServerThread() {
        System.out.println("Connecting to server...");
        Thread serverThread = new Thread() {
            @Override
            public void run() {
                client.start();
                try {
                    client.connect(5000, "will.red", 54555, 54777);
                } catch (IOException e) {
                    System.out.println("Could not connect.");
                    reconnect();
                }
            }
        };
        serverThread.start();
    }

    public void reconnect() {
        Thread reconnectThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try{
                    System.out.println("Reconnecting...");
                    client.reconnect(5000);
                } catch(IOException e) {
                    System.out.println("Could not reconnect.");
                    reconnect();
                }
            }
        };
        reconnectThread.start();
    }

    public void addServerResponseListener(ServerResponseListener srl) {
        serverResponseListeners.add(srl);
    }

    public void removeServerResponseListener(ServerResponseListener srl) {
        serverResponseListeners.remove(srl);
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public void sendTCP(Object o) {
        client.sendTCP(o);
    }

    public void registerClass(Class c) {
        kryo.register(c);
    }
}
