package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.*;

public class PingHandler {
    private int progressiveIdPingRequest;
    private MessageBroker pingBroker;
    private Socket pingSocket;
    private InitialConnector initialConnector;
    private final int PING_TIMEOUT_SECONDS = 500;
    private final Duration timeout = Duration.ofSeconds(PING_TIMEOUT_SECONDS);
    private final int waitBetweenPingsMilliseconds = 2000;
    private int idUser;

    public PingHandler(InitialConnector initialConnector, MessageBroker pingBroker, Socket pingSocket) {
        this.initialConnector = initialConnector;
        this.pingBroker = pingBroker;
        this.pingSocket = pingSocket;
        this.progressiveIdPingRequest = 0;
    }

    /**
     * Starts the ping thread and returns
     *
     * Continuously sends ping messages to the server and sets connected field to false when
     * connection is no longer stable
     * This routine is entirely contained in the initial connector class as it doesn't need the usual type of
     * communication with the server, and thus won't use the Sender/Receiver classes
     */
    public void startPinging() {

        new Thread(() -> {

            OutputStream outStream;
            InputStream inStream;


            try {
                inStream = pingSocket.getInputStream();
                outStream = pingSocket.getOutputStream();
            } catch (IOException e){
                initialConnector.notifyNetworkError("Couldn't get input/output streams");
                return;
            }

            new Thread( () -> {
                while (initialConnector.isConnected()) {
                    try {
                        pingBroker.receive(inStream);
                    } catch (IOException e) {
                        initialConnector.notifyNetworkError("Ping routine couldn't receive message");
                    }
                }
            }).start();

            ExecutorService pingExecutor = Executors.newSingleThreadExecutor();

            Future<Void> handler;

            do{

                handler = pingExecutor.submit(() -> {

                    pingBroker.waitSyncMessage(); //operation to execute with timeout

                    return null; //no need for a return value
                });


                pingBroker.addToMessage(NetworkFieldEnum.ID_USER, idUser);
                pingBroker.addToMessage(NetworkFieldEnum.COMMAND, CommandEnum.PING);
                pingBroker.addToMessage(NetworkFieldEnum.ID_PING_REQUEST, increaseAndGetPingRequestId());

                try{
                    pingBroker.send(outStream);
                }
                catch (IOException e){
                    initialConnector.notifyNetworkError("Ping routine couldn't send message");
                    return;
                }

                //receive pong message
                try {
                    handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                    handler.cancel(true);
                    initialConnector.notifyNetworkError("Connection timed out");
                    pingBroker.flushFirstSyncMessage();
                    break;
                } catch (InterruptedException | ExecutionException e) {
                    handler.cancel(true);
                    initialConnector.notifyNetworkError("Ping routine interrupted");
                    pingBroker.flushFirstSyncMessage();
                    break;
                }

                //maybe encapsulate operations below

                int receivedIdUser = ApplicationHelper.getIntFromBrokerField(pingBroker.readField(NetworkFieldEnum.ID_USER));
                int receivedIdPingRequest = ApplicationHelper.getIntFromBrokerField(pingBroker.readField(NetworkFieldEnum.ID_PING_REQUEST));

                if(!CommandEnum.PONG
                        .equals(CommandEnum.fromObjectToEnum(pingBroker.readField(NetworkFieldEnum.COMMAND)))){
                    initialConnector.notifyNetworkError("ERROR: socket was not dedicated for ping routine");
                }
                else if (receivedIdUser != idUser){
                    initialConnector.notifyNetworkError("Server Error: identification failed");
                }
                else if (receivedIdPingRequest != progressiveIdPingRequest){
                    initialConnector.notifyNetworkError("Wrong Request Id. Expected: " + progressiveIdPingRequest + ". Received: " + receivedIdPingRequest);
                }

                pingBroker.flushFirstSyncMessage();

                //Ping only every 2 seconds
                try {
                    Thread.sleep(waitBetweenPingsMilliseconds);
                } catch (InterruptedException e) {
                    System.err.println("Ping: Interrupted, starting new ping request immediately");
                }
            } while (initialConnector.isConnected());

        }).start();

    }

    /**
     * Increments the request id by one and returns it
     * @return the new request id
     */
    public int increaseAndGetPingRequestId(){
        progressiveIdPingRequest++;
        return progressiveIdPingRequest;
    }

    public void assignIdUser(int idUser) {
        this.idUser = idUser;
    }
}
