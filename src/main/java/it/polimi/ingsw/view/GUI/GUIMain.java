package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.network.ClientNetworkManager;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.view.UserInterface;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;

public class GUIMain implements UserInterface {

    private List<GameElementBean> beans;
    private List<CommandEnum> availableCommands;
    private ClientNetworkManager networkManager;
    private String chosenNickname;

    /**
     * Creates a new GUI and calls the method to create a new NetworkManager
     * @param hostname the name of the host
     * @param port the port to connect to
     */
    public GUIMain(String hostname, int port){
        beans = new ArrayList<>();
        availableCommands = new ArrayList<>();
        networkManager = new ClientNetworkManager(hostname, port);
    }

    @Override
    public void showWelcomeScreen() {

    }

    @Override
    public void showLoginScreen() {

    }

    @Override
    public void showGameruleSelection() {

    }

    @Override
    public void showLobby() {

    }

    @Override
    public void showTowerAndWizardSelection() {

    }

    @Override
    public void showGameInterface() {

    }

    @Override
    public void startInterface() {

    }

    @Override
    public void addBean(GameElementBean bean) {
        beans.add(bean);
    }

    @Override
    public GameElementBean removeBean(int index) {
        return beans.remove(index);
    }

    @Override
    public void clearBeans() {
        beans.clear();
    }

    @Override
    public void addCommand(CommandEnum command) {
        availableCommands.add(command);
    }

    @Override
    public CommandEnum removeCommand(int index) {
        return availableCommands.remove(index);
    }

    @Override
    public void clearCommands() {
        availableCommands.clear();
    }

}
