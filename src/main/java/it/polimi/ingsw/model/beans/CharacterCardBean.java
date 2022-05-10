package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.player.PlayerEnum;

import java.util.ArrayList;
import java.util.List;

public class CharacterCardBean extends GameElementBean {
    private int id;
    private String name;
    private int cost;
    private String description;
    private List<StudentEnum> students;

    public CharacterCardBean(int id, String name, String description, List<StudentEnum> students,int cost){
        final int highPriority = 2;
        this.id = id;
        this.cost = cost;
        this.description = description;
        this.name = name;
        this.students = students;
        priority = highPriority;
    }

    public List<StudentEnum> getStudents() {
        return students;
    }

    public int getId() {
        return id;
    }

    public int getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    @Override
    public String drawCLI() {
        StringBuilder toReturn = new StringBuilder();



        toReturn.append("\t_________________________________________\t\n");
        toReturn.append("\t|            ::").append(name).append("::").append("\n");
        toReturn.append("\t|\tID: ").append(id).append("\n");
        toReturn.append("\t|\tCOST: ").append(cost).append("\n");
        toReturn.append("\t|\tEFFECT: ").append(description).append("\n");
        if(students != null)
            toReturn.append("\t|\tSTUDENTS: ").append(students).append("\n");

        toReturn.append("\t_________________________________________\t\n");

        String border = 	"___________________________________________________\t\n";

        return setTab(toReturn.toString(), border.length() );
    }
}