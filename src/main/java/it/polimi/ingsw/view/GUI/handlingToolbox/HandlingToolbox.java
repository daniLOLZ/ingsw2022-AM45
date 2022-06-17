package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.CommandEnum;
import it.polimi.ingsw.network.client.ClientSender;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public interface HandlingToolbox {
    EventHandler<MouseEvent> NO_EFFECT = event -> {
        };

    EventHandler<MouseEvent> DISABLED = event -> {
        };

    EventHandler<ActionEvent> NO_ACTION = event -> {

        };

    //note: commands can be allowed and disabled infinitely.
    //      No effect will result if the toolbox doesn't
    //      offer any functionality for the given command
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
