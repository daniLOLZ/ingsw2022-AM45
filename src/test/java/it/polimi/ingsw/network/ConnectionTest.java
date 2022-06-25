//Old network tests
/*
package it.polimi.ingsw.network;

import it.polimi.ingsw.network.client.ClientNetworkManager;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Server;

import java.util.Scanner;

//SERVER
public class ConnectionTest {
    ClientHandler serverHandler;
    static Server server;


    public static void main(String[] args) {
        server = new Server(54321);
        server.startServer();
    }
}

//CLIENT ONE
    class CLIENT {
        static ClientNetworkManager client;

        public static void main(String[] args) {
            client = new ClientNetworkManager("127.0.0.1", 54321);
            client.login("127.0.0.1", 54321, "FRANCO");

            client.sendGameModePreference(1, 2);
            client.sendReadyStatus(true);
            boolean started = false;
            while(client.isConnected()){
                System.out.println("Press ENTER to request START GAME");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
                started = client.startGame();
                System.out.println(started);
            }

        }
    }


//CLIENT TWO
    class AWFUL_CLIENT {
        static ClientNetworkManager client;

        public static void main(String[] args) {
            client = new ClientNetworkManager("127.0.0.1", 54321);
            boolean connected = client.login("127.0.0.1", 54321, "FRANCO");
            while(! connected)
                connected = client.login("127.0.0.1", 54321, "MARIO");

            boolean commandOK;
            commandOK = client.sendGameModePreference(1, 2);
            commandOK = client.sendReadyStatus(true);
            commandOK = client.startGame();

            if(!commandOK)
                System.out.println("No Host");

            boolean started = false;
            while(client.isConnected()){
                System.out.println("Press ENTER to request START GAME");
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();
                started = client.startGame();
                System.out.println(started);
            }

        }


    }


 */
