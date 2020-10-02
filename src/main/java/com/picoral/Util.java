package com.picoral;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;
import java.util.List;

/**
 * Utility class
 */
public abstract class Util {

    /**
     * Handles data validation listeners
     */
    public static class Listeners {

        /**
         * Adds to the given text field data validation to ensure only doubles with
         * two or less digits after the dot can be inserted
         *
         * @param priceField JavaFX TextField to add the said listener
         */
        public static void addPriceListener(TextField priceField) {

            priceField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                    if (newValue.contains(".")) {

                        int counter = 0;
                        for (char c : oldValue.toCharArray()) {
                            if (c == '.') {
                                counter++;
                            }
                        }

                        if (counter == 0) {
                            priceField.setText(newValue.replaceAll("[^\\d+\\.+[0-9]{1,2}]", ""));
                        } else {

                            int dotIndex = newValue.indexOf(".");
                            String beforeDot = newValue.substring(0, dotIndex);
                            String afterDot = newValue.substring(dotIndex + 1);
                            afterDot = afterDot.replaceAll("\\D", "");

                            if (afterDot.length() > 2) {
                                afterDot = String.valueOf(afterDot.charAt(0)) + afterDot.charAt(1);
                            }

                            priceField.setText(beforeDot + "." + afterDot);

                        }

                    } else {

                        priceField.setText(newValue.replaceAll("\\D", ""));

                    }

                }
            });

        }

        /**
         * Adds to the given text field data validation to ensure only integers can be inserted
         *
         * @param quantityField JavaFX TextField to add the said listener
         */
        public static void addQuantityListener(TextField quantityField) {

            quantityField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if (!newValue.matches("\\d*")) {
                        quantityField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });

        }


        /**
         * List of the added fields based on the selected category.
         * Needed to be able to remove the fields once the category is changed.
         */
        private static List<TextField> addedFields = new LinkedList<>();

        /**
         * Getter for addedFields list
         */
        public static List<TextField> getAddedFields() {
            return addedFields;
        }

        /**
         * Reset the added fields for unique properties based on the product's category
         */
        public static void resetAddedFields() {
            addedFields.forEach(field -> {
                try {
                    VBox parent = (VBox) field.getParent();
                    parent.getChildren().remove(field);
                } catch (Exception ignored) {
                }
            });
            addedFields = new LinkedList<>();
        }

        /**
         * Adds to the given ComboBox change listeners to add or remove fields
         * based on the selected category
         *
         * @param categoryBox ComboBox category CB reference
         */
        public static void addCategoryListener(ComboBox<String> categoryBox, TitledPane titledPane) {

            categoryBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

                    VBox rightVB = (VBox) categoryBox.getParent();
                    VBox leftVB = (VBox) rightVB.getParent().getChildrenUnmodifiable().get(0);

                    resetAddedFields();

                    double minWidth = ((TextField) leftVB.getChildren().get(0)).getMinWidth();

                    TextField colorField, sizeField, resField, osField;

                    switch (newValue) {

                        case "Computer":

                            TextField ramField = new TextField();
                            TextField gpuField = new TextField();
                            TextField cpuField = new TextField();
                            TextField storageTypeField = new TextField();

                            ramField.setMinWidth(minWidth);
                            gpuField.setMinWidth(minWidth);
                            cpuField.setMinWidth(minWidth);
                            storageTypeField.setMinWidth(minWidth);

                            ramField.setPromptText("RAM");
                            gpuField.setPromptText("GPU");
                            cpuField.setPromptText("CPU");
                            storageTypeField.setPromptText("Storage Type");

                            leftVB.getChildren().addAll(cpuField, ramField);
                            rightVB.getChildren().addAll(gpuField, storageTypeField);

                            addedFields = new LinkedList<>() {{
                                add(ramField);
                                add(gpuField);
                                add(cpuField);
                                add(storageTypeField);
                            }};
                            break;

                        case "TV":

                            sizeField = new TextField();
                            resField = new TextField();

                            sizeField.setMinWidth(minWidth);
                            resField.setMinWidth(minWidth);

                            sizeField.setPromptText("Size");
                            resField.setPromptText("Resolution");

                            leftVB.getChildren().add(sizeField);
                            rightVB.getChildren().add(resField);

                            addedFields = new LinkedList<>() {{
                                add(sizeField);
                                add(resField);
                            }};

                            break;

                        case "Watch":

                            colorField = new TextField();
                            sizeField = new TextField();

                            colorField.setMinWidth(minWidth);
                            sizeField.setMinWidth(minWidth);

                            colorField.setPromptText("Color");
                            sizeField.setPromptText("Size");

                            leftVB.getChildren().add(colorField);
                            rightVB.getChildren().add(sizeField);

                            addedFields = new LinkedList<>() {{
                                add(colorField);
                                add(sizeField);
                            }};

                            break;

                        case "Phone":

                            osField = new TextField();
                            colorField = new TextField();

                            osField.setMinWidth(minWidth);
                            colorField.setMinWidth(minWidth);

                            osField.setPromptText("OS");
                            colorField.setPromptText("Color");

                            leftVB.getChildren().add(osField);
                            rightVB.getChildren().add(colorField);

                            addedFields = new LinkedList<>() {{
                                add(osField);
                                add(colorField);
                            }};

                            break;

                        default:
                            throw new RuntimeException("InvalidCategoryException");

                    }

                }
            });

        }


    }

}
