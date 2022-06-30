package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * The HandlingToolbox class provides and stores EventHandlers to decorate any objects at run-time.
 * It provides mainly allow and disable command methods. Every command can be enabled and disabled for any number of time.
 * Nothing will happen if the HandlingToolbox doesn't offer any functionality for the input command.
 */
public interface HandlingToolbox {
    /**
     * An EventHandler assigned to NO_EFFECT has no assigned action
     */
    EventHandler<MouseEvent> NO_EFFECT = event -> {
        };

    /**
     * An EventHandler assigned to DISABLED is temporary inactive
     */
    EventHandler<MouseEvent> DISABLED = event -> {
        };

    /**
     * An EventHandler assigned to NO_ACTION has no assigned action
     */
    EventHandler<ActionEvent> NO_ACTION = event -> {

        };


    /**
     * Makes the relative handling resource available for the given command (limited to the toolbox functionalities).
     * Note that the correct use of the handling toolbox requires disabling all no-more-permitted commands before
     * enabling the new ones
     * @param command the command for which the toolbox will provide the handling resource if asked
     * @param resourceProvider the provider on which the toolbox relies
     */
     void allowCommand(CommandEnum command, ClientSender resourceProvider);

    /**
     * Changes the handling resources to make sure the given command isn't allowed
     * @param command the command to disable
     */
    void disableCommand(CommandEnum command);
}
