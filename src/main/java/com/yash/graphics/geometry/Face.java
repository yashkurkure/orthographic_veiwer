package com.yash.graphics.geometry;

/**
 * Face
 *
 * This class defines a face in a mesh.
 *
 * Each face consists of 3 points, forming a triangle.
 * The points are represented by their ids inside the
 * face and the Mesh class.
 * */
public class Face {

    private int id1;
    private int id2;
    private int id3;

    public Face(int id1, int id2, int id3){
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
    }

    public int getId1() {return id1;}
    public int getId2() {return id2;}
    public int getId3() {return id3;}

    /**
     * Explodes the face into it's boundary lines.
     * */
    public Line[] explode() {
        Line[] lines = new Line[3];
        lines[0] = new Line(id1, id2);
        lines[1] = new Line(id2, id3);
        lines[2] = new Line(id3, id1);
        return lines;
    }
}
