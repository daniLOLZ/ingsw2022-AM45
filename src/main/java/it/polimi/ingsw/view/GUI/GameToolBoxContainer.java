package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.beans.AdvancedIslandGroupBean;
import it.polimi.ingsw.model.beans.IslandGroupBean;
import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.view.GUI.handlingToolbox.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The GameToolBoxContainer class encapsulates the HandlingToolboxes logic
 */
public class GameToolBoxContainer {

    private AssistantHandlingToolbox assistantHandlingToolbox;
    private BoardHandlingToolbox boardHandlingToolbox;
    private CloudHandlingToolbox cloudHandlingToolbox;
    private IslandHandlingToolbox islandHandlingToolbox;
    private List<CharacterCardHandlingToolbox> characterCardHandlingToolboxes;
    private HelpingToolBox helpingToolbox;
    private CharacterCardSelection selections;

    public GameToolBoxContainer(int numAssistants, int entranceStudents, int numTables, int numClouds, int numCharacterCards, CharacterCardSelection selections){
        assistantHandlingToolbox = new AssistantHandlingToolbox(numAssistants);
        boardHandlingToolbox = new BoardHandlingToolbox(entranceStudents, numTables);
        cloudHandlingToolbox = new CloudHandlingToolbox(numClouds);
        islandHandlingToolbox = new IslandHandlingToolbox();
        helpingToolbox = new HelpingToolBox();
        this.selections = selections;

        boardHandlingToolbox.setSelectionsContainer(selections);
        helpingToolbox.setSelectionsContainer(selections);

        characterCardHandlingToolboxes = new ArrayList<>();

        for (int character = 0; character < numCharacterCards; character++) {
            characterCardHandlingToolboxes.add(new CharacterCardHandlingToolbox());
            characterCardHandlingToolboxes.get(character).setSelectionsContainer(selections);
        }
    }

    /**
     * Proxy method for updateIslandGroups in IslandHandlingToolbox.
     * @param islands The list containing all the relevant information about each island
     */
    public void updateIslandGroups(List<IslandGroupBean> islands){
        islandHandlingToolbox.updateIslandGroups(islands);
    }

    /**
     * Proxy method for updateAdvancedIslandGroups in IslandHandlingToolbox.
     * @param islands The list containing all the relevant information about each island
     */
    public void updateAdvancedIslandGroups(List<AdvancedIslandGroupBean> islands){
        islandHandlingToolbox.updateAdvancedIslandGroups(islands);
    }

    /**
     * Proxy method for setCardInfo in CharacterCardHandlingToolbox
     * @param index The id of the Character Card this Toolbox is assigned to
     * @param numStudents The number of students on the Character Card
     */
    public void setCharacterCardInfo(int index, int numStudents){
        characterCardHandlingToolboxes.get(index).setCardInfo(index, numStudents);
    }

    /**
     * Proxy method for setMaxMNSteps in IslandHandlingToolbox.
     * @param steps The maximum number of mother nature steps
     */
    public void setMaxMNSteps(int steps){
        islandHandlingToolbox.setMaxMNSteps(steps);
    }

    /**
     * Returns all the HandlingToolboxes stored in this object.
     * @return A list containing all the HandlingToolboxes stored in this object
     */
    public List<HandlingToolbox> getAll() {
        List<HandlingToolbox> handlingToolboxes = new ArrayList<>();

        handlingToolboxes.add(assistantHandlingToolbox);
        handlingToolboxes.add(boardHandlingToolbox);
        handlingToolboxes.add(cloudHandlingToolbox);
        handlingToolboxes.add(islandHandlingToolbox);
        handlingToolboxes.addAll(characterCardHandlingToolboxes);
        handlingToolboxes.add(helpingToolbox);

        return handlingToolboxes;
    }

    /**
     * Proxy method for allowCommand in HandlingToolBox.
     * @param command the command for which the toolbox will provide the handling resource if asked
     * @param resourceProvider the provider on which the toolbox relies
     */
    public void allowCommand(CommandEnum command, ClientSender resourceProvider){

        if (command == CommandEnum.SELECT_CHARACTER) resetSelections();

        for (HandlingToolbox toolbox: getAll()) toolbox.allowCommand(command, resourceProvider);
    }

    /**
     * Disables all the commands in all contained Toolboxes.
     */
    public void clearCommands(){
        for (HandlingToolbox toolbox : getAll()) {
            for (CommandEnum command : CommandEnum.values()) toolbox.disableCommand(command);
        }
    }


    /**
     * Gets the current value of the assistantHandlingToolbox property.
     * @return The HandlingToolbox containing all Assistants' allowed actions
     */
    public AssistantHandlingToolbox getAssistantHandlingToolbox() {
        return assistantHandlingToolbox;
    }

    /**
     * Gets the current value of the boardHandlingToolbox property.
     * @return The HandlingToolbox containing all Board's allowed actions
     */
    public BoardHandlingToolbox getBoardHandlingToolbox() {
        return boardHandlingToolbox;
    }

    /**
     * Gets the current value of the cloudHandlingToolbox property.
     * @return The HandlingToolbox containing all Clouds' allowed actions
     */
    public CloudHandlingToolbox getCloudHandlingToolbox() {
        return cloudHandlingToolbox;
    }

    /**
     * Gets the current value of the islandHandlingToolbox property.
     * @return The HandlingToolbox containing all Islands' allowed actions
     */
    public IslandHandlingToolbox getIslandHandlingToolbox() {
        return islandHandlingToolbox;
    }

    /**
     * Gets the current value of the characterCardHandlingToolboxes property.
     * @return The HandlingToolboxes containing all Characters' allowed actions
     */
    public List<CharacterCardHandlingToolbox> getCharacterCardHandlingToolboxes() {
        return characterCardHandlingToolboxes;
    }

    /**
     * Gets the current value of the helpingToolbox property.
     * @return The HandlingToolbox containing all the allowed
     */
    public HelpingToolBox getHelpingToolbox() {
        return helpingToolbox;
    }

    /**
     * Proxy method for resetSelections in CharacterCardSelections.
     */
    public void resetSelections(){
        selections.resetSelections();
    }
}



