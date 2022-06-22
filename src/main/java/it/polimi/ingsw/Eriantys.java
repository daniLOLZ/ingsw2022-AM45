package it.polimi.ingsw;

import it.polimi.ingsw.network.client.ClientMain;
import it.polimi.ingsw.network.Preferences;
import it.polimi.ingsw.network.server.Server;

import java.util.Arrays;
import java.util.List;

public class Eriantys {

    public static final List<String> allowedParameters = Arrays.asList("--cli", "--gui", "--server", "--hostname", "--port");
    public static final List<Boolean> parameterRequiresInput = Arrays.asList(false, false, false, true, true); //positional reference
    private static String hostname;
    private static int portNumber;
    private static boolean serverMode = false, GUIMode = false, CLIMode = false;



    /**
     * Entry point for the game
     * In order of priority : Server mode > GUI mode > CLI mode
     * @param args the arguments read from command line
     */
    public static void main(String[] args) {
        assignDefaultNetworkParameters();
        readParameters(args);


        //Starts the appropriate instance of the game
        if(serverMode){
            Server server = new Server(portNumber);
            server.startServer();
        }
        if(GUIMode){
            //todo re-add functionality
            ClientMain client = new ClientMain(hostname, portNumber, true);
            client.startApplication();
        }
        if(CLIMode){
            ClientMain client = new ClientMain(hostname, portNumber, false);
            client.startApplication();
        }
    }

    private static void assignDefaultNetworkParameters() {
        portNumber = Preferences.readPortFromJson();
        hostname = Preferences.readHostnameFromJson();
        serverMode = false;
        GUIMode = false;
        CLIMode = true;

        //Other preferences here;
    }

    /**
     * Reads the arguments from the command line
     * Invalid parameters are ignored
     * @param arguments the arguments read
     */
    private static void readParameters(String[] arguments) {

        int argLength = arguments.length;
        String readArgument;

        for(int argumentIndex = 0; argumentIndex < argLength; argumentIndex++){
            if(!allowedParameters.contains(arguments[argumentIndex])){
                continue;
            }
            if(parameterRequiresInput.get(argumentIndex) && argumentIndex+1 >= argLength) { // There is no actual value
                // after a parameter that requires it
                continue;
            }

            readArgument = arguments[argumentIndex];

            //Server mode > GUI mode > CLI mode
            //Starts actually parsing the parameters, akin to a switch
            if(readArgument.equals(allowedParameters.get(0))){ // --cli
                CLIMode = true;
            }
            else if (readArgument.equals(allowedParameters.get(1))) { // --gui
                GUIMode = true;
            }
            else if (readArgument.equals(allowedParameters.get(2))){ // --server
                serverMode = true;
            }
            else if (readArgument.equals(allowedParameters.get(3))) { // --hostname
                hostname = arguments[argumentIndex+1];
            }
            else if (readArgument.equals(allowedParameters.get(4))) { // --port
                portNumber = Integer.parseInt(arguments[argumentIndex+1]);
            }

            //Other parameters here, if needed

        }
    }
}
