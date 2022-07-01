package it.polimi.ingsw.network;

public class ConnectionFailedException extends Exception{

    private final String errorMessage;

    public ConnectionFailedException(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
