package asrs.Controllers;

import asrs.Models.*;
import asrs.Serial.Serial;
import asrs.Utils.BinPlacer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.internal.org.xml.sax.SAXParseException;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public BarChart leftBar;
    public BarChart rightBar;
    public TextArea mainLog;
    public Label lblCurrentState;
    public Button btnReset;
    public Button btnPreviousStep;
    public Button btnStart;
    public Button btnNextStep;
    public AnchorPane warehouse;
    private Stage primaryStage;
    private Location lcBin1 = new Location(6,1);
    private Location lcBin2 = new Location(7,1);
    private Bin leftBin = new Bin(10, lcBin1);
    private Bin rightBin = new Bin(18, lcBin2);
    private boolean binArrived = false;
    private static final String PAUSED = "paused";
    private static final String PLAYING  = "playing";
    private static final String MOVING  = "moving";
    private static final String IDLE  = "idle";
    private boolean useSerial = true;
    private Bin leftBinQueue;
    private Bin rightBinQueue;
    private String state;
    private String robotState;
    private Order order;
    private ArrayList<Product> queue;
    private int currentProductIndex;
    @FXML
    private WarehouseController warehouseController;
    @FXML
    private HBox steps;
    @FXML
    private StepsController stepsController;

    private Serial serial;

    /**
     * This method is called after the view is initialized
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initBars();
        stepsController.reset();
        state = PAUSED;
        robotState = IDLE;
        reset();
        /**
         * Disable the Serial on Robins computer
         */
        try {
            if(InetAddress.getLocalHost().getHostName().contains("Robin")) {
                useSerial = false;
            }
            System.out.println(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if(useSerial) {
            log("Using serial");
            serial = new Serial(this);
            serial.runServer();
            serial.setupArduino("Sanya", "COM4", 115200);
            serial.setupArduino("Erica", "COM3", 115200);
            warehouseController.setSerial(serial);
        } else {
            log("Not using serial");
        }
    }

    /**
     * Set the primary state for selecting a file
     * @param primaryStage
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Initialize bin bars visualization
     */
    private void initBars() {
        leftBar.setLegendVisible(false);
        rightBar.setLegendVisible(false);
        leftBar.setAnimated(false);
        rightBar.setAnimated(false);
        setLeftBar(0);
        setRightBar(0);
    }

    /**
     * Start the loop of the robot picker
     */
    private void startWarehouseThread() {
        /**
         * Create a new teask
         */
        Task pickProduct = new Task<Void>() {
            @Override public Void call() {
                /**
                 * While state of the program is PLAYING
                 * and there are products left in the queue
                 */
                while(currentProductIndex < order.getProducts().size() && state.equals(PLAYING)) {
                    Product currentProduct;
                    Location location;
                    int delay;
                    Bin bin;
                    String pos;

                    Platform.runLater(() -> {
                        //System.out.println(order.getProducts().size() + " -__- " + currentProductIndex);
                        log("Started thread");
                    });

                    /**
                     * Set the first product if currentIndex is empty (first time of loop)
                     */
                    if (currentProductIndex < 0) {
                        Platform.runLater(() -> {
                            log("Current product is null, set current product");
                        });
                        currentProductIndex = 0;
                    }
                    currentProduct = order.getProducts().get(currentProductIndex);

                    /**
                     * Move the robot to the product location and pick the product
                     */
                    delay = warehouseController.robotMove(currentProduct.getLocation());
                    if(useSerial) {
                        serial.sendMessageToSanya("POS_" + currentProduct.getLocation().getX() + "" + currentProduct.getLocation().getY());
                        robotState = MOVING;
                    }

                    /**
                     * Wait for the robot
                     */
                    try {
                        Platform.runLater(() -> {
                            log("Sleeping");
                        });
                        Thread.sleep(delay);
                    } catch (InterruptedException interrupted) {
                        if (isCancelled()) {
                            updateMessage("Cancelled");
                            Platform.runLater(() -> {
                                log("Cancelled");
                            });
                        }
                    }
                    while (!robotState.equals(IDLE)) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException interrupted) {}
                    }
                    Platform.runLater(() -> {
                        stepsController.nextStep();
                        log("Robot moved to location");
                    });

                    /**
                     * Check bin
                     */
                    if(leftBinQueue.getProducts().contains(currentProduct)) {
                        bin = leftBin;
                        pos = "left";
                    } else {
                        bin = rightBin;
                        pos = "right";
                    }

                    /**
                     * Move he robot to the bin calculated by the algorithm
                     */
                    delay = warehouseController.robotMove(bin.getLocation());
                    if(useSerial) {
                        serial.sendMessageToSanya("POS_" + bin.getLocation().getX() + "" + bin.getLocation().getY());
                        robotState = MOVING;
                    }
                    Platform.runLater(() -> {
                        log("Moving to bin");
                    });

                    /**
                     * Wait for the robot
                     */
                    try {
                        Platform.runLater(() -> {
                            log("Sleep");
                        });
                        Thread.sleep(delay);
                    } catch (InterruptedException interrupted) {
                        if (isCancelled()) {
                            updateMessage("Cancelled");
                            Platform.runLater(() -> {
                                log("Cancelled");
                            });
                        }
                    }
                    while (!robotState.equals(IDLE)) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException interrupted) {}
                    }
                    Platform.runLater(() -> {
                        stepsController.nextStep();
                        log("Robot moved to bin");
                    });

                    /**
                     * Empty bin
                     */
                    Platform.runLater(() -> {
                        //System.out.println(order.getProducts().size() + " ----- " + currentProductIndex);
                        if(pos == "left") {
                            setLeftBar(bin.getFullnessInPercentage());
                            if(leftBin.getFreeSpace() < order.getProducts().get(currentProductIndex).getSize()) {
                                toggleState();
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Empty left bin");
                                alert.setHeaderText("Left bin is full.");
                                alert.setContentText("Press OK after empty");
                                alert.showAndWait();
                                leftBin.emptyBin();
                            }
                            leftBin.addPacket(order.getProducts().get(currentProductIndex), "e");
                            log("Product dropped in left bin");
                        } else if(pos == "right") {
                            setRightBar(bin.getFullnessInPercentage());
                            if(rightBin.getFreeSpace() < order.getProducts().get(currentProductIndex).getSize()) {
                                toggleState();
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Empty right bin");
                                alert.setHeaderText("Right bin is full.");
                                alert.setContentText("Press OK after empty");
                                alert.showAndWait();
                                rightBin.emptyBin();
                            }
                            rightBin.addPacket(order.getProducts().get(currentProductIndex), "e");
                            log("Product dropped in right bin");
                        }
                    });
                    if (order.getProducts().size() > currentProductIndex) {
                        currentProductIndex = currentProductIndex + 1;
                    }
                }
                Platform.runLater(() -> {
                    if (state.equals(PLAYING)) {
                        toggleState();
                    }
                });
                return null;
            }
        };
        new Thread(pickProduct).start();
    }

    public void setLeftBar(Integer percentage) {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("", percentage));
        leftBar.getData().setAll(series);
    }

    public void setRightBar(Integer percentage) {
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("", percentage));
        rightBar.getData().setAll(series);
    }

    public void onOpenOrderClick(ActionEvent actionEvent) {
        File chosenFile = openFile();
        if(!chosenFile.equals(null)) {
            loadOrder(chosenFile);
        }
    }

    /**
     * Open the file selector in the mainStage
     * @return
     */
    public File openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("XML files (*.xml)", "xml"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null) {
            return null;
        }
        String fileLocation = file.getAbsolutePath();
        log(fileLocation + " opened.");
        return file;
    }

    /**
     * Load the selected file from the file selector
     * @param file
     */
    public void loadOrder(File file) {
        /**
         * Reset the program of previous orders
         */
        reset();
        try {
            /**
             * Generate order of given file
             */
            order = new Order(file);

            /**
             * Check if the products are present in the database
             */
            ArrayList<Product> notExisting = order.getNotExisting();
            if(notExisting.size() > 0) {
                String wrongIds = "";
                for(Product p: notExisting) {
                    wrongIds += p.getProductId() + ", ";
                }
                wrongIds = wrongIds.substring(0, wrongIds.length()-2);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                if (notExisting.size() == 1) {
                    alert.setHeaderText("A requested product is not in storage");
                    alert.setContentText("productID: " + wrongIds);
                } else {
                    alert.setHeaderText("Some requested products are not in storage");
                    alert.setContentText("productIDs: " + wrongIds);
                }
                alert.showAndWait();
            }
            log(String.valueOf(order));

            /**
             * Set the path visualisation on the warehouse
             */
            warehouseController.setRoute(order.getLocations());

            /**
             * Create bin queue
             */
            BinPlacer binPlacer = new BinPlacer(leftBin.getLocation(), rightBin.getLocation());
            binPlacer.setOrder(order);

            /**
             * Set the content of the left and right bin by algorithm
             */
            leftBinQueue = binPlacer.getLeftBin();
            rightBinQueue = binPlacer.getRightBin();
            btnStart.setDisable(false);
            if(useSerial) {
                serial.sendMessageToSanya("RESTART");
                robotState = MOVING;
            }
            setStateLabel("Waiting for start");

            /**
             * Set the progressbar
             */
            stepsController.setTotalSteps(order.getProducts().size() * 2);
        } catch (SAXParseException se) {
            System.out.println("SAXParseException");
            System.out.println(se);
            reset();
        } catch (Exception e) {
            System.out.println(e);
            reset();
        }
    }

    /**
     * Resets the program
     */
    public void reset() {
        disableControls();
        stepsController.reset();
        warehouseController.reset();
        order = null;
        leftBinQueue = null;
        rightBinQueue = null;
        state = PAUSED;
        btnStart.setText("START");
        setStateLabel("Idle");
    }

    /**
     * Disable the previous, play and next controls
     */
    public void disableControls() {
        btnNextStep.disableProperty().set(true);
        btnPreviousStep.disableProperty().set(true);
        btnStart.disableProperty().set(true);
    }

    /**
     * Generate the packinglists off the bins
     * @param actionEvent
     */
    public void generatePackingList(ActionEvent actionEvent) {
        System.out.println("Left bin: " + leftBin.getProducts());
        System.out.println("Right bin" + rightBin.getProducts());
        //warehouse.goToNext();
    }

    /**
     * Add a message to the log in the programm
     * @param log
     * @param newLine
     */
    public void log(String log, boolean newLine) {
        mainLog.setText(mainLog.getText() + (newLine ? "\n\r" : " ") + log);
        mainLog.positionCaret(mainLog.getLength());
    }

    public void log(String log) {
        log(log, true);
    }

    /**
     * Set the state label of the program
     * @param strState
     */
    public void setStateLabel(String strState) {
        lblCurrentState.setText(strState);
    }

    public void nextStep(ActionEvent actionEvent) {
        stepsController.nextStep();
    }

    public void previousStep(ActionEvent actionEvent) {
        stepsController.previousStep();
    }

    public void onResetButton(ActionEvent actionEvent) {
        log("Reset by user");
        reset();
    }

    public void onToggleStateClick(ActionEvent actionEvent) {
        toggleState();
    }

    /**
     * Play or Pause the application
     */
    public void toggleState() {
        System.out.println("YESYESYES");
        if(state.equals(PLAYING)) {
            // PAUSE
            log("Paused");
            setStateLabel("Paused");
            state = PAUSED;
            btnStart.setText("RESUME");
            btnNextStep.setDisable(false);
            btnPreviousStep.setDisable(false);
            if(useSerial) {
                serial.sendMessageToSanya("STOP");
                serial.sendMessageToErica("STOP");
            }
        } else if(state.equals(PAUSED)) {
            // PLAY
            log("Playing");
            setStateLabel("Playing");
            state = PLAYING;
            btnStart.setText("PAUSE");
            btnNextStep.setDisable(true);
            btnPreviousStep.setDisable(true);
            startWarehouseThread();

        }
    }

    /**
     * This method is called if a message is received from
     * the Sanya arduino
     * @param m
     */
    public void messageFromSanya(String m){
        System.out.println(m);
        if(m.contains("ARRIVED AT BIN")){
            serial.sendMessageToErica("DROP");
            binArrived = true;
        } else if(m.contains("ARRIVED AT Y") && !binArrived){
            serial.sendMessageToErica("PICK UP");
        }else if(m.contains("RESTARTED")){
            robotState = MOVING;
        }
    }

    /**
     * This method is called if a message is received from
     * the Erica arduino
     * @param m
     */
    public void messageFromErica(String m){
        System.out.println(m);
        if(m.contains("RETRACTED")){
            System.out.println("ASRS: RETRACTED");
            robotState = IDLE;
        }
        if(m.contains("RETRACTED") && binArrived){
            System.out.println("ASRS: RETRACTED");
            robotState = IDLE;
            binArrived = false;
        }
    }

    public void sanyaStatus(String m){
        System.out.println(m);
    }

    public void ericaStatus(String m){
        System.out.println(m);
    }

    public boolean isUseSerial() {
        return useSerial;
    }
}
