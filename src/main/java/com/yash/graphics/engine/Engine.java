package com.yash.graphics.engine;
import com.yash.graphics.geometry.Face;
import com.yash.graphics.geometry.Line;
import com.yash.graphics.geometry.Mesh;
import com.yash.graphics.geometry.Vertex;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Engine
 *
 * The engine that draws the pixels onto a
 * WritableImage.
 * */
public class Engine {

    /**
     * Image on which we draw pixels.
     * */
    private WritableImage image;

    /**
     * Height of the Canvas in JavaFX UI.
     * */
    private double canvasHeight;

    /**
     * Width of the Canvas in JavaFX UI.
     * */
    private double canvasWidth;


    public Engine(double canvasHeight, double canvasWidth) {
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        image = new WritableImage((int)canvasHeight, (int)canvasWidth);
    }


    /**
     * This method is responsible for drawing a given mesh
     * onto the WritableImage. It returns the WritableImage
     * which is ready to be put onto the JavaFX canvas.
     * */
    public WritableImage draw(Mesh mesh, Color color) {
        if(mesh == null) {
            System.err.println("Could not draw mesh, because mesh was null.");
            return null;
        }

        // Get the faces and the vertices of the mesh
        Face[] faces = mesh.getFaces();
        HashMap<Integer, Vertex> vertices = mesh.getVertices();

        // Draw circles that represent the vertices
        for(Vertex v : vertices.values()) {
            drawFilledCircle(transformToCanvasCoordinates(v), 2, color, true);
        }

        // Accumulate all the lines.
        // This avoids drawing duplicate lines.
        HashSet<Line> lines = new HashSet<>();
        for(Face f : faces) {
            for(Line l : f.explode()) {
                // Adding to a set avoid duplicate lines
                lines.add(l);
            }
        }

        // Draw the lines
        for(Line l : lines) {
            Vertex v1 = transformToCanvasCoordinates(vertices.get(l.getId1()).projectOnXYPlane());
            Vertex v2 = transformToCanvasCoordinates(vertices.get(l.getId2()).projectOnXYPlane());
            drawLine(v1, v2 , color);
        }

        return image;
    }

    /**
     * Puts a colored pixel onto the WritableImage at the specified location.
     * */
    private synchronized void drawPixel(Vertex v, Color color){
        // Only draw what is visible on the canvas area
        // Note that the co-ordinate system of the JavaFX canvas is different.
        if(v.getX() >= 0 && v.getY() >=0 && v.getX() < canvasWidth && v.getY() < canvasHeight){
            PixelWriter pw = image.getPixelWriter();
            pw.setColor((int)v.getX(), (int)v.getY(), color);
        }
    }

    /**
     * Draw a circle using Bresenham's Circle Drawing Algorithm.
     * */
    private void drawFilledCircle(Vertex vc, int r, Color color, boolean filled) {

        // xc, yc are the center of the circle
        int xc = (int) vc.getX();
        int yc = (int) vc.getY();

        // x,y lie on the circumference
        int x = 0;
        int y = r;

        // The decision parameter
        int d = 3 - 2 * r;

        // Draws pixels on axis x = xc and y = yc of the circle;
        _drawFilledCircle(xc, yc, x ,y, color, filled);

        // Draw the pixels in octets of the circle.
        while(y >= x){
            // Move East
            x++;
            if(d>0){
                // Move South East
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * (x - y) + 6;
            }
            _drawFilledCircle(xc, yc,x,y, color, filled);
        }

    }

    /**
     * Helper to draw Bresenham's Circle.
     *
     * Fill pixels (x,y) relative to the center of the circle (xc, yc)
     * in all 8 octets of the circle. To Fill the circle, lines are drawn
     * from (xc, yc) to (x, y) in each octet.
     * */
    private void _drawFilledCircle(int xc, int yc, int x, int y, Color color, boolean filled){

        // Quadrant 1
        drawPixel(new Vertex(xc+x, yc+y,0),color);
        if(filled) drawLine(new Vertex(xc, yc, 0), new Vertex(xc+x, yc+y,0),color);
        drawPixel(new Vertex(xc+y, yc+x,0),color);
        if(filled) drawLine(new Vertex(xc, yc, 0), new Vertex(xc+y, yc+x,0),color);

        // Quadrant 2
        drawPixel(new Vertex(xc-x, yc+y,0),color);
        if(filled) drawLine(new Vertex(xc, yc, 0), new Vertex(xc-x, yc+y,0),color);
        drawPixel(new Vertex(xc-y, yc+x,0),color);
        if(filled) drawLine(new Vertex(xc, yc, 0), new Vertex(xc-y, yc+x,0),color);

        // Quadrant 3
        drawPixel(new Vertex(xc-x, yc-y,0),color);
        if(filled) drawLine(new Vertex(xc, yc, 0), new Vertex(xc-x, yc-y,0),color);
        drawPixel(new Vertex(xc-y, yc-x,0),color);
        if(filled) drawLine(new Vertex(xc, yc, 0), new Vertex(xc+y, yc-x,0),color);


        // Quadrant 4
        drawPixel(new Vertex(xc+x, yc-y,0),color);
        if(filled) drawLine(new Vertex(xc, yc, 0), new Vertex(xc+x, yc-y,0),color);
        drawPixel(new Vertex(xc+y, yc-x,0),color);
        if(filled) drawLine(new Vertex(xc, yc, 0), new Vertex(xc+y, yc-x,0),color);

    }


    /**
     * Draw a line using the Bresenham's Line Drawing Algorithm.
     * */
    private void drawLine(Vertex v0, Vertex v1, Color color) {

        double x0 = v0.getX();
        double y0 = v0.getY();
        double x1 = v1.getX();
        double y1 = v1.getY();

        double dx = Math.abs(x1 - x0);
        double sx = (x0 < x1) ? 1 : -1;
        double dy = -Math.abs(y1 - y0);
        double sy = (y0 < y1) ? 1 : -1;

        // Incremental error
        double error = dx + dy;

        while (true) {
            drawPixel(new Vertex(x0, y0, 0), color);
            if ((x0 == x1) && (y0 == y1)) break;
            double e2 = 2 * error;
            if (e2 >= dy) {
                if(x0 == x1) break;
                error += dy;
                x0 += sx;
            }
            if (e2 <= dx) {
                if(y0 == y1) break;
                error += dx;
                y0 += sy;
            }
        }
    }


    /**
     * Converts the world coordinates to Canvas Coordinates.
     *
     * The JavaFX Canvas has the following coordinate system:
     *
     *    (0,0)
     *      .---------------> +x
     *      |
     *      |
     *      |
     *      |
     *      v
     *     +y
     *
     *     The 4 corners of the Canvas are located at:
     *      (0,0), (canvasWidth, 0), (0, canvasHeight) and (canvasWidth, canvasHeight)
     *
     *     To transform the world coordinates into canvas such that our
     *     origin lies at the center of the canvas, we must:
     *      1) Invert the y-axis
     *      2) Shift the origin to (canvasWidth/2, canvasHeight/2)
     *
     *      We further scale the world coordinates by 100, such that the canvas
     *      can represent 8 units of the world on each axis.
     * */
    private Vertex transformToCanvasCoordinates(Vertex v) {

        // Scale the world coordinates by 100.
        // Our canvas will be able to represent 8 units of the world
        // coordinate system on each axis.
        int scaleFactor = 100;

        return new Vertex(
                v.getX() * scaleFactor + canvasWidth / 2.0,
                -v.getY() * scaleFactor + canvasHeight /2.0,
                0
        );
    }

}
