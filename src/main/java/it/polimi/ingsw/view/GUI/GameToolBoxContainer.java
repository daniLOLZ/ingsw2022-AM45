package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.beans.IslandGroupBean;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.view.GUI.handlingToolbox.*;

import java.util.ArrayList;
import java.util.List;

public class GameToolBoxContainer {

    private AssistantHandlingToolbox assistantHandlingToolbox;
    private BoardHandlingToolbox boardHandlingToolbox;
    private CloudHandlingToolbox cloudHandlingToolbox;
    private IslandHandlingToolbox islandHandlingToolbox;
    private List<CharacterCardHandlingToolbox> characterCardHandlingToolboxes;

    public GameToolBoxContainer(int numAssistants, int entranceStudents, int numTables, int numClouds, int numCharacterCards){
        assistantHandlingToolbox = new AssistantHandlingToolbox(numAssistants);
        boardHandlingToolbox = new BoardHandlingToolbox(entranceStudents, numTables);
        cloudHandlingToolbox = new CloudHandlingToolbox(numClouds);
        islandHandlingToolbox = new IslandHandlingToolbox();

        characterCardHandlingToolboxes = new ArrayList<>();

        for (int character = 0; character < numCharacterCards; character++) {
            characterCardHandlingToolboxes.add(new CharacterCardHandlingToolbox());
        }
    }

    public void updateIslandGroups(List<IslandGroupBean> islands){
        islandHandlingToolbox.updateIslandGroups(islands);
    }

    public void setNumStudentsOnCharacterCard(int index, int numStudents){
        characterCardHandlingToolboxes.get(index).setNumStudents(numStudents);
    }

    public List<HandlingToolbox> getAll() {
        List<HandlingToolbox> handlingToolboxes = new ArrayList<>();

        handlingToolboxes.add(assistantHandlingToolbox);
        handlingToolboxes.add(boardHandlingToolbox);
        handlingToolboxes.add(cloudHandlingToolbox);
        handlingToolboxes.add(islandHandlingToolbox);
        handlingToolboxes.addAll(characterCardHandlingToolboxes);

        return handlingToolboxes;
    }

    public void allowCommand(CommandEnum command, ClientSender resourceProvider){

        for (HandlingToolbox toolbox: getAll()) toolbox.allowCommand(command, resourceProvider);
    }

    public void clearCommands(){
        for (HandlingToolbox toolbox : getAll()) {
            for (CommandEnum command : CommandEnum.values()) toolbox.disableCommand(command);
        }
    }


    public AssistantHandlingToolbox getAssistantHandlingToolbox() {
        return assistantHandlingToolbox;
    }

    public BoardHandlingToolbox getBoardHandlingToolbox() {
        return boardHandlingToolbox;
    }

    public CloudHandlingToolbox getCloudHandlingToolbox() {
        return cloudHandlingToolbox;
    }

    public IslandHandlingToolbox getIslandHandlingToolbox() {
        return islandHandlingToolbox;
    }

    public List<CharacterCardHandlingToolbox> getCharacterCardHandlingToolboxes() {
        return characterCardHandlingToolboxes;
    }
}



