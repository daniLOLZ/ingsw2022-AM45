package it.polimi.ingsw.model.beans;

import it.polimi.ingsw.model.StudentEnum;
import it.polimi.ingsw.model.TeamEnum;
import it.polimi.ingsw.network.BeanEnum;

import java.util.List;

public class CloudBean extends GameElementBean{
    private int idCloud;
    private List<StudentEnum> students;

    public CloudBean(int idCloud, List<StudentEnum> students){
        final int highPriority = 3;
        this.idCloud = idCloud;
        this.students = students;
        priority = highPriority;
    }

    @Override
    public BeanEnum getBeanType() {
        return BeanEnum.CLOUD_BEAN;
    }

    public int getIdCloud() {
        return idCloud;
    }

    public List<StudentEnum> getStudents() {
        return students;
    }

    public void setIdCloud(int idCloud) {
        this.idCloud = idCloud;
    }

    public void setStudents(List<StudentEnum> students) {
        this.students = students;
    }


    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();


        toReturn.append("\t____________________________________\t\n");
        toReturn.append("\t|Cloud : ").append(idCloud).append("\n");
        toReturn.append("\t|\tStudents: ").append(students).append("\n");
        toReturn.append("\t____________________________________\t\n");

        return setTab(toReturn.toString());
    }
}
