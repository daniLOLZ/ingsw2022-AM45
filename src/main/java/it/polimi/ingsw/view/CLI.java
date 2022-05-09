package it.polimi.ingsw.view;

import it.polimi.ingsw.model.beans.GameBoardBean;
import it.polimi.ingsw.model.beans.GameElementBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI {
    private List<GameElementBean> beans;
    private StringBuilder View;
    private StringBuilder LastView;
    private StringBuilder LastElement;


    public CLI(){
        beans = new ArrayList<>();
        View = new StringBuilder("");
        LastView = new StringBuilder("");
        LastElement = new StringBuilder();
    }

    public void show(){
        final int lowestPriority = 4;
        final int timeToNewLine = 1;
        final int startPosition = 0;
        int precPriority = lowestPriority;
        int positionOnScreen = 0;
        GameElementBean curr = beans.get(0);
        GameElementBean bean;
        int index = 0;
        int min = lowestPriority;

        //I must draw all beans. When I draw one I remove it.
        while(!beans.isEmpty()){
            min = lowestPriority;
            //choosing bean to draw (min priority go first)
            for(int id = 0; id < beans.size(); id++){
                bean = beans.get(id);
                if(bean.getPriority() < min ){
                    min = bean.getPriority();
                    curr = bean;
                    index = id;
                }
            }

            if(precPriority == curr.getPriority())
                positionOnScreen++;

            if(positionOnScreen > timeToNewLine)
                positionOnScreen = startPosition;


            draw(positionOnScreen, curr.drawCLI());
            precPriority = curr.getPriority();
            beans.remove(index);


        }

        System.out.println(View.toString());
    }

    private void draw(int position, String elementToDraw){
        int width = 7;


        if(position == 0){
            LastView = new StringBuilder(View.toString());
            View.append("\n\n");
            View.append(elementToDraw);
            LastElement = new StringBuilder(elementToDraw);
            return;
        }

        //Old element to show in left position on screen
        Scanner scannerLastElement = new Scanner(LastElement.toString());
        scannerLastElement.useDelimiter("\n");

        //new element to show in right position on screen
        Scanner scannerThisElement = new Scanner(elementToDraw);
        scannerThisElement.useDelimiter("\n");

        //OFFSET
        StringBuilder offsetBuilder = new StringBuilder();
        for(int i = 0; i < width * position; i++){
            offsetBuilder.append("\t");
        }
        String offset = offsetBuilder.toString();
        String last = LastView.toString();

        /*
        now for each old element's row I append the new element's row with an offset.
        I must start from the last view, before the old element was inserted, and append
        both two elements.
         */
        while(scannerLastElement.hasNext() && scannerThisElement.hasNext()){
            LastView.append(scannerLastElement.next()).append(offset).append(scannerThisElement.next()).append("\n");
        }


        View = new StringBuilder(LastView.toString());
        LastElement = new StringBuilder(elementToDraw);
    }

    public void addBean(GameElementBean bean){
        beans.add(bean);
    }

    public void removeBean(int position){
        beans.remove(position);
    }
    public void clearBeans(){
        beans.clear();
    }
}
