package it.polimi.ingsw.view.GUI;

/**
 * The Coord class offers coordinates storage and handling
 */
public class Coord {

    public final static int  NO_ROTATION = 0, COUNTERCLOCKWISE = 1, UPSIDE_DOWN = 2, CLOCKWISE = 3, FULL_ROTATION = 4;

    public double x;
    public double y;

    public Coord(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * Modifies the x property by the given delta.
     * @param deltaX The value that shall be added to x
     */
    public void moveX(double deltaX){
        x = x + deltaX;
    }

    /**
     * Modifies the y property by the given delta.
     * @param deltaY The value that shall be added to y
     */
    public void moveY(double deltaY){
        y = y + deltaY;
    }

    /**
     * Modifies the coordinates by adding the ones of another Coord.
     * @param coord2 The coordinates to sum to the old ones
     */
    public void moveCoord(Coord coord2){
        if (coord2 == null) return;
        moveX(coord2.x);
        moveY(coord2.y);
    }

    /**
     * Rotates the point around the given center by the given rotation
     * @param center The center around which apply the rotation
     * @param rotation The rotation angle (in right angles, positive counterclockwise)
     */
    public void rotate(Coord center, int rotation){

        if (center == null) return;

        Coord normalized = this.pureSumX(-center.x).pureSumY(-center.y);

        //standardize the rotation parameter
        rotation = rotation % FULL_ROTATION;
        while (rotation < NO_ROTATION) rotation += FULL_ROTATION;

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

    /**
     * Creates a new Coord, result of the rotation of this.
     * @param center The center around which apply the rotation
     * @param rotation The rotation angle (in right angles, positive counterclockwise)
     * @return The rotated Coord
     */
    public Coord pureRotate(Coord center, int rotation){
        Coord returnable = new Coord(x,y);
        returnable.rotate(center, rotation);
        return returnable;
    }

    /**
     * Returns a new Coord, result of the translation of this on the x-axis bt the given delta.
     * @param x The distance on the x-axis between this Coord and the new Coord
     * @return The translated Coord
     */
    public Coord pureSumX(double x){
        return new Coord(this.x + x, this.y);
    }

    /**
     * Returns a new Coord, result of the translation of this on the y-axis bt the given delta.
     * @param y The distance on the y-axis between this Coord and the new Coord
     * @return The translated Coord
     */
    public Coord pureSumY(double y){
        return new Coord(this.x, this.y + y);
    }

    /**
     * Returns a new Coord, result of the sum between this and the given Coord.
     * @param coord2 The Coord that shall be added to this
     * @return The sum of this Coord and the given Coord
     */
    public Coord pureSum(Coord coord2){
        return pureSumX(coord2.x).pureSumY(coord2.y);
    }

}
