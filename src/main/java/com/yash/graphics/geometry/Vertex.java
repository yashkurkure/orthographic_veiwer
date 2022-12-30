package com.yash.graphics.geometry;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Vertex
 *
 * This class holds the data for a vertex in 3d space.
 *
 * */
public class Vertex {

    private double x;
    private double y;
    private double z;

    public Vertex(double x, double y, double z){
        // Only stores till 5 decimal places
        this.x = ((double)((int)(x*100)))/100.0;
        this.y = ((double)((int)(y*100)))/100.0;
        this.z = ((double)((int)(z*100)))/100.0;
    }

    public double getX(){ return x;}
    public double getY(){ return y;}
    public double getZ(){ return z;}

    /**
     * Projects the vertex on the X-Y Plane.
     * */
    public Vertex projectOnXYPlane(){
        return new Vertex(x,y,0);
    }

    /**
     * Rotates the vertex about X-axis.
     * */
    public Vertex rotateAboutX(double theta) {
        return new Vertex(
                x,
                y*cos(theta) - z*sin(theta),
                y*sin(theta) + z*cos(theta)
                );
    }

    /**
     * Rotates the vertex about Y-axis.
     * */
    public Vertex rotateAboutY(double theta) {
        return new Vertex(
                x*cos(theta) + z*sin(theta),
                y,
                z*cos(theta) - x*sin(theta)
        );
    }

    /**
     * Rotates the vertex about Z-axis.
     * */
    public Vertex rotateAboutZ(double theta) {
        return new Vertex(
                x*sin(theta) + y*cos(theta),
                x*sin(theta) + y*cos(theta),
                z
        );
    }

    @Override
    public String toString(){
        return  "(" + x + ", " + y + ", " + z + ")";
    }

}
