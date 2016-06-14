package asrs.Controllers;

import javafx.scene.control.*;
import javafx.scene.control.Label;

public class StepsController {
    public ProgressBar progressBar;
    public Label label;
    private int totalSteps;
    private int currentStep = 0;
    private static final String BLUE_BAR    = "blue-bar";
    private static final String GREEN_BAR  = "green-bar";
    private static final String[] barColorStyleClasses = { BLUE_BAR, GREEN_BAR };

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
        updateLabel();
    }

    public double getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

    public void nextStep() {
        if(currentStep < totalSteps) {
            currentStep = currentStep + 1;
            //System.out.println("Yo: " + (currentStep / totalSteps * 100) + " = " + currentStep + " = " + totalSteps);
            setProgressBar((double) currentStep / (double) totalSteps);
            updateLabel();
        }
    }

    public void previousStep() {
        if(currentStep >= 1) {
            currentStep = currentStep - 1;
            setProgressBar((double) currentStep / (double) totalSteps);
            updateLabel();
        }
    }

    public void reset() {
        setProgressBar(0);
        setCurrentStep(0);
        setTotalSteps(0);
    }

    private void setProgressBar(double percentage) {
        //System.out.println(percentage);
        progressBar.setProgress(percentage);
        if(percentage == 1) {
            setBarStyleClass(GREEN_BAR);
        } else {
            setBarStyleClass(BLUE_BAR);
        }
    }

    private void setBarStyleClass(String barStyleClass) {
        progressBar.getStyleClass().removeAll(barColorStyleClasses);
        progressBar.getStyleClass().add(barStyleClass);
    }

    public void updateLabel() {
        label.setText(currentStep + " / " + totalSteps);
    }
}
