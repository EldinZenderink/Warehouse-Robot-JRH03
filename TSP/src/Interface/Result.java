package Interface;

import Algorithm.SimResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *              #--- Algorithm Results class ---#
 * This will generate a extra JFrame with the algorithm's results.
 * The user gets the options to display the results in the console.
 */
public class Result extends JDialog implements ActionListener {

    private JButton generate;
    private JButton close;

    private String algorithm;
    private double totalDistance;
    private int totalSteps;
    private double totalTime;


    public Result(String algorithm, double totalDistance, int totalSteps, long totalTime) {
        /**
         * Constructor asks for results and displays it in a JDialog. *
         */
        this.algorithm = algorithm;
        this.totalDistance = totalDistance;
        this.totalSteps = totalSteps;
        this.totalTime = totalTime / 1000.0;

        setTitle("Results Simulation");
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setSize(250, 200);

        JLabel algorithmUsed = new JLabel("Algorithm: " + algorithm + "\n");
        algorithmUsed.setPreferredSize(new Dimension(200, 20));
        add(algorithmUsed);


        JLabel time = new JLabel("Total Time: " + this.totalTime + " seconds");
        time.setPreferredSize(new Dimension(200, 20));
        add(time);

        JLabel distance = new JLabel("Total Distance: " + totalDistance);
        distance.setPreferredSize(new Dimension(200, 20));
        add(distance);

        JLabel steps = new JLabel("Total Steps: " + totalSteps);
        steps.setPreferredSize(new Dimension(200, 20));
        add(steps);

        JLabel filler = new JLabel("    ");
        filler.setPreferredSize(new Dimension(200, 20));
        add(filler);

        generate = new JButton("In console");
        add(generate);
        generate.addActionListener(this);

        close = new JButton("Close");
        add(close);
        close.addActionListener(this);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == close) {
            // Close dialog
            setVisible(false);
        } else if (e.getSource() == generate) {
            SimResult result = new SimResult(algorithm, totalDistance, totalSteps, totalTime);
            System.out.println(result);
            JOptionPane.showMessageDialog(this, "Results generated in console.");
            setVisible(false);
        }
    }

}

