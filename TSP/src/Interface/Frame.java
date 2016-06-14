/**
 *         #------ TSP Frame ------#
 * Contains many methods for graphical objects
 * and the actionListener for all buttons.
 */
package Interface;


import Algorithm.Algorithms.Enumeration;
import Algorithm.Algorithms.Greedy;
import Algorithm.Algorithms.RandomGreedy;
import jdk.internal.org.xml.sax.SAXParseException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Frame extends JFrame implements ActionListener {

    private JComboBox selectWidth;
    private JButton change;
    private Field field;
    private JButton enumeration;
    private JButton randomSelected;
    private JTextField numberRandom;
    private JButton greedy;
    private JButton random;
    private JButton loadOrder;

    public Frame() {
        /**
         * In this method we create the frame and add all the GUI
         * elements. This constructor does not require any parameters.
         */

        setTitle("TSP Simulation");

        setLayout(new FlowLayout());
//        GridBagConstraints c = new GridBagConstraints();

        setSize(1020, 1200); // Size of the main frame
//
//        c.insets = new Insets(20, 0, 40, 0);

        /** Option to change the width of Field **/
        JLabel width = new JLabel("Width : ");
//        c.gridx = 11;
//        c.gridy = 0;
//        c.ipadx = 5;
//        add(width, c);
        add(width);

        Integer[] arrayWidth = {10, 25, 50, 100};
        selectWidth = new JComboBox(arrayWidth);
//        c.gridx = 12;
//        add(selectWidth, c);
        add(selectWidth);

        /** Change width **/
        change = new JButton("Change");
//        c.gridx = 13;
//        add(change, c);
        add(change);
        change.addActionListener(this);

        loadOrder = new JButton("Load order...");
        loadOrder.addActionListener(this);
//        c.gridx = 14;
//        add(loadOrder, c);
        add(loadOrder);

        /* Field grid */
        field = new Field(10, this);
//        c.gridx = 0;
//        c.gridy = 0;
//        c.gridwidth = 10;
//        c.gridheight = 10;
//        c.ipadx = 1000;
//        c.ipady = 1000;
//        c.fill = GridBagConstraints.CENTER;
//        add(field, c);


        enumeration = new JButton("Enumeration");
//        c.gridx = 11;
//        c.gridy = 5;
//        c.gridwidth = 2;
//        c.gridheight = 1;
//        c.ipadx = 0;
//        c.ipady = 0;
//        add(enumeration, c);
        add(enumeration);
        enumeration.addActionListener(this);

        greedy = new JButton("Greedy");
//        c.gridx = 13;
//        c.gridy = 5;
//        c.gridwidth = 2;
//        add(greedy, c);
        add(greedy);
        greedy.addActionListener(this);

        random = new JButton("Random");
//        c.gridx = 15;
//        c.gridwidth = 2;
//        c.gridy = 5;
//        add(random, c);
        add(random);
        random.addActionListener(this);

        numberRandom = new JTextField(2);
        add(numberRandom);

        randomSelected = new JButton("Random-Selected");
        add(randomSelected);
        randomSelected.addActionListener(this);


        add(field);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);


    }

    public void getWidth(int Index) {
        if (Index == 0) {
            field.resetField(10);
        } else if (Index == 1) {
            field.resetField(25);
        } else if (Index == 2) {
            field.resetField(50);
        } else {
            field.resetField(100);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == change) {
            int selectedWidth = selectWidth.getSelectedIndex();
            this.getWidth(selectedWidth);

        } else if (e.getSource() == random) {
            RandomGreedy algorithm = new RandomGreedy(field);
            algorithm.getResult();

        } else if (e.getSource() == greedy) {
            Greedy algorithm = new Greedy(field);
            algorithm.getResult();

        } else if (e.getSource() == enumeration) {
            Enumeration algorithm = new Enumeration(field);
            algorithm.getResult();

        } else if (e.getSource() == loadOrder) {
            if (!getXMLOrder()) {
                JOptionPane.showMessageDialog(this, "Failed to load order.");
            }
        } else if(e.getSource() == randomSelected) {
            try {
                int number = Integer.parseInt(numberRandom.getText());
                field.randomSelected(number);
            } catch (NumberFormatException nfe) {
                System.out.println("Value most be a number.");
            } catch (Exception exc) {
                System.out.println("Something went wrong.");
            }
        }
    }

    private boolean getXMLOrder() {
        try {
            JFileChooser fileOpener = new JFileChooser();
            fileOpener.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileOpener.setFileFilter(new FileNameExtensionFilter("XML files (*.xml)", "xml"));

            if (fileOpener.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                Order xml = new Order(fileOpener.getSelectedFile());
                System.out.println(xml.getCustomer().getLastname());
                System.out.println(xml.getDatum());
                for (int i = 0; i < xml.getArtikelnummer().size(); i++) {
                    System.out.println(xml.getArtikelnummer().get(i));
                    field.selectPointFromInput(Integer.parseInt(xml.getArtikelnummer().get(i)));
                }
            }

            return true;
        } catch (NullPointerException ex) {
            return false;
        } catch (SAXParseException ex) {
            return false;
        }
    }
}
