package it.polimi.ingsw.view.GUI.handlingToolbox;

import it.polimi.ingsw.network.ClientNetworkManager;
import it.polimi.ingsw.network.CommandEnum;

public interface HandlingToolbox {

    //note: commands can be allowed and disabled infinitely.
    //      No effect will result if the toolbox doesn't
    //      offer any functionality for the given command
    /**
     * Makes the relative handling resource available for the given command (limited to the toolbox functionalities)
     * @param command the command for which the toolbox will provide the handling resource if asked
     * @param resourceProvider the provider on which the toolbox relies
     */
     void allowCommand(CommandEnum command, ClientNetworkManager resourceProvider);

    /**
     * Changes the handling resources to make sure the given command isn't allowed
     * @param commandEnum the command to disable
     */
    void disableCommand(CommandEnum commandEnum);
}
