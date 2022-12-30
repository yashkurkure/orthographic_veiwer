package com.yash.graphics.geometry;
import javax.vecmath.Vector3d;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Mesh
 *
 * This class defines a 3d mesh of triangles.
 *
 * Each mesh contains a set of vertices and faces.
 * The vertices are mapped to ids. Each face is
 * defined by 3 vertices using their ids to form
 * a triangle.
 * */
public class Mesh {

    public int numVertices;
    public int numFaces;

    /**
     * A map of integer ids to vertices.
     * */
    private HashMap<Integer, Vertex> vertices;

    /**
     * Faces of the mesh.
     * */
    private Face[] faces;

    public Mesh() {
        this.numFaces = 0;
        this.numVertices = 0;
        this.vertices = new HashMap<>();
        this.faces = new Face[0];
    }

    public HashMap<Integer, Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(HashMap<Integer, Vertex> vertices) {
        this.vertices = vertices;
    }

    public Face[] getFaces() {
        return faces;
    }

    public void setFaces(Face[] faces) {
        this.faces = faces;
    }

    /**
     * Rotate the mesh by theta radians about X-axis.
     * */
    public Mesh rotateAboutX(double theta) {
        Mesh m = new Mesh();
        m.setFaces(Arrays.copyOf(faces, faces.length));
        HashMap<Integer, Vertex> rotatedVertices = new HashMap<>();
        for(Map.Entry<Integer, Vertex> entry : vertices.entrySet()) {
            int id = entry.getKey();
            Vertex v = entry.getValue();
            rotatedVertices.put(
                    id,
                    v.rotateAboutX(theta)
            );
        }
        m.setVertices(rotatedVertices);
        return m;
    }

    /**
     * Rotate the mesh by theta radians about Y-axis.
     * */
    public Mesh rotateAboutY(double theta) {
        Mesh m = new Mesh();
        m.setFaces(Arrays.copyOf(faces, faces.length));
        HashMap<Integer, Vertex> rotatedVertices = new HashMap<>();
        for(Map.Entry<Integer, Vertex> entry : vertices.entrySet()) {
            int id = entry.getKey();
            Vertex v = entry.getValue();
            rotatedVertices.put(
                    id,
                    v.rotateAboutY(theta)
            );
        }
        m.setVertices(rotatedVertices);
        return m;
    }

    /**
     * Rotate the mesh by theta radians about Z-axis.
     * */
    public Mesh rotateAboutZ(double theta) {
        Mesh m = new Mesh();
        m.setFaces(Arrays.copyOf(faces, faces.length));
        HashMap<Integer, Vertex> rotatedVertices = new HashMap<>();
        for(Map.Entry<Integer, Vertex> entry : vertices.entrySet()) {
            int id = entry.getKey();
            Vertex v = entry.getValue();
            rotatedVertices.put(
                    id,
                    v.rotateAboutZ(theta)
            );
        }
        m.setVertices(rotatedVertices);
        return m;
    }


    /**
     * Read mesh data from file.
     * */
    public static Mesh parseMeshFromFile(String filePath){
        int numVertices = 0;
        int numFaces = 0;
        HashMap <Integer, Vertex> vertices = new HashMap<>();
        ArrayList<Face> faces = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filePath)));
            String st;
            int lineNumber = 0;
            while((st = br.readLine()) != null) {

                // Parse line 0 which tells us the number of vertices and faces
                if(lineNumber == 0) {
                    String[] shapeStats = st.split(",", 0);
                    numVertices = Integer.parseInt(shapeStats[0]);
                    numFaces = Integer.parseInt(shapeStats[1]);

                }
                // Parsing lines 1 to numVertices which are the vertices of the mesh
                else if (lineNumber <= numVertices) {
                    String[] vertex = st.split(",", 0);
                    int id = Integer.parseInt(vertex[0]);
                    double x = Double.parseDouble(vertex[1]);
                    double y = Double.parseDouble(vertex[2]);
                    double z = Double.parseDouble(vertex[3]);
                    vertices.put(id,new Vertex(x,y,z));

                } else if (lineNumber > numVertices &&
                        lineNumber <= numVertices + numFaces) {
                    String[] face = st.split(",", 0);
                    int id1 = Integer.parseInt(face[0]);
                    int id2 = Integer.parseInt(face[1]);
                    int id3 = Integer.parseInt(face[2]);
                    faces.add(new Face(id1, id2, id3));
                } else {
                    System.err.println("Number of vertices and faces don't match data.");
                    System.exit(-1);
                }
                lineNumber++;
            }

        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Mesh m = new Mesh();
        m.setVertices(vertices);
        m.setFaces(faces.toArray(new Face[numFaces]));
        return m;
    }
}
