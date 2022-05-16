package it.polimi.ingsw.model;

import it.polimi.ingsw.model.beans.CloudBean;
import it.polimi.ingsw.model.beans.GameElementBean;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.observer.CloudWatcher;
import it.polimi.ingsw.view.observer.Watcher;

import java.util.ArrayList;
import java.util.List;

public class Cloud extends DrawableObject{
    private List<StudentEnum> students;
    private final int id;
    private final int studentsPerCloud;
    private List<Watcher> watchers;

    public Cloud(int id, int studentsPerCloud){ //, VirtualView virtualView
        this.id = id;
        this.studentsPerCloud = studentsPerCloud;
        students = new ArrayList<>();
        // CloudObserver obsCreated = new Observer();
        // this.addObserver(ciao);
        // virtualView.addNewObserver(obsCreated);
    }

    /**
     * Version with observer pattern.
     * Create the cloud and its observer; passing virtual view the observer put itself in the
     * right list of the virtual view
     * @param id >= 0
     * @param studentsPerCloud > 0
     * @param virtualView != null
     */
    public Cloud(int id, int studentsPerCloud, VirtualView virtualView){
        this.id = id;
        this.studentsPerCloud = studentsPerCloud;
        students = new ArrayList<>();
        watchers = new ArrayList<>();                                        //list of observers
        Watcher cloudWatcher = new CloudWatcher(this,virtualView);    //New observer and the observer look this object
        watchers.add(cloudWatcher);                                         //This object has the observer

    }

    public void fill(List<StudentEnum> numStudents){
        for(StudentEnum student: numStudents)
            students.add(student);
        //alert();
    }

    public StudentEnum remove(int index){
        if(students.isEmpty())
            return null;

        StudentEnum toReturn = students.remove(index);
        //alert();
        return  toReturn;
    }

    public List<StudentEnum> empty(){
        ArrayList<StudentEnum> studentsToReturn = new ArrayList<>();
        while(!students.isEmpty())
            studentsToReturn.add(students.remove(0));

        //alert();
        return studentsToReturn;
    }

    public boolean isEmpty(){
        return students.isEmpty();
    }

    @Override
    public GameElementBean toBean() {
        CloudBean bean = new CloudBean(id, students);
        return bean;
    }
}
