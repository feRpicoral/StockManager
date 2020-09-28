package com.picoral.controller;

import com.picoral.App;
import com.picoral.models.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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

        //Make sure the titled pane starts collapsed
        addProductPane.setExpanded(false);

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
        nameCol.    setCellValueFactory(new PropertyValueFactory<>("name"     ));
        IdCol.      setCellValueFactory(new PropertyValueFactory<>("ID"       ));
        priceCol.   setCellValueFactory(new PropertyValueFactory<>("price"    ));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category" ));
        modelCol.   setCellValueFactory(new PropertyValueFactory<>("model"    ));
        brandCol.   setCellValueFactory(new PropertyValueFactory<>("brand"    ));
        warrantyCol.setCellValueFactory(new PropertyValueFactory<>("warranty" ));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity" ));

        //Populate category combo box with possible categories
        String[] categoriesSorted = Util.sortStringArray(Util.possibleCategories);
        category.getItems().addAll(Arrays.asList(categoriesSorted));

        //ContextMenu for rows
        ContextMenu cm = new ContextMenu(){{
            getItems().add(new MenuItem("Edit"     ));
            getItems().add(new MenuItem("See more" ));
            getItems().add(new MenuItem("Remove"   ));
        }};

        //Handles right click on a row
        table.setRowFactory(tableView -> {
            TableRow<Product> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.SECONDARY) {

                    if (cm.isShowing()) {
                        cm.hide();
                    }

                    cm.show(tableView, event.getScreenX(), event.getScreenY());

                    //Menu handling
                    //Edit
                    cm.getItems().get(0).setOnAction(e -> {

                        new ViewProduct(row.getItem(), dataHandler, true);

                    });

                    //See more
                    cm.getItems().get(1).setOnAction(e -> {
                        new ViewProduct(row.getItem(), dataHandler);
                    });

                    //Remove
                    cm.getItems().get(2).setOnAction(e -> {
                        if (ConfirmBox.getConfirmation("Do you really want delete this product?")) {
                            dataHandler.removeProduct(row.getItem());
                        }
                    });

                } else {
                    //Clicked outside the menu
                    cm.hide();

                    //Clicked twice on row
                    if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        new ViewProduct(row.getItem(), dataHandler);
                    }
                }
            });

            return row ;
        });

        //Handles key pressed on a selected row
        table.setOnKeyPressed(event -> {

            Product current = table.getSelectionModel().getSelectedItem();

            switch (event.getCode()) {

                case ENTER:

                    new ViewProduct(current, dataHandler);
                    break;

                case DELETE: case BACK_SPACE:

                    if (ConfirmBox.getConfirmation("Do you really want delete this product?")) {
                        dataHandler.removeProduct(current);
                    }
                    break;

            }

        });

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

        //End of MenuBar buttons

        //Load image preview
        imgURL.setOnKeyTyped(e -> {
            previewImage();
        });

        //Add listeners to the price and quantity fields, the only two which need data validation
        Util.Listeners.addPriceListener(price);
        Util.Listeners.addQuantityListener(quantity);

        //Listener to add different fields once the category is changed
        Util.Listeners.addCategoryListener(category, addProductPane);

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

        Product p;

        List<TextField> addedFields = Util.Listeners.getAddedFields();

        switch (category.getValue()) {

            case "Computer":

                p = new Computer(
                        name.getText(),
                        generateUniqueID(),
                        Double.parseDouble(price.getText()),
                        category.getValue(),
                        model.getText(),
                        brand.getText(),
                        warranty.getText(),
                        Integer.parseInt(quantity.getText()),
                        imgPreview.getImage(),
                        imgURL.getText(),
                        addedFields.get(0).getText(),
                        addedFields.get(1).getText(),
                        addedFields.get(2).getText(),
                        addedFields.get(3).getText()
                );

                break;

            case "TV":

                p = new TV(
                        name.getText(),
                        generateUniqueID(),
                        Double.parseDouble(price.getText()),
                        category.getValue(),
                        model.getText(),
                        brand.getText(),
                        warranty.getText(),
                        Integer.parseInt(quantity.getText()),
                        imgPreview.getImage(),
                        imgURL.getText(),
                        addedFields.get(0).getText(),
                        addedFields.get(1).getText()
                );

                break;

            case "Watch":

                p = new Watch(
                        name.getText(),
                        generateUniqueID(),
                        Double.parseDouble(price.getText()),
                        category.getValue(),
                        model.getText(),
                        brand.getText(),
                        warranty.getText(),
                        Integer.parseInt(quantity.getText()),
                        imgPreview.getImage(),
                        imgURL.getText(),
                        addedFields.get(0).getText(),
                        addedFields.get(1).getText()
                );

                break;

            case "Phone":

                p = new Phone(
                        name.getText(),
                        generateUniqueID(),
                        Double.parseDouble(price.getText()),
                        category.getValue(),
                        model.getText(),
                        brand.getText(),
                        warranty.getText(),
                        Integer.parseInt(quantity.getText()),
                        imgPreview.getImage(),
                        imgURL.getText(),
                        addedFields.get(0).getText(),
                        addedFields.get(1).getText()
                );

                break;

            default:
                throw new RuntimeException("InvalidCategoryException");

        }

        dataHandler.addProduct(p);
        clearFields();

    }

    /**
     * Clear all fields and remove the preview image
     */
    private void clearFields() {

        Util.Listeners.resetAddedFields();

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

        Util.Listeners.addCategoryListener(cb, addProductPane);

        imgPreview.setImage(null);

    }

    /**
     * Load the image preview when the url is typed
     */
    private void previewImage() {

        try {
            Image img = new Image(imgURL.getText());
            imgPreview.setImage(img);

        } catch (Exception ignored) {}

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
