package it.polimi.ingsw.network.commandHandler;

import it.polimi.ingsw.controller.GameRuleEnum;
import it.polimi.ingsw.network.*;
import it.polimi.ingsw.network.connectionState.InLobby;

public class PlayGameHandler extends CommandHandler{

    public PlayGameHandler(){
        commandAccepted = CommandEnum.PLAY_GAME;
    }

    /**
     * The user requests to play a game with the given rules
     */
    @Override
    public boolean executeCommand(MessageBroker messageBroker, ClientHandlerParameters parameters) throws UnexecutableCommandException {

        CommandEnum readCommand = CommandEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.COMMAND));
        if(!checkHandleable(readCommand, commandAccepted)) throw new UnexecutableCommandException();

        GameRuleEnum rules = GameRuleEnum.fromObjectToEnum(messageBroker.readField(NetworkFieldEnum.GAME_RULE));

        parameters.setUserLobby(ActiveLobbies.assignLobby(rules));
        parameters.getUserLobby().addPlayer(parameters.getIdUser());
        parameters.setConnectionState(new InLobby());
        notifySuccessfulOperation(messageBroker);

        return true;
    }
}
