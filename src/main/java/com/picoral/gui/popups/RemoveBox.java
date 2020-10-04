package com.picoral.gui.popups;

import com.picoral.core.App;
import com.picoral.data.DataHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the pop-up product removal box.
 */
public class RemoveBox {

    /**
     * RemoveBox fxml loader
     */
    private class RemoveBoxLayout extends AnchorPane {

        @FXML
        private TextField idField;

        @FXML
        private Button btnRemove;

        @FXML
        private Button btnCancel;

        public RemoveBoxLayout() {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/picoral/views/remove.fxml"));
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

            //Close button
            btnCancel.setOnAction(e -> {
                stop();
            });

            //Remove product button
            btnRemove.setOnAction(e -> {

                String id = idField.getText();
                String msg = String.format("Do you really want to delete the product with the ID %s?", id);

                //Call confirmation box
                if (ConfirmBox.getConfirmation(msg)) {
                    dataHandler.removeProduct(id);
                    stop();
                } else {
                    idField.clear();
                }

            });

        }

    }

    private final Stage window;
    private final DataHandler dataHandler = App.dataHandler;

    public RemoveBox() {

        //Stage and layout initialization
        AnchorPane rb = new RemoveBoxLayout();
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
        window.setScene(new Scene(rb));

        //Show window
        window.showAndWait();

    }

    private void stop() {
        window.close();
    }

}


