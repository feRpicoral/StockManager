package com.picoral.controller;

import com.picoral.App;
import com.picoral.models.Product;
import javafx.scene.control.TableView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Writes and reads to the save files
 */
public class DataHandler {

    private JSONObject json = new JSONObject();
    private TableView<Product> table;
    private final File f;
    private final ArrayList<Product> productsFromJSON = new ArrayList<>();

    public DataHandler() {

        f = new File("data.json");

        if (f.exists()) {
            load();
        } else {
            create();
        }

    }

    /**
     * Load JSON file if it exists
     */
    private void load() {

        try {

            //Current JSON as String
            String jsonStr = Files.lines(Paths.get("data.json")).collect(Collectors.joining(System.lineSeparator()));

            try {

                //If there's only one product it will be recognized as a JSONObject
                //Otherwise it will be a JSONArray

                //Current JSON as JSONObject
                JSONObject j = new JSONObject(jsonStr).getJSONObject("products");

                String name = j.getString("name");
                String ID = j.getString("id");
                double price = j.getDouble("price");
                String category = j.getString("category");
                String model = j.getString("model");
                String brand = j.getString("brand");
                String warranty = j.getString("warranty");
                int quantity = j.getInt("quantity");
                String imageURL = j.getString("imageURL");

                Product p = new Product(name, ID, price, category, model, brand, warranty, quantity, imageURL);

                productsFromJSON.add(p);

            } catch (Exception ignored) {

                //Current JSON as JSONArray
                JSONArray arr = new JSONObject(jsonStr).getJSONArray("products");

                for (Object obj : arr) {
                    if (obj instanceof JSONObject) {

                        JSONObject j = (JSONObject) obj;

                        String name = j.getString("name");
                        String ID = j.getString("id");
                        double price = j.getDouble("price");
                        String category = j.getString("category");
                        String model = j.getString("model");
                        String brand = j.getString("brand");
                        String warranty = j.getString("warranty");
                        int quantity = j.getInt("quantity");
                        String imageURL = j.getString("imageURL");

                        Product p = new Product(name, ID, price, category, model, brand, warranty, quantity, imageURL);

                        productsFromJSON.add(p);

                    }
                }
            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    /**
     * File was just created and need to be populated
     */
    private void create() {
        try {

            FileWriter fw = new FileWriter("data.json");
            fw.write("{\"products\":[]}");
            fw.close();

            String jsonStr = Files.lines(Paths.get("data.json")).collect(Collectors.joining(System.lineSeparator()));
            json = new JSONObject(jsonStr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Called when program is about to close.
     * Saves the JSONObject to the file
     */
    public void save() {

        try {

            FileWriter fw = new FileWriter(f);
            fw.write(json.toString());
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Resets all the saved data. No confirmation prompt.
     */
    public void reset() {
        f.delete();
        create();
        table.getItems().clear();
    }

    /**
     * Sets the table and add the products that were on the data.json file to it
     */
    public void setTable(TableView<Product> table) {
        if (table != null) {
            this.table = table;

            for (Product p : productsFromJSON) {
                addProduct(p);
            }

        }
    }

    /**
     * Add product to the table
     *
     * @param product Product instance to be added
     */
    public void addProduct(Product product) {
        table.getItems().add(product);

        //Avoids not adding the imageURL property if it is null
        String imgURL = product.getImageURL();

        if (imgURL == null) {
            imgURL = "";
        }

        String finalImgURL = imgURL;

        //Create JSON obj
        JSONObject values = new JSONObject(){{
            put("name", product.getName());
            put("id", product.getID());
            put("price", product.getPrice());
            put("category", product.getCategory());
            put("model", product.getModel());
            put("brand", product.getBrand());
            put("warranty", product.getWarranty());
            put("quantity", product.getQuantity());
            put("imageURL", finalImgURL);
        }};

        //Append to the current obj
        json.accumulate("products", values);

    }

    /**
     * Remove product from the table.
     * No confirmation prompt.
     *
     * @param product Product instance to be removed
     */
    public void removeProduct(Product product) {
        table.getItems().remove(product);
    }

    /**
     * Remove product from the table by ID
     * No confirmation prompt
     *
     * @param id String ID of the product to be removed
     */
    public void removeProduct(String id) {

        for (Product p : table.getItems()) {

            if (p.getID().equals(id)) {

                removeProduct(p);

            }

        }

    }
}
