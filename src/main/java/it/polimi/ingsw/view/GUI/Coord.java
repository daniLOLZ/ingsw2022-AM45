package it.polimi.ingsw.view.GUI;

public class Coord {

    public double x;
    public double y;

    public Coord(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void moveX(double deltaX){
        x = x + deltaX;
    }

    public void moveY(double deltaY){
        y = y + deltaY;
    }

    public void moveCoord(Coord coord2){
        if (coord2 == null) return;
        moveX(coord2.x);
        moveY(coord2.y);
    }

    public Coord pureSumX(double x){
        return new Coord(this.x + x, this.y);
    }

    public Coord pureSumY(double y){
        return new Coord(this.x, this.y + y);
    }

    public Coord pureSum(Coord coord2){
        return pureSumX(coord2.x).pureSumY(coord2.y);
    }
}
