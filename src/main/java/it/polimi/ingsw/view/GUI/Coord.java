package it.polimi.ingsw.view.GUI;

public class Coord {

    public final static int COUNTERCLOCKWISE = 1, CLOCKWISE = -1, NO_ROTATION = 0, UPSIDE_DOWN = 2;

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

    public void rotate(Coord center, int rotation){

        if (center == null) return;

        Coord normalized = this.pureSumX(-center.x).pureSumY(-center.y);

        switch (rotation){
            case COUNTERCLOCKWISE -> {
                double oldX = normalized.x;
                normalized.x = -normalized.y;
                normalized.y = oldX;
            }
            case CLOCKWISE -> {
                double oldX = normalized.x;
                normalized.x = normalized.y;
                normalized.y = -oldX;
            }
            case UPSIDE_DOWN -> {
                normalized.x = -normalized.x;
                normalized.y = -normalized.y;
            }
            default -> {return;}
        }

        this.x = center.x + normalized.x;
        this.y = center.y + normalized.y;
    }

    public Coord pureRotate(Coord center, int rotation){
        Coord returnable = new Coord(x,y);
        returnable.rotate(center, rotation);
        return returnable;
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
