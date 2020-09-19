package com.picoral.models;

import com.picoral.controller.Util;
import java.util.Arrays;

public class Product {

    private String name;
    private String ID;
    private double price;
    private String category;
    private String model;
    private String brand;
    private String warranty;
    private int quantity;
    private String imageURL;

    /**
     * Default constructor
     */
    public Product(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity, String imageURL) {

        //Name
        if (!name.isBlank()) {
            this.name = name;
        }

        //ID
        if (!ID.isBlank()) {
            this.ID = ID;
        }

        //Price
        if (price >= 0D) {
            this.price = price;
        }

        //Category
        if (Arrays.asList(Util.possibleCategories).contains(category)) {
            this.category = category;
        }

        //Model
        if (!model.isBlank()) {
            this.model = model;
        }

        //Brand
        if (!brand.isBlank()) {
            this.brand = brand;
        }

        //Warranty
        if (!warranty.isBlank()) {
            this.warranty = warranty;
        }

        //Quantity
        if (quantity >= 0) {
            this.quantity = quantity;
        }

        //Image url
        if (Util.isURLImage(imageURL)) {
            this.imageURL = imageURL;
        } else {
            this.imageURL = null;
        }

    }

    /**
     * Constructor without the image URL
     */
    public Product(String name, String ID, double price, String category, String model, String brand, String warranty, int quantity) {
        this(name, ID, price, category, model, brand, warranty, quantity, null);
    }


    /**
     * Increase current amount by 1
     */
    public void increaseQuantity() {
        increaseQuantity(1);
    }

    /**
     * Increase product quantity by given amount
     *
     * @param amount int amount greater than 0 to increase to the current quantity
     */
    public void increaseQuantity(int amount) {

        if (amount > 0) {
            quantity += amount;
        }

    }

    /**
     * Decrease current quantity by 1
     */
    public void decreaseQuantity() {
        decreaseQuantity(1);
    }

    /**
     * Decrease product quantity by given amount
     *
     * @param amount int amount greater than 0 to decrease from the current quantity
     */
    public void decreaseQuantity(int amount) {

        if (amount < 1) {
            return;
        }

        if (quantity - amount >= 0) {
            quantity -= amount;
        }

    }

    //To String - debug only
    @Override
    public String toString() {
        return "Product {" +
                "name='" + name + '\'' +
                ", ID='" + ID + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", warranty='" + warranty + '\'' +
                ", quantity=" + quantity +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }


    //Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!name.isBlank()) {
            this.name = name;
        }
    }

    public String getID() {
        return ID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price > 0D) {
            this.price = price;
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (Arrays.asList(Util.possibleCategories).contains(category.toLowerCase())) {
            this.category = category;
        }
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        if (!model.isBlank()) {
            this.model = model;
        }
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        if (!brand.isBlank()) {
            this.brand = brand;
        }
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        if (!warranty.isBlank()) {
            this.warranty = warranty;
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        if (Util.isURLImage(imageURL)) {
            this.imageURL = imageURL;
        }
    }
}
