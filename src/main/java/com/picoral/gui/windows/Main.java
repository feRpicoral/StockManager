package com.picoral.gui.windows;

import com.picoral.Util;
import com.picoral.core.App;
import com.picoral.data.DataHandler;
import com.picoral.gui.popups.ConfirmBox;
import com.picoral.gui.popups.RemoveBox;
import com.picoral.models.*;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.picoral.Constants.CATEGORIES;

public class Main extends ScrollPane {

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

    @FXML
    private CheckMenuItem btnUseSampleData;

    @FXML
    private Label noImgLabel;

    @FXML
    private ProgressBar imgProgressbar;

    //Constructor with main app as parameter
    public Main(App app, DataHandler dataHandler) {

        if (app == null) {
            throw new RuntimeException("App reference is null");
        }

        this.app = app;

        if (dataHandler == null) {
            throw new RuntimeException("DataHandler reference is null");
        }

        this.dataHandler = dataHandler;

        //Load fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/picoral/views/main.fxml"));
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
        mandatoryFields = new ArrayList<>() {{
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
        category.getItems().addAll(Arrays.asList(CATEGORIES));

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

            return row;
        });

        //Handles key pressed on a selected row
        table.setOnKeyPressed(event -> {

            Product current = table.getSelectionModel().getSelectedItem();

            switch (event.getCode()) {

                case ENTER:

                    new ViewProduct(current, dataHandler);
                    break;

                case DELETE:
                case BACK_SPACE:

                    if (ConfirmBox.getConfirmation("Do you really want delete this product?")) {
                        dataHandler.removeProduct(current);
                    }
                    break;

            }

        });

        //Add product buttons
        //Add button on click
        btnAdd.setOnAction(e -> {
            if (addProduct()) {
                addProductPane.setExpanded(false);
            }
        });

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

        //Use sample data check menu item
        btnUseSampleData.setOnAction(e -> {

            if (btnUseSampleData.isSelected()) {

                dataHandler.loadSampleData();

            } else {

                dataHandler.unloadSampleData();

            }

            addProductPane.setExpanded(false);

        });

        //End of MenuBar buttons

        //Load image preview
        imgURL.textProperty().addListener(observable -> previewImage(((StringProperty) observable).get()));

        //Add listeners to the price and quantity fields, the only two which need data validation
        Util.Listeners.addPriceListener(price);
        Util.Listeners.addQuantityListener(quantity);

        //Listener to add different fields once the category is changed
        Util.Listeners.addCategoryListener(category, addProductPane);

    }

    /**
     * Create a product object based on the current information typed and adds it to the table
     *
     * @return True if the product was created, false otherwise
     */
    private boolean addProduct() {

        //Verify if all the mandatory fields are present
        for (TextField field : mandatoryFields) {
            if (field.getText().isBlank()) {
                return false;
            }
        }

        if (category.getValue() == null) {
            return false;
        }

        Product p;

        //Make sure the price and quantity are within the double/integer possible range
        //TODO Improve validation an give feedback to the user

        try {

            Integer.parseInt(quantity.getText());
            Double.parseDouble(price.getText());

        } catch (Exception ignored) {
            return false;
        }


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
        return true;

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

        //Reset the image preview
        previewImage(null);

    }

    /**
     * Loads the image from the given URL and sets it to the preview's ImageView
     *
     * @param url URL as String of the image
     */
    private void previewImage(String url) {

        //Private class for handling the visibility and text of the labels and progress bar of the image preview
        final class PreviewLabels {

            /**
             * Resets the label & progress bar to the no image state and nulls the image of the
             * ImagePreview
             */
            private void reset() {

                imgPreview.setImage(null);
                imgProgressbar.setProgress(0D);
                noImage();

            }

            /**
             * Sets the label and progress bar to the loading state
             */
            private void loading() {

                noImgLabel.setText("The image is loading...");
                noImgLabel.setVisible(true);
                imgProgressbar.setVisible(true);

            }

            /**
             * Hides the label and progress bar and sets the image preview to the given image
             *
             * @param img JavaFX image to set in the image preview
             */
            private void loaded(Image img) {

                imgPreview.setImage(img);
                noImgLabel.setVisible(false);
                imgProgressbar.setVisible(false);

            }

            /**
             * Sets the label and progress bar to the initial no image state
             */
            private void noImage() {

                noImgLabel.setText("This product has no image");
                noImgLabel.setVisible(true);
                imgProgressbar.setVisible(false);

            }

            /**
             * Sets the label and progress bar to the invalid URL state
             */
            private void invalid() {

                noImgLabel.setText("The URL you entered is invalid");
                noImgLabel.setVisible(true);
                imgProgressbar.setVisible(false);

            }

        }

        PreviewLabels handler = new PreviewLabels();

        handler.reset();

        //If the user deleted the URL just reset the labels and return
        if (url == null || url.isBlank()) {
            return;
        }

        try {

            //Create JavaFX Image from the URL using background loading
            //Note that due to background loading being set to true, isError() will only work after
            //the image was fully loaded, otherwise isError() will return false to things like 'http:'
            Image img = new Image(url, true);

            //Update the labels to the loading state
            handler.loading();

            img.progressProperty().addListener((observable, oldValue, progress) -> {

                if (oldValue.doubleValue() == 0D) {
                    System.out.println("called");
                }

                //Update the progress bar
                imgProgressbar.setProgress(progress.doubleValue());

                //Image finished background loading
                if (progress.doubleValue() == 1D) {

                    //Since we are using background loading isError() is only reliable after the image was fully loaded
                    if (img.isError()) {

                        //The URL does not point to an image
                        handler.invalid();

                    } else {

                        //The URL is valid and we got an image
                        handler.loaded(img);

                    }

                }

            });

        } catch (Exception ignored) {

            //The URL is invalid
            handler.invalid();

        }

    }

    /**
     * Get unique ID as String
     *
     * @return String unique ID
     */
    private String generateUniqueID() {

        int id = ThreadLocalRandom.current().nextInt(0, 10_000);

        while (!isIdUnique(id)) {
            id = ThreadLocalRandom.current().nextInt(0, 10_000);
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
