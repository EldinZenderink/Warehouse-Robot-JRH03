package asrs;

import asrs.Controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/MainWindow.fxml"));
        Parent root = (Parent)loader.load();
        MainController mainController = (MainController)loader.getController();
        mainController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("ASRS System");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.sizeToScene();
        if(isMatchingResolution()) {
            primaryStage.setFullScreen(true);
        }
        primaryStage.show();
        primaryStage.setResizable(false);
        mainController.setStateLabel("Idle");
    }

    public static void main(String[] args) {
        launch(args);
    }

    private boolean isMatchingResolution() {
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        if(primaryScreenBounds.getWidth() == 1280 && primaryScreenBounds.getHeight() == 720) {
            return true;
        } else {
            return false;
        }
    }
}