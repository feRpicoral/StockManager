package com.picoral.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javax.imageio.ImageIO;
import java.net.URL;

/**
 * Utility class
 */
public abstract class Util {

    //All possible product categories
    //TODO Move this to an external file or menubar > config
    public static final String[] possibleCategories = new String[]{
            "Smart Phone",
            "Watch",
            "TV",
            "Computer"
    };

    /*
    Path to the JSON where the data will be stored
    The file doesn't need to exist - if it doesn't it will be created
    Change to sample_data.json to quick test with a few products but don't remove them
    */
    public static final String DATA_FILE_PATH = "data.json";

    /**
     * Verify if the given string is a valid URL.
     * Can't return false positives since it tries to open the url.
     * Usage should be as minimal as possible due to performance
     *
     * @param url String
     * @return True if and only if the given string is a valid url
     */
    public static boolean isURLValid(String url) {
        try {
            (new URL(url)).openStream().close();
            return true;
        } catch (Exception ignored) { }

        return false;
    }

    /**
     * Verify if the given string is an image url
     * Should not be used due to huge performance drawback.
     * If used, reduce usage to as low as possible to improve performance
     *
     * @deprecated Consider other possibilities due to performance drawback
     * @param url URL as string to verify if it points to an image
     * @return True if and only if the given string is a valid URL and points to an image; false otherwise
     */
    public static boolean isURLImage(String url) {
        try {
            return isURLValid(url) && ImageIO.read(new URL(url)) != null;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Sorts string array alphabetically using selection sorting
     * Not using Arrays.sort(x) due to university requirements
     *
     * @deprecated Use Arrays.sort(x) instead
     * @param words String array to be sorted
     * @return Sorted array
     */
    public static String[] sortStringArray(String[] words) {

        for (int i = 0; i < words.length; i++) {

            for (int j = i + 1; j < words.length; j++) {

                if (words[i].compareTo(words[j]) > 0) {

                    String temp = words[i];
                    words[i] = words[j];
                    words[j] = temp;

                }

            }

        }

        return words;

    }

    /**
     * Handles data validation listeners
     */
    static class Listeners {

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

    }

}
