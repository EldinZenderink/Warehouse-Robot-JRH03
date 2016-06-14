package asrs.Controllers;

import asrs.Models.Location;
import asrs.Models.Product;
import asrs.Serial.Serial;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WarehouseController implements Initializable {
    public AnchorPane routeCanvas;
    public AnchorPane robotCanvas;
    private Circle robot;
    private TranslateTransition robotTT;
    private Location robotStartLocation = new Location(1, 0);
    private ArrayList<Location> locations;
    private int circleSize = 10;
    private int currentLocationIndex = -1;
    private String state;
    volatile int navigationSpeed = 10;
    private final String MOVING = "moving";
    private final String PICKING = "picking";
    private final String STOPPED = "stopped";
    private Serial serial;

    public WarehouseController() {
        robot = new Circle(100, 100, circleSize * 1.5);
        robot.setFill(Color.RED);
        robotTT = new TranslateTransition(Duration.millis(2000), robot);
        robotTT.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                state = STOPPED;
            }
        });
        state = STOPPED;
    }

    /**
     * Add robot to canvas after the view is initialized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        robotCanvas.getChildren().add(robot);
        robotReset();
    }

    /**
     * Sets the serial object to this object
     * @param serial
     */
    public void setSerial(Serial serial){
        this.serial = serial;
    }

    /**
     * Get the current products arraylist index
     * @return
     */
    public int getCurrentLocationIndex() {
        return currentLocationIndex;
    }

    /**
     * Resets the warehouse visualisation
     */
    public void reset() {
        currentLocationIndex = -1;
        locations = null;
        clearCanvas();
        robotReset();
    }

    /**
     * Set the route of the robot on to the warehouse
     * @param locations
     */
    public void setRoute(ArrayList<Location> locations) {
        reset();
        this.locations = locations;
        Location previousLocation = null;

        for(Location location : locations) {
            int endTop = calculatePositionX(location.getX());
            int endLeft = calculatePositionY(location.getY());

            if(previousLocation != null) {
                // Draw a line from the previous location to the new location
                int startTop = calculatePositionX(previousLocation.getX());
                int startLeft = calculatePositionY(previousLocation.getY());

                Line line = new Line(startTop, startLeft, endTop, endLeft);
                line.setStrokeWidth(2);
                addShape(line);

                Circle circle = new Circle(endTop, endLeft, circleSize);
                addShape(circle);
            } else {
                Circle circle = new Circle(endTop, endLeft, circleSize);
                addShape(circle);
            }
            previousLocation = location;
        }
    }

    /**
     * Move robot dot to a specific location
     * @param x
     * @param y
     */
    public void setRobotLocation(int x, int y) {
        robot.setCenterX(x);
        robot.setCenterY(y);
    }

    /**
     * Set the location of the robot to a grid position
     * @param location
     */
    public void setRobotLocation(Location location) {
        setRobotLocation(calculatePositionX(location.getX()), calculatePositionY(location.getY()));
    }

    /**
     * Reset robot
     */
    public void robotReset() {
        setRobotLocation(robotStartLocation);
        robot.toFront();
    }

    /**
     * Go to next location
     * @return
     */
    public boolean goToNext() {
        state = MOVING;
        robotMove(locations.get(currentLocationIndex + 1));
        currentLocationIndex = currentLocationIndex + 1;
        return true;
    }

    /**
     * Go to previous location
     * @return
     */
    public boolean goToPrevious() {
        state = MOVING;
        if(currentLocationIndex > 0) {
            robotMove(locations.get(currentLocationIndex - 1));
            currentLocationIndex = currentLocationIndex - 1;
        }
        return true;
    }

    /**
     * Move robot to a grid location
     * @param location
     * @return
     */
    public int robotMove(Location location) {
        /**
         * If robot moves to bin location,
         * Modify location for visualisation
         */
        if(location.getX() == 6) {
            location.setX(7);
        } else if(location.getX() == 7) {
            location.setX(9);
        }
        int startX = (int) robot.getTranslateX() + (int) robot.getCenterX(),
            startY = (int) robot.getTranslateY() + (int) robot.getCenterY(),
            endX   = calculatePositionX(location.getX()),
            endY   = calculatePositionY(location.getY()),
            moveX  = (calculatePositionX(location.getX()) - startX),
            moveY  = (calculatePositionY(location.getY()) - startY),
            speed  = calculateAnimationSpeed(startX, startY, endX, endY);

        robotTT.setDuration(Duration.valueOf(String.valueOf(speed) + "ms"));
        robotTT.setByX(moveX);
        robotTT.setByY(moveY);
        robotTT.play();
        return speed + 100;
    }

    /**
     * Ad a shape to the routeCanvas
     * @param shape
     */
    private void addShape(javafx.scene.shape.Shape shape) {
        routeCanvas.getChildren().add(shape);
    }

    /**
     * Remove the children from the routeCanvas
     */
    private void clearCanvas() { routeCanvas.getChildren().clear(); }

    /**
     * Calculate the pixel position of given X grid location
     * @param i
     * @return
     */
    private int calculatePositionX(int i) {
        int padding = i * 20 - 10;
        int blocks = i * 100 - 50;
        return padding + blocks;
    }

    /**
     * Calculate the pixel position of given Y grid location
     * @param i
     * @return
     */
    private int calculatePositionY(int i) {
        i = Math.abs(6 - i);
        int padding = i * 20 - 10;
        int blocks = i * 100 - 50;
        return padding + blocks;
    }

    /**
     * Calculate the speed in MS between two grid coordinates
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return
     */
    private int calculateAnimationSpeed(int startX, int startY, int endX, int endY) {
        int diffX = Math.abs(startX - endX),
            diffY = Math.abs(startY - endY);
        return ((diffX + diffY) * navigationSpeed / 2) / 12;
    }
}
