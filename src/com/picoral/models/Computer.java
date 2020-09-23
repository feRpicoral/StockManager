package com.picoral.models;

import javafx.scene.image.Image;

public class Computer extends Product {

    /*Properties*/
    private String ram;
    private String gpu;
    private String cpu;
    private String storageType;

    /*Calls to base class' constructors*/

    public Computer(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, Image image, String imageURL, String ram, String gpu, String cpu, String storageType) {
        super(name, ID, price, category, model, brand, warranty, quantity, image, imageURL);

        this.ram = ram;
        this.gpu = gpu;
        this.cpu = cpu;
        this.storageType = storageType;
    }

    public Computer(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, String ram, String gpu, String cpu, String storageType) {
        super(name, ID, price, category, model, brand, warranty, quantity);

        this.ram = ram;
        this.gpu = gpu;
        this.cpu = cpu;
        this.storageType = storageType;
    }

    public Computer(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, String imageURL, String ram, String gpu, String cpu, String storageType) {
        super(name, ID, price, category, model, brand, warranty, quantity, imageURL);

        this.ram = ram;
        this.gpu = gpu;
        this.cpu = cpu;
        this.storageType = storageType;
    }

    public Computer(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, Image image, String ram, String gpu, String cpu, String storageType) {
        super(name, ID, price, category, model, brand, warranty, quantity, image);

        this.ram = ram;
        this.gpu = gpu;
        this.cpu = cpu;
        this.storageType = storageType;
    }

    /*Getters & Setters*/

    /**
     * Get a String array with all the unique properties of a computer
     *
     * @return String array with all the unique properties
     */
    public static String[] getPropertiesArr() {
        return new String[]{"RAM", "GPU", "CPU", "Storage Type"};
    }

    /**
     * Return the value of a property based on its name
     *
     * @param name Name of the property. Valid names are the ones on the array from getPropertiesArr()
     * @return Null if the property name is invalid, otherwise the value of the property.
     */
    public String getPropertyByName(String name) {

        switch (name.toLowerCase()) {
            case "ram":
                return getRam();
            case "gpu":
                return getGpu();
            case "cpu":
                return getCpu();
            case "storage type":
                return getStorageType();
            default:
                return null;
        }

    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
}
