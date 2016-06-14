/**
 * #------ Line Class ------#
 * This class is the blueprint for a line object.
 * This class doesn't have a lot of functionality
 * other than storing information about a line.
 * <p>
 * The Field.paintComponent uses these objects to
 * draw the lines on the field, as results of
 * simulations.
 */

package Interface;

public class Line {

    private Point point1;
    private Point point2;
    private int number;

    public Line(Point point1, Point point2, int number) {
        this.point1 = point1;
        this.point2 = point2;
        this.number = number;
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public int getNumber() {
        return number;
    }
}