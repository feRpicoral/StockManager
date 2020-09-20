package com.picoral.controller;

import com.picoral.App;
import com.picoral.models.Product;
import javafx.scene.control.TableView;

import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Writes and reads to the save files
 */
public class DataHandler {

    private JSONObject json = new JSONObject();
    private TableView<Product> table;
    private String jsonStr;
    private final File f;
    private final ArrayList<Product> productsFromJSON = new ArrayList<>();

    public DataHandler() {

        f = new File(Util.DATA_FILE_PATH);

        //Current JSON data as string
        try {
            jsonStr = Files.lines(Paths.get(Util.DATA_FILE_PATH)).collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception ignored) {
            System.out.println("The '" + Util.DATA_FILE_PATH + "' file was not found. It will be generated.");
        }

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

        //List which will hold all the json objects (products)
        List<JSONObject> jList = new ArrayList<>();

        try {

            //If there's only one product it will be recognized as a JSONObject
            //Otherwise it will be a JSONArray

            //Current JSON as JSONObject
            jList.add(new JSONObject(jsonStr).getJSONObject("products"));

        } catch (Exception ignored) {

            //Current JSON as JSONArray
            JSONArray arr = new JSONObject(jsonStr).getJSONArray("products");

            for (int i = 0; i < arr.length(); i++) {
                jList.add(arr.getJSONObject(i));
            }

        }

        //Loop for each object (product) from list
        for (JSONObject j : jList) {

            //Product properties
            String name     = j.getString("name");
            String ID       = j.getString("id");
            double price    = j.getDouble("price");
            String category = j.getString("category");
            String model    = j.getString("model");
            String brand    = j.getString("brand");
            String warranty = j.getString("warranty");
            int    quantity = j.getInt("quantity");
            String imageURL = j.getString("imageURL");

            Product p = new Product(name, ID, price, category, model, brand, warranty, quantity, imageURL);

            //We can't add these to the table yet because it hasn't been created yet
            //Since initialize() hasn't been called yet on MainController
            //These products will be on hold and will be added once setTable is called
            productsFromJSON.add(p);

        }

    }


    /**
     * File was just created and needs to be populated
     */
    private void create() {
        try {

            FileWriter fw = new FileWriter(Util.DATA_FILE_PATH);
            fw.write("{\"products\":[]}"); //Empty JSON skeleton
            fw.close();

            //Update current JSON as string an as object
            jsonStr = Files.lines(Paths.get(Util.DATA_FILE_PATH)).collect(Collectors.joining(System.lineSeparator()));
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

            String jsonStr = json.toString();
            FileWriter fw = new FileWriter(f);

            if (jsonStr.equals("{}")) {
                jsonStr = "{\"products\":[]}";
            }

            fw.write(jsonStr);
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
     *
     * @param table the TableView to set as the class table
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
        removeFromJson(product);
        table.getItems().remove(product);
    }

    /**
     * Remove product from the table by ID
     * No confirmation prompt
     *
     * @param id String ID of the product to be removed
     */
    public void removeProduct(String id) {

        //List of products to remove to avoid concurrent modification exception
        List<Product> toRemove = new ArrayList<>();

        for (Product p : table.getItems()) {

            if (p.getID().equals(id)) {

                toRemove.add(p);

            }

        }

        toRemove.forEach(this::removeProduct);

    }

    /**
     * Removes the given product from the JSONObject, if present.
     *
     * @param product Product to be removed from the JSONObject
     */
    private void removeFromJson(Product product) {

        if (json.get("products") instanceof JSONArray) {
            //More than one product in the list - JSONArray

            JSONArray arr = (JSONArray) json.get("products");

            for (int i = 0; i < arr.length(); i++) {

                if (((JSONObject) arr.get(i)).get("id").equals(product.getID())) {
                    arr.remove(i);
                }

            }

        } else {
            //Only one product in the list - JSONObject

            if (json.getJSONObject("products").get("id").equals(product.getID())) {
                jsonStr = "{\"products\":[]}";
                json = new JSONObject(jsonStr);
            }

        }

    }
}
