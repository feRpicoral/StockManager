package com.picoral.controller;

import com.picoral.models.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the view more about product window
 */
public class ViewProduct {

    /**
     * ViewProduct layout handler
     */
    private class ViewProductLayout extends AnchorPane {


        @FXML
        private TextField nameField;

        @FXML
        private TextField idField;

        @FXML
        private TextField priceField;

        @FXML
        private TextField categoryField;

        @FXML
        private TextField modelField;

        @FXML
        private TextField brandField;

        @FXML
        private TextField warrantyField;

        @FXML
        private TextField quantityField;

        @FXML
        private Button btnEdit;

        @FXML
        private Button btnSave;

        @FXML
        private ImageView imageView;

        @FXML
        private Label noImgLabel;

        @FXML
        private VBox parent;

        public ViewProductLayout() {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/product.fxml"));
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

            //TODO Allow the product's image url to be edited

            //Add listeners to the price and quantity fields, the only two which need data validation
            Util.Listeners.addPriceListener(priceField);
            Util.Listeners.addQuantityListener(quantityField);

            //Update fields values to the actual product info
            nameField.setText(product.getName());
            idField.setText(product.getID());
            priceField.setText(Double.toString(product.getPrice()));
            categoryField.setText(product.getCategory());
            modelField.setText(product.getModel());
            brandField.setText(product.getBrand());
            warrantyField.setText(product.getWarranty());
            quantityField.setText(Integer.toString(product.getQuantity()));

            //Set product image
            if (product.hasImage()) {
                noImgLabel.setVisible(false);
                imageView.setImage(product.getImage());
            }

            //Edit button on click
            btnEdit.setOnAction(e -> {

                //Make the fields editable (not disabled) and show the save button
                btnSave.setVisible(true);
                changeTextFieldDisabledState(false);

            });

            //Save button on click
            btnSave.setOnAction(e -> {

                //Hide the button and set the fields as disables (non editable)
                btnSave.setVisible(false);
                changeTextFieldDisabledState(true);

                //Update the value
                product.setName(nameField.getText());
                product.setPrice(Double.parseDouble(priceField.getText()));
                product.setCategory(categoryField.getText());
                product.setModel(modelField.getText());
                product.setBrand(brandField.getText());
                product.setWarranty(warrantyField.getText());
                product.setQuantity(Integer.parseInt(quantityField.getText()));

                //Save the new values and update the table
                dataHandler.updateProduct(product);

            });

        }

        /**
         * Change the text fields disabled stated based on the state parameter
         *
         * @param state New disabled state for all the text fields
         */
        private void changeTextFieldDisabledState(boolean state) {
            for (Node hb : parent.getChildren()) {

                if (hb instanceof HBox) {

                    for (Node tf : ((HBox) hb).getChildren()) {

                        if (tf instanceof TextField) {

                            if (!tf.getId().equals("idField")) {
                                tf.setDisable(state);
                            }

                        }

                    }

                }

            }
        }

    }

    Stage window;
    DataHandler dataHandler;
    Product product;

    public ViewProduct(Product product, DataHandler dataHandler) {

        if (dataHandler == null) {
            throw new RuntimeException("Data Handler reference is null");
        }

        this.dataHandler = dataHandler;

        if (product == null) {
            throw new RuntimeException("Product reference is null");
        }

        this.product = product;

        //Stage and layout initialization
        AnchorPane vp = new ViewProductLayout();
        window = new Stage();

        //Avoid skipping stop handling if closed through the X
        window.setOnCloseRequest(e -> {
            e.consume();
            stop();
        });

        //Window properties
        window.setResizable(false);
        window.setTitle(product.getName());
        window.setScene(new Scene(vp));

        //Show window
        window.show();

    }

    private void stop() {
        window.close();
    }

}
