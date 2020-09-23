package com.picoral.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the pop-up confirm box
 */
public class ConfirmBox {

    /**
     * Confirm box fxml loader
     */
    private class ConfirmBoxLayout extends AnchorPane {

        @FXML
        private Text text;

        @FXML
        private Button btnYes;

        @FXML
        private Button btnNo;

        private final String msg;

        public ConfirmBoxLayout(String msg) {

            this.msg = msg;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/confirm.fxml"));
            loader.setRoot(this);
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

        }

        @FXML
        void initialize() {

            //Update confirm message
            if (!msg.isBlank()) {
                text.setText(msg);
            }

            //Set focus to No
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    btnNo.requestFocus();
                }
            });


            btnNo.setOnAction(e -> {
                answer = false;
                stop();
            });

            btnYes.setOnAction(e -> {
                answer = true;
                stop();
            });

        }

    }

    boolean answer;
    Stage window;

    private ConfirmBox(String msg) {

        //Stage and layout initialization
        AnchorPane cb = new ConfirmBoxLayout(msg);
        window = new Stage();

        //Avoid skipping stop handling if closed through the X
        window.setOnCloseRequest(e -> {
            e.consume();
            stop();
        });

        //Window properties
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setTitle("Remove Product by ID");
        window.setScene(new Scene(cb));

        //Show window
        window.showAndWait();

    }

    private void stop() {
        window.close();
    }

    private boolean getAnswer() {
        return answer;
    }

    /**
     * Calls a confirmation box with the given message and returns the answer
     *
     * @param msg Message to be displayed to the user
     * @return True if the user chooses 'Yes' and False otherwise
     */
    public static boolean getConfirmation(String msg) {
        return new ConfirmBox(msg).getAnswer();
    }

    /**
     * Calls a confirmation box with the default message and returns the answer
     *
     * @return True if the user chooses 'Yes' and False otherwise
     */
    public static boolean getConfirmation() {
        return getConfirmation("");
    }

}
