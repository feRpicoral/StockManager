package com.picoral.models;

import javafx.scene.image.Image;

public class Phone extends Product {

    /*Unique Properties*/
    private String os;
    private String color;

    /*Constructors*/
    public Phone(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, Image image, String imageURL, String os, String color) {
        super(name, ID, price, category, model, brand, warranty, quantity, image, imageURL);
        this.os = os;
        this.color = color;
    }

    public Phone(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, String os, String color) {
        super(name, ID, price, category, model, brand, warranty, quantity);
        this.os = os;
        this.color = color;
    }

    public Phone(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, String imageURL, String os, String color) {
        super(name, ID, price, category, model, brand, warranty, quantity, imageURL);
        this.os = os;
        this.color = color;
    }

    public Phone(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, Image image, String os, String color) {
        super(name, ID, price, category, model, brand, warranty, quantity, image);
        this.os = os;
        this.color = color;
    }

    /*Getters & Setters*/

    /**
     * Get a String array with all the unique properties of a computer
     *
     * @return String array with all the unique properties
     */
    public static String[] getPropertiesArr() {
        return new String[]{"OS", "Color"};
    }

    /**
     * Return the value of a property based on its name
     *
     * @param name Name of the property. Valid names are the ones on the array from getPropertiesArr()
     * @return Null if the property name is invalid, otherwise the value of the property.
     */
    public String getPropertyByName(String name) {

        switch (name.toLowerCase()) {
            case "os":
                return getOs();
            case "color":
                return getColor();
            default:
                return null;
        }

    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}