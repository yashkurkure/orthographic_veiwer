package com.yash.graphics.geometry;


/**
 * Line
 *
 * This class defines a line in a mesh.
 *
 * Each line is represented by 2 vertices
 * represented by their ids.
 * */
public class Line{

    private int id1;
    private int id2;

    public Line(int id1, int id2){
        this.id1 = id1;
        this.id2 = id2;
    }

    public int getId1() {return id1;}
    public int getId2() {return id2;}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if(!(o instanceof Line)) {
            return false;
        }

        Line l = (Line) o;

        if(this.id1 == l.id1 && this.id2 == l.id2) return true;
        if(this.id1 == l.id2 && this.id2 == l.id1) return true;

        return false;
    }

    @Override
    public int hashCode() {
        // Using the Cantor Pairing function to generate a unique hash code
        return ((id1 + id2)*(id1+id2+1))/2;
    }
}
