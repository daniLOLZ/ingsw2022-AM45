package it.polimi.ingsw.view;

import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.network.ClientNetworkManager;
import it.polimi.ingsw.network.CommandEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an interface to what the client will see, it contains a NetworkManager which
 * takes requests from the View and sends them to the server
 */
public abstract class UserInterface {
    protected List<GameElementBean> beans;
    protected List<CommandEnum> availableCommands;
    protected ClientNetworkManager networkManager;
    protected String nickname; // Should we keep this here?

    /**
     * Network-less constructor, used for testing
     */
    public UserInterface(){
        beans = new ArrayList<>();
        availableCommands = new ArrayList<>();
    }
    public UserInterface(String hostname, int port){
        beans = new ArrayList<>();
        availableCommands = new ArrayList<>();
        networkManager = new ClientNetworkManager(hostname, port);
    }

    public void addBean(GameElementBean bean){
        beans.add(bean);
    }

    public GameElementBean removeBean(int index){
        return beans.remove(index);
    }

    public void clearBeans(){
        beans.clear();
    }

    public void addCommand(CommandEnum command){
        availableCommands.add(command);
    }

    public CommandEnum removeCommand(int index){
        //todo remove infinite recursion
        return removeCommand(index);
    }

    public void clearCommand(){
        availableCommands.clear();
    }

    public abstract void showWelcomeScreen();

    public abstract void showLoginScreen();

    public abstract void showGameruleSelection();

    public abstract void showLobby();

    public abstract void showTowerAndWizardSelection();

    public abstract void showGameInterface();

    /**
     * Initializes and starts the game interface
     */
    public abstract void startInterface();

}
