package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.ApplicationHelper;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.MessageBroker;
import it.polimi.ingsw.network.NetworkFieldEnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.*;

public class PongHandler {

    private final ClientHandler clientHandler;
    private final MessageBroker pingBroker;
    private final Socket pingSocket;
    public static final int PONG_TIMEOUT_SECONDS = 5;
    private final Duration timeout = Duration.ofSeconds(PONG_TIMEOUT_SECONDS);
    private boolean pongStarted;

    public PongHandler(ClientHandler clientHandler, MessageBroker pingBroker, Socket pingSocket) {
        this.clientHandler = clientHandler;
        this.pingBroker = pingBroker;
        this.pingSocket = pingSocket;
        pongStarted = false;
    }

    /**
     * Starts the pong routine in a new thread, returning afterwards
     */
    public void startPonging() {

        if(pongStarted) return;

        new Thread(() -> {

            InputStream clientInput;
            OutputStream clientOutput;

            try {
                clientInput = pingSocket.getInputStream();
                clientOutput = pingSocket.getOutputStream();
            } catch (IOException e) {
                clientHandler.connectionLostAlert("Error obtaining streams\n" + e.getMessage());
                return;
            }

            ExecutorService pingExecutor = Executors.newSingleThreadExecutor();

            new Thread(() -> {
                while (clientHandler.isConnected()) {
                    try {
                        pingBroker.receive(clientInput);
                    } catch (IOException e) {
                        clientHandler.connectionLostAlert("Pong routine couldn't receive message");
                        return;
                    }
                }
            }).start();

            Future<Void> handler;

            while (clientHandler.isConnected()) {

                handler = pingExecutor.submit(() -> {

                    pingBroker.waitSyncMessage(); //operation to execute with timeout

                    return null; //no need for a return value

                });

                // as soon as this routine starts, we need to immediately wait 5 seconds
                // since the client must initiate the ping messages immediately

                try {
                    handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    handler.cancel(true);
                    clientHandler.connectionLostAlert("Connection timed out");
                    pingBroker.flushFirstSyncMessage();
                    break;
                } catch (InterruptedException | ExecutionException e) {
                    handler.cancel(true);
                    clientHandler.connectionLostAlert("Pong routine interrupted");
                    pingBroker.flushFirstSyncMessage();
                    break;
                }


                if (!CommandEnum.PING
                        .equals(CommandEnum.fromObjectToEnum(pingBroker.readField(NetworkFieldEnum.COMMAND)))) {
                    clientHandler.connectionLostAlert("ERROR: socket was not dedicated for pong routine");
                } else {
                    int receivedIdUser = ApplicationHelper.getIntFromBrokerField(pingBroker.readField(NetworkFieldEnum.ID_USER));
                    int receivedIdPingRequest = ApplicationHelper.getIntFromBrokerField(pingBroker.readField(NetworkFieldEnum.ID_PING_REQUEST));

//                    System.out.printf("Received ping #%d from user %d\n", receivedIdPingRequest, receivedIdUser);

                    pingBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.PONG);
                    pingBroker.addToMessage(NetworkFieldEnum.ID_PING_REQUEST, receivedIdPingRequest);
                    pingBroker.addToMessage(NetworkFieldEnum.ID_USER, receivedIdUser);

                    try {
                        pingBroker.send(clientOutput);
                    } catch (IOException e) {
                        clientHandler.connectionLostAlert("Couldn't send the pong command");
                    }
                }

                pingBroker.flushFirstSyncMessage();
            }
        }).start();

        pongStarted = true;
    }
}
