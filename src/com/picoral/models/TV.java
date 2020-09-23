package com.picoral.models;

import javafx.scene.image.Image;

public class TV extends Product {

    /*Properties*/
    private String size;
    private String resolution;

    /*Constructors*/
    public TV(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, Image image, String imageURL, String size, String resolution) {
        super(name, ID, price, category, model, brand, warranty, quantity, image, imageURL);
        this.size = size;
        this.resolution = resolution;
    }

    public TV(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, String size, String resolution) {
        super(name, ID, price, category, model, brand, warranty, quantity);
        this.size = size;
        this.resolution = resolution;
    }

    public TV(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, String imageURL, String size, String resolution) {
        super(name, ID, price, category, model, brand, warranty, quantity, imageURL);
        this.size = size;
        this.resolution = resolution;
    }

    public TV(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, Image image, String size, String resolution) {
        super(name, ID, price, category, model, brand, warranty, quantity, image);
        this.size = size;
        this.resolution = resolution;
    }

    /*Getters & Setters*/

    /**
     * Get a String array with all the unique properties of a computer
     *
     * @return String array with all the unique properties
     */
    public static String[] getPropertiesArr() {
        return new String[]{"Size", "Resolution"};
    }

    /**
     * Return the value of a property based on its name
     *
     * @param name Name of the property. Valid names are the ones on the array from getPropertiesArr()
     * @return Null if the property name is invalid, otherwise the value of the property.
     */
    public String getPropertyByName(String name) {

        switch (name.toLowerCase()) {
            case "size":
                return getSize();
            case "resolution":
                return getResolution();
            default:
                return null;
        }

    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}