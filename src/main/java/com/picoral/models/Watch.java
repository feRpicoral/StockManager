package com.picoral.models;

import javafx.scene.image.Image;

public class Watch extends Product {

    /*Unique Properties*/
    private String color;
    private String size;

    /*Constructors*/
    public Watch(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, Image image, String imageURL, String color, String size) {
        super(name, ID, price, category, model, brand, warranty, quantity, image, imageURL);
        this.color = color;
        this.size = size;
    }

    public Watch(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, String color, String size) {
        super(name, ID, price, category, model, brand, warranty, quantity);
        this.color = color;
        this.size = size;
    }

    public Watch(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, String imageURL, String color, String size) {
        super(name, ID, price, category, model, brand, warranty, quantity, imageURL);
        this.color = color;
        this.size = size;
    }

    public Watch(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, Image image, String color, String size) {
        super(name, ID, price, category, model, brand, warranty, quantity, image);
        this.color = color;
        this.size = size;
    }

    /*Getters & Setters*/

    /**
     * Get a String array with all the unique properties of a computer
     *
     * @return String array with all the unique properties
     */
    public static String[] getPropertiesArr() {
        return new String[]{"Color", "Size"};
    }

    /**
     * Return the value of a property based on its name
     *
     * @param name Name of the property. Valid names are the ones on the array from getPropertiesArr()
     * @return Null if the property name is invalid, otherwise the value of the property.
     */
    public String getPropertyByName(String name) {

        switch (name.toLowerCase()) {
            case "color":
                return getColor();
            case "size":
                return getSize();
            default:
                return null;
        }

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}