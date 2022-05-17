package it.polimi.ingsw.view.GUI;

//this is a stub class
public class ConnectionWithServerHandler {

    public static boolean login(String nickname){

        //don't take this personally, I needed a way to test login failure
        if (nickname.equals("ingconti")) return false;
        return true;
    }
}
