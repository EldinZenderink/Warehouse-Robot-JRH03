/**
 *       #--- Field class for the simulation grid ---#
 * This class is the GUI for the simulation grid, and handles
 * the way you interact with the grid like clicking on points
 * to add them to the simulation. It also contains many methods
 * for handling points and also drawing lines on the field.
 */

package Interface;

import Algorithm.Algorithms.Route;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Objects;

public class Field extends JPanel implements MouseListener {

    private ArrayList<Point> grid;
    private ArrayList<Line> lines;
    private Frame frame;
    private int columns;
    private int pointSize;
    private boolean drawLines;

    public Field(int columns, Frame frame) {
        this.columns = columns;
        grid = new ArrayList<>();
        lines = new ArrayList<>();

        setPreferredSize(new Dimension(1001, 1001));
        addMouseListener(this);

        this.drawLines = false;

        initializeField();
    }

    public void initializeField() {
        /**
         * This method creates all the points of the grid and
         * generates their coordinates, which are used to draw
         * the points onto the gridmap.
         *
         *
         *  10 columns -> 100px wide 100px high | 100 points
         *  25 columns ->  40px wide  40px high | 625 points
         *  50 columns ->  20px wide  20px high | 2500 points
         *  100columns ->  10px wide  10px high | 10000 points
         */

        pointSize = 10;
        int maxRow = 100;
        int gridSize = 10000;
        int rowCount = 1;

        // Set the size variables of the field
        if (columns == 10) {
            pointSize = 100;
            maxRow = 10;
            gridSize = 100;
        } else if (columns == 25) {
            pointSize = 40;
            maxRow = 25;
            gridSize = 625;
        } else if (columns == 50) {
            pointSize = 20;
            maxRow = 50;
            gridSize = 2500;
        } else if (columns == 100) {
            pointSize = 10;
            maxRow = 100;
            gridSize = 10000;
        }

        int x = 0;
        int y = 0;
        int count = 0;

        // Start making the points and add them to the grid
        while (count < gridSize) {
            grid.add(new Point(count, x, y));

            if (rowCount == maxRow) {
                y += pointSize;
                x = 0;
                rowCount = 1;
            } else {
                rowCount++;
                x += pointSize;
            }

            count++;
        }
    }

    public void resetField(int columns) {
        /** This function clears the field of any added elements **/
        this.columns = columns;
        lines = new ArrayList<>();
        grid = new ArrayList<>();
        initializeField();
        repaint();
    }

    public void randomSelected (int numbers) {
        /**
         * This methods gets the amount of points that needs to be selected
         * and randomize the points that are going to be selected.
         * Whenever the amount is greater then the grid size it will give a message.
         */
        int selectedCount = 0;
        for (Point point : grid) if (point.getSelected()) selectedCount++;

        if (grid.size() - selectedCount < numbers) {
            System.out.println("Number is greater then field.");
            JOptionPane.showMessageDialog(frame, "Number is greater then field.");

        } else {
            for(int i = 0; i < numbers; i++) {

                int random = (int )(Math.random() * grid.size());

                if (!grid.get(random).getSelected()) {
                    this.selectPointFromInput(random);
                } else {
                    i--;
                }
            }

        }
    }
    private void selectPoint(MouseEvent e) {
        /**
         * This method gets the block coordinates of the point
         * the user clicks on, so it can later be used to paint
         * the graphic another color.
         */
        int rootX = e.getX() - (e.getX() % pointSize);
        int rootY = e.getY() - (e.getY() % pointSize);

        for (Point point : grid) {
            if ((rootX == point.getX()) && (rootY == point.getY())) {
                if (Objects.equals(point.getStatus(), "SELECTED")) {
                    point.resetStatus();
                } else {
                    point.setStatus("SELECTED");
                }
            }
        }

        repaint();
    }

    public void selectPointFromInput(int index) {
        /** This method selects a point when you load in an XML file. **/
//        for (Point point : grid) {
//            if (point.getIndex() == index) {
//                point.setStatus("SELECTED");
//                break;
//            }
//        }
        grid.get(index).setStatus("SELECTED");
        repaint();
    }

    public void paintComponent(Graphics brush) {
        /**
         * This method is the graphics drawing class, it loops
         * over the grid and adds the points, as well as other
         * graphics like lines needed to make the grid UI.
         */
        // Set the background
        brush.setColor(Color.WHITE);
        brush.fillRect(0, 0, 1001, 1001);

        int halfPointSize = pointSize / 2;

        boolean gridNumbers;
        gridNumbers = grid.size() < 1000;

        // Add all the blocks
        for (Point point : grid) {

            if (point.getStatus().equals("SELECTED")) {
                brush.setColor(Color.GREEN);

                brush.fillRect(point.getX() + 1, point.getY() + 1, pointSize, pointSize);

                // Draw the lines between points provided you want to
                if (drawLines) {
                    for (Point linepoint : grid) {
                        if ((linepoint.getStatus().equals("SELECTED"))) {
                            brush.setColor(Color.BLUE);
                            brush.drawLine(point.getX() + halfPointSize, point.getY() + halfPointSize, linepoint.getX() + halfPointSize, linepoint.getY() + halfPointSize);
                        }
                    }
                }

                // Draw the simulation lines indicating the route
                for (Line line : lines) {
                    int x1;
                    int y1;
                    brush.setColor(Color.BLUE);

                    // If the point is the corner of the field, draw from 1000, 1000
                    if (line.getPoint1().getIndex() == 201285) {
                        x1 = line.getPoint1().getX();
                        y1 = line.getPoint1().getY();
                    } else {
                        x1 = line.getPoint1().getX() + halfPointSize;
                        y1 = line.getPoint1().getY() + halfPointSize;
                    }

                    int x2 = line.getPoint2().getX() + halfPointSize;
                    int y2 = line.getPoint2().getY() + halfPointSize;
                    brush.drawLine(x1, y1, x2, y2);


                    brush.drawString(Integer.toString(line.getNumber() + 1), x2 - 10, y2 - 10);
                }


            }

            brush.setColor(Color.GRAY);

            // Draw the grid numbers on the screen (If the size is 10 or 25)
            if (gridNumbers) {
                if (point.getIndex() == 0) {
                    brush.drawString("0", point.getX() + 5, point.getY() + 15);
                } else {
                    brush.drawString(Integer.toString(point.getIndex()), point.getX() + 5, point.getY() + 15);
                }
            }

            // Draw the grid lines
            brush.drawRect(point.getX(), point.getY(), pointSize, pointSize);

        }
    }

    public void drawLine(Point point1, Point point2, int number) {
        /** Make a new Line object and repaint the field to make it show up on the grid. **/
        lines.add(new Line(point1, point2, number));
        repaint();
    }

    public void drawRoute(Route route) {
        /**
         * This method draws all lines that are included
         * in a route. It clears all original lines on the
         * field and adds every one that exists within
         * that route.
         */
        lines = new ArrayList<>();
        int pointCounter = 0;
        Point oldPoint = new Point(209381, 0, 0);

        for (Point point : route.getRouteList()) {
            if (!(point == route.getRouteList().get(0))) {
                drawLine(oldPoint, point, pointCounter);
                oldPoint = point;
                pointCounter++;
            } else {
                oldPoint = point;
            }
        }

        repaint();
    }

    public void removeLine(int index) {
        /** Remove a line from the Lines array. **/
        lines.remove(index);
        repaint();
    }

    public void clearLines() {
        /** Clear not the whole field but only the lines **/
        lines = new ArrayList<>();
    }

    /**
     * Getter and Abstract methods *
     */
    public ArrayList<Point> getGrid() {
        return grid;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        selectPoint(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
