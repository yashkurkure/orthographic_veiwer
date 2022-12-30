package com.yash.graphics.view;

import com.yash.graphics.engine.Engine;
import com.yash.graphics.geometry.Mesh;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controller for the JavaFX Application.
 * */
public class Controller {

    /**
     * JavaFX Canvas
     * */
    @FXML
    private Canvas canvas;

    /**
     * File path of the mesh file.
     * */
    private String filePath;

    /**
     * Keep track of mouse positions
     * */
    private MouseDragRotate mouseDragRotate;

    /**
     * The current mesh being displayed on the Canvas.
     * */
    private Mesh mesh;

    /**
     * ThreadPool to run the Engine on.
     * */
    private ExecutorService exec = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true); // allows app to exit if tasks are running
        return t ;
    });

    /**
     * Keeps track of rotation about Y-axis in degrees.
     * */
    private int thetaAboutY = 0;

    /**
     * Keeps track of rotation about X-axis in degrees.
     * */
    private int thetaAboutX = 0;

    /**
     * Color used to draw the mesh.
     * */
    private Color color;

    /**
     * Initialization code after GUI elements are loaded.
     * */
    @FXML
    private void initialize() {
        color = new Color(0.0, 0.0, 1.0, 1.0);
        canvas.getGraphicsContext2D().setLineWidth(1);
        canvas.getGraphicsContext2D().fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        setMouseHandlers();
    }

    private void setMouseHandlers(){

        // Handler for when the mouse is pressed(hold)
        // This gets the initial position of the mouse where
        // the drag action might start.
        canvas.setOnMousePressed(mouseEvent -> {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();

            // Create a new mouse tracker for the drag event
            mouseDragRotate = new MouseDragRotate();
            mouseDragRotate.initialX = x;
            mouseDragRotate.initialY = y;
        });

        // Handler for when the mouse is dragged.
        // Here we can calculate how far the mouse went
        // from its initial position where the drag started.
        canvas.setOnMouseDragged(mouseEvent -> {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();

            // Set the final position of the mouse.
            mouseDragRotate.finalX = x;
            mouseDragRotate.finalY = y;

            if(mesh!=null){

                // Create a task to rotate the mesh.
                Task<WritableImage> task = new Task<WritableImage>() {
                    @Override
                    protected WritableImage call() throws Exception {
                        Engine e = new Engine(canvas.getHeight(), canvas.getWidth());
                        return e.draw(
                                mesh
                                    .rotateAboutY(Math.toRadians(thetaAboutY + mouseDragRotate.getRotationAngleAboutY()))
                                    .rotateAboutX(Math.toRadians(thetaAboutX + mouseDragRotate.getRotationAngleAboutX()))
                                , color
                        );
                    }
                };

                // Callback to update the canvas
                task.setOnSucceeded(workerStateEvent -> {
                    WritableImage i = task.getValue();
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    clearCanvas();
                    gc.drawImage(i, 0, 0);

                });

                exec.submit(task);

            }
        });


        // Handler for when the mouse button is released.
        // This indicates the drag event was completed.
        canvas.setOnMouseReleased(mouseEvent -> {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();

            // Get the final coordinates of the mouse
            mouseDragRotate.finalX = x;
            mouseDragRotate.finalY = y;

            // Update the mesh to its final state (drag event completed by user)
            mesh = mesh
                    .rotateAboutY(Math.toRadians(thetaAboutY + mouseDragRotate.getRotationAngleAboutY()))
                    .rotateAboutX(Math.toRadians(thetaAboutX + mouseDragRotate.getRotationAngleAboutX()));

            // Reset the angles for the next drag event.
            thetaAboutX = 0;
            thetaAboutY = 0;
            mouseDragRotate = null;
        });
    }

    /**
     * Handler for opening files.
     * */
    @FXML
    private void handleOpenFile() {

        // Open the file
        Stage primaryStage = (Stage) canvas.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("TEXT (*.txt)", "*.txt"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Open mesh file...");
        File f = fileChooser.showOpenDialog(primaryStage);

        if(f != null) {

            filePath = f.getAbsolutePath();

            // Load the mesh from file.
            mesh = Mesh.parseMeshFromFile(filePath);

            // Create a task to draw the mesh onto a Writable Image
            Task<WritableImage> task = new Task<WritableImage>() {
                @Override
                protected WritableImage call() throws Exception {
                    Engine e = new Engine(canvas.getHeight(), canvas.getWidth());
                    return e.draw(mesh, color);
                }
            };

            // When drawing is complete, draw the image onto the canvas.
            task.setOnSucceeded(workerStateEvent -> {
                WritableImage i = task.getValue();
                clearCanvas();
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.drawImage(i, 0, 0);

            });

            // Execute on separate thread to not block the UI thread.
            exec.submit(task);
        }
    }

    /**
     * Handler to exit application.
     * */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    /**
     * Clear the canvas.
     * */
    private void clearCanvas(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
