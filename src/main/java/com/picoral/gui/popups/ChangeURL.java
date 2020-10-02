package com.picoral.gui.popups;

import com.picoral.gui.windows.ViewProduct;
import com.picoral.data.DataHandler;
import com.picoral.models.Product;
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
public class ChangeURL {

    /**
     * RemoveBox fxml loader
     */
    private class ChangeURLLayout extends AnchorPane {

        @FXML
        private TextField urlField;

        @FXML
        private Button btnSave;

        @FXML
        private Button btnCancel;

        public ChangeURLLayout() {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/picoral/views/change-url.fxml"));
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
            btnSave.setOnAction(e -> {

                String url = urlField.getText();

                if (product.setImageURL(url)) {
                    //Success
                    dataHandler.updateProduct(product);
                    viewWindow.updateImage();
                    stop();
                } else {
                    urlField.clear();
                    urlField.setPromptText("The url you entered is invalid!");
                }

            });

        }

    }

    private final Stage window;
    private final Product product;
    private final DataHandler dataHandler;
    private final ViewProduct viewWindow;

    /**
     * Main constructor
     *
     * @param dataHandler DataHandler instance - needed to update the image URL on the JSON
     * @param viewWindow  ViewProduct instance - needed to re-show the progress bar
     * @param product     Product     instance - needed to update the product's data
     */
    public ChangeURL(DataHandler dataHandler, ViewProduct viewWindow, Product product) {

        if (product == null) {
            throw new RuntimeException("Product reference is null");
        }

        this.product = product;

        if (dataHandler == null) {
            throw new RuntimeException("DataHandler reference is null");
        }

        this.dataHandler = dataHandler;

        if (viewWindow == null) {
            throw new RuntimeException("ViewProduct reference is null");
        }

        this.viewWindow = viewWindow;

        //Stage and layout initialization
        ChangeURLLayout box = new ChangeURLLayout();
        window = new Stage();

        //Avoid skipping stop handling if closed through the X
        window.setOnCloseRequest(e -> {
            e.consume();
            stop();
        });

        //Window properties
        window.initModality(Modality.APPLICATION_MODAL);
        window.setResizable(false);
        window.setTitle("Change Image URL");
        window.setScene(new Scene(box));

        //Show window
        window.showAndWait();

    }

    private void stop() {
        window.close();
    }

}


