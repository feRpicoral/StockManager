package com.picoral.controller;

import com.picoral.App;
import com.picoral.models.Product;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MainController extends ScrollPane {

    private final App app;
    private List<TextField> mandatoryFields;
    private final DataHandler dataHandler;

    //FXML variables

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem btnResetData;

    @FXML
    private MenuItem btnAddNew;

    @FXML
    private MenuItem btnRemoveByID;

    @FXML
    private MenuItem btnClose;

    @FXML
    private MenuItem btnAbout;

    @FXML
    private TableView<Product> table;

    @FXML
    private TableColumn<Product, String> nameCol;

    @FXML
    private TableColumn<Product, String> IdCol;

    @FXML
    private TableColumn<Product, Double> priceCol;

    @FXML
    private TableColumn<Product, String> categoryCol;

    @FXML
    private TableColumn<Product, String> modelCol;

    @FXML
    private TableColumn<Product, String> brandCol;

    @FXML
    private TableColumn<Product, String> warrantyCol;

    @FXML
    private TableColumn<Product, Integer> quantityCol;

    @FXML
    private TextField name;

    @FXML
    private TextField price;

    @FXML
    private TextField model;

    @FXML
    private TextField brand;

    @FXML
    private TextField warranty;

    @FXML
    private TextField quantity;

    @FXML
    private TitledPane addProductPane;

    @FXML
    private TextField imgURL;

    @FXML
    private ComboBox<String> category;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnReset;

    @FXML
    private ImageView imgPreview;

    //Constructor with main app as parameter
    public MainController(App app, DataHandler dataHandler) {

        if (app == null) {
            throw new RuntimeException("App reference is null");
        }

        this.app = app;

        if (dataHandler == null) {
            throw new RuntimeException("DataHandler reference is null");
        }

        this.dataHandler = dataHandler;

        //Load fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/main.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @FXML
    void initialize() {

        //Make the table accessible through the DataHandler
        dataHandler.setTable(table);

        //Change table placeholder
        table.setPlaceholder(new Text("You don't have any products yet :("));

        //Populate mandatoryFields list
        mandatoryFields = new ArrayList<>(){{
            add(name);
            add(price);
            add(model);
            add(brand);
            add(warranty);
            add(quantity);
        }};

        //Tie properties to table cols
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        IdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        warrantyCol.setCellValueFactory(new PropertyValueFactory<>("warranty"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        //Populate category combo box with possible categories
        category.getItems().addAll(Arrays.asList(Util.possibleCategories));

        //Add product buttons
        //Add button on click
        btnAdd.setOnAction(this::addProduct);

        //Reset fields button on click
        btnReset.setOnAction(e -> {
            clearFields();
        });

        //MenuBar Buttons
        //Reset Data
        btnResetData.setOnAction(e -> {
            if (ConfirmBox.getConfirmation("Do you really want to delete all the products?")) {
                dataHandler.reset();
            }
        });

        //Close program
        btnClose.setOnAction(e -> {
            e.consume();
            app.stop();
        });

        //Add new product menu button
        btnAddNew.setOnAction(e -> {
            if (addProductPane.isCollapsible()) {
                addProductPane.setExpanded(true);
            }
        });

        //Remove by ID
        btnRemoveByID.setOnAction(e -> {
            new RemoveBox(dataHandler);
        });

        //About program
        btnAbout.setOnAction(e -> {

        });

        //End of MenuBar buttons

        //Load image preview
        imgURL.setOnKeyTyped(e -> {
            previewImage();
        });

        //Assure price is numbers only
        //TODO Improve dot verification
        price.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                int count = 0;
                for (char c : newValue.toCharArray()) {
                    if (c == '.') {
                        count++;
                    }
                }

                if (count > 0) {
                    newValue = newValue.substring(0, newValue.length() - 1);
                }

                if (!newValue.matches("\\d*\\.?\\d*")) {
                    price.setText(newValue.replaceAll("[^\\d*\\.?\\d*]", ""));
                }
            }
        });

        //Assure quantity are numbers only
        quantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    quantity.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });


    }

    private void addProduct(Event e) {

        //Verify if all the mandatory fields are present
        for (TextField field : mandatoryFields) {
            if (field.getText().isBlank()) {
                return;
            }
        }

        if (category.getValue() == null) {
            return;
        }

        Product p = new Product(
                name.getText(),
                generateUniqueID(),
                Double.parseDouble(price.getText()),
                category.getValue(),
                model.getText(),
                brand.getText(),
                warranty.getText(),
                Integer.parseInt(quantity.getText()),
                imgURL.getText()
        );

        dataHandler.addProduct(p);

        clearFields();

    }

    /**
     * Clear all fields and remove the preview image
     */
    private void clearFields() {

        HBox parent = (HBox) name.getParent().getParent();

        for (Node vbox : parent.getChildren()) {

            for (Node vboxChild : ((VBox) vbox).getChildren()) {

                if (vboxChild instanceof TextField) {
                    ((TextField) vboxChild).setText("");
                }

            }

        }


        //Best solution so far to reset the value and keep the promp text
        //Prob there is a better solution, but so far this works fine
        //Create an identical CB, remove the old one and add the new
        VBox comboBoxParent = (VBox) category.getParent();
        ComboBox<String> cb = new ComboBox<>();
        cb.setPromptText(category.getPromptText());
        cb.getItems().setAll(category.getItems());
        cb.setMinWidth(category.getMinWidth());
        comboBoxParent.getChildren().remove(category);
        comboBoxParent.getChildren().add(cb);
        category = cb; //Updated variable

        imgPreview.setImage(null);

    }

    /**
     * Load the image preview when the url is typed
     */
    private void previewImage() {
        if (Util.isURLImage(imgURL.getText())) {
            imgPreview.setImage(new Image(
                    imgURL.getText()
            ));
        } else {
            imgPreview.setImage(null);
        }
    }

    /**
     * Get unique ID as String
     *
     * @return String unique ID
     */
    private String generateUniqueID() {

        int id = ThreadLocalRandom.current().nextInt(0,10_000);

        while (!isIdUnique(id)) {
            id = ThreadLocalRandom.current().nextInt(0,10_000);
        }

        return Integer.toString(id);

    }

    /**
     * Check if the given id is currently being used or not
     *
     * @param id ID as integer
     * @return True if ID is unique
     */
    private boolean isIdUnique(int id) {

        for (Product p : table.getItems()) {
            if (p.getID().equals(Integer.toString(id))) {
                return false;
            }
        }

        return true;

    }

}
