package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.network.*;
import it.polimi.ingsw.view.LobbyBean;

public class GetLobbyStatusHandler extends CommandHandler{

    public GetLobbyStatusHandler(){
        this.commandAccepted = CommandEnum.GET_LOBBY_STATUS;
    }
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {
        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        //Todo (possibly) : Implement this as an asynchronous reply.
        // if we want to have asynchronous lobby updates, we need to start this as a thread, which
        // can cause a lot of concurrency problems and wrong receival of messages
        /*
        Thread waitForLobbyUpdates = new Thread (() -> {
            if (lobbyWatcher != null) {
                oldBean = lobbyWatcher.getBean();
                while (lobbyWatcher.getBean().equals(oldBean)) {
                    try {
                        wait(); //Since executeCommand is synchronized, could this cause problems?
                    } catch (InterruptedException e) {
                        System.err.println("Wait interrupted, sending error");
                        notifyError(messageBroker, "Server error while fetching lobby data");
                    }
                    //Check for updates, don't answer immediately
                }
            } else {
                lobbyWatcher = new LobbyWatcher(parameters.getUserLobby());
            }
        }).start();
        */
/*
        if(lobbyWatcher == null){
            lobbyWatcher = new LobbyWatcher(parameters.getUserLobby());
            lobbyWatcher.update();
        }
*/
        //LobbyBean lobbyBean = lobbyWatcher.getBean();
        LobbyBean lobbyBean = parameters.getUserLobby().toBean();
        messageBroker.addToMessage(NetworkFieldEnum.BEAN_TYPE, lobbyBean.getBeanEnum());
        messageBroker.addToMessage(NetworkFieldEnum.BEAN, lobbyBean);
        notifySuccessfulOperation(messageBroker);

        return true;
    }
}
