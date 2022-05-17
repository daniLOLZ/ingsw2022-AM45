package it.polimi.ingsw.view.GUI;

public class Coord {

    public double x;
    public double y;

    public Coord(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void moveX(double deltaX){x = x + deltaX;}

    public void moveY(double deltaY){y = y + deltaY;}

    public void moveCoord(Coord coord2){
        x = x + coord2.x;
        y = y + coord2.y;
    }
}
