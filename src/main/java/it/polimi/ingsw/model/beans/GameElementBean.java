package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.network.Bean;

import java.util.Scanner;

public abstract class GameElementBean implements Bean {
    protected int priority;                 //Priority is useful to draw the element in order

    /**
     * Show in CLI the Bean information
     * @return the string with information about that bean
     */
    public abstract String toString();



    public int getPriority() {
        return priority;
    }

    /**
     *
     * @param string != null
     * @param borderLength > 0 the length of the  frame
     * @return a string with all rows that end in equal point.
     * All \n are aligned
     */
    protected String setTab(String string, int borderLength){
        Scanner scanner = new Scanner(string);
        scanner.useDelimiter("\n");
        StringBuilder builder = new StringBuilder();

        while(scanner.hasNext()){
            String x = scanner.next();
            while(x.length() < borderLength)
                x = x + " ";

            builder.append(x).append("\n");
        }
        return  builder.toString();
    }

    /**
     *
     * @param string != null
     * @return a string with all rows that end in equal point.
     * All \n are aligned
     */
    protected String setTab(String string){
        Scanner scanner = new Scanner(string);
        scanner.useDelimiter("\n");
        StringBuilder builder = new StringBuilder();
        String border = "    ____________________________________    \n";
        while(scanner.hasNext()){
            String x = scanner.next();
            while(x.length() < border.length())
                x = x + " ";

            builder.append(x).append("\n");
        }
        return  builder.toString();
    }

    //todo
}
