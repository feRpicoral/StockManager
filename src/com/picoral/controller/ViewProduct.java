package com.picoral.controller;

import com.picoral.models.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
        private Label name;

        @FXML
        private Label id;

        @FXML
        private Label price;

        @FXML
        private Label category;

        @FXML
        private Label model;

        @FXML
        private Label brand;

        @FXML
        private Label warranty;

        @FXML
        private Label quantity;

        @FXML
        private ImageView imageView;

        @FXML
        private Label noImgLabel;

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

            name.setText(product.getName());
            id.setText(product.getID());
            price.setText(Double.toString(product.getPrice()));
            category.setText(product.getCategory());
            model.setText(product.getModel());
            brand.setText(product.getBrand());
            warranty.setText(product.getWarranty());
            quantity.setText(Integer.toString(product.getQuantity()));

            if (product.hasImage()) {
                noImgLabel.setVisible(false);
                imageView.setImage(product.getImage());
            }


        }

    }

    Stage window;
    Product product;

    public ViewProduct(Product product) {

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
