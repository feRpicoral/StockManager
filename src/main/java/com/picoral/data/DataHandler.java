package com.picoral.data;

import com.picoral.models.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.picoral.Constants.DATA_FILE_NAME;
import static com.picoral.Constants.SAMPLE_DATA_FILE_PATH;

/**
 * Writes and reads to the save file
 */
public class DataHandler {

    private JSONArray json = new JSONArray(); //JSONArray obj
    private TableView<Product> table;
    private File f; //Current JSON file obj - it may be the temp file if the user is using sample data
    private final List<Product> productsFromJSON = new ArrayList<>();
    private boolean isUsingSampleData = false;

    public DataHandler() {

        //Start point to the DataHandler
        //If the file exists, parse it. Otherwise, create a new one
        //and parse the newly created file
        createFile();

    }

    /**
     * Returns to the user generated data
     */
    public void unloadSampleData() {

        //Make sure no product from different data file will be added
        productsFromJSON.clear();

        //Clear the table
        json = new JSONArray();
        isUsingSampleData = false;
        table.getItems().clear();

        //Load the user data file
        createFile();

        //Update the table
        setTable(table);

        System.out.println("[WARNING] You are no longer using sample data. All the changes here will be saved.");

    }

    /**
     * Saves the current data and loads the sample data
     */
    public void loadSampleData() {

        //Make sure no product from different data file will be added
        productsFromJSON.clear();

        //Save the changes to the user data file and clear the table
        save();
        table.getItems().clear();

        isUsingSampleData = true;

        try {

            //Load the sample data file from the resources

            //Path to the sample data file
            Path path;

            try {

                path = Path.of(getClass().getResource("/com/picoral/" + SAMPLE_DATA_FILE_PATH).toURI());

                //Will throw and exception if running on jlink runtime, forcing the catch block
                new File(path.toUri());

            } catch (Exception ignored) {

                //When running after compiled
                FileSystem fs = FileSystems.getFileSystem(URI.create("jrt:/"));

                //Get path object from modules
                path = fs.getPath(

                        "/modules/" + getClass().getModule().getName(),
                        "com/picoral",
                        SAMPLE_DATA_FILE_PATH

                );

            }

            //Set the sample data file to a temp file
            //This temp file will contain the sample data, loaded from resources folder
            f = File.createTempFile("temp", ".json");

            //Populate the temp file
            FileWriter fw = new FileWriter(f);
            fw.write(Files.readString(path));
            fw.close();

            //Update the json obj
            json = new JSONArray(Files.readString(Path.of(f.getPath())));

            //Parse the data
            parse();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Update the table
        setTable(table);

        System.out.println("[WARNING] You are now using sample data. Any changes you make here won't be saved!");

    }

    /**
     * Create the JSON file if it doesn't exist.
     * If it does exist, the file will be loaded and set to the class attribute "f"
     */
    private void createFile() {

        //Location in the system of the directory for the data file
        String dirPath = System.getProperty("user.home") +
                File.separator +
                ".StockManager" +
                File.separator +
                "data";

        //Path to the actual file - dirPath followed by the file's name
        String filePath = dirPath + File.separator + DATA_FILE_NAME;

        try {

            File dir = new File(dirPath);

            //Create the directories if needed
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(filePath);

            //If the file doesn't exist, create a blank one
            if (!file.exists()) {
                file.createNewFile();

                FileWriter fw = new FileWriter(file);
                fw.write("[]");
                fw.close();

            }

            //Set the class file to the newly created - or loaded - file
            // and set the jsonStr to the contents of this file
            f = file;

            //Update the json obj
            json = new JSONArray(Files.readString(Path.of(file.getPath())));

            //Parse the contents of the file
            parse();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Parses the content of the JSON file
     */
    private void parse() {

        //Loop for each object (product) from json array
        for (Object obj : json) {

            JSONObject j = (JSONObject) obj;

            //Product properties
            String name = j.getString("name");
            String ID = j.getString("id");
            double price = j.getDouble("price");
            String category = j.getString("category");
            String model = j.getString("model");
            String brand = j.getString("brand");
            String warranty = j.getString("warranty");
            int quantity = j.getInt("quantity");
            String imageURL = j.getString("imageURL");

            Product p;

            JSONObject unique = j.getJSONObject("unique");

            String size, color, resolution, os;

            switch (category) {
                case "Computer":

                    String ram = unique.getString("ram");
                    String gpu = unique.getString("gpu");
                    String cpu = unique.getString("cpu");
                    String storageType = unique.getString("storageType");

                    p = new Computer(name, ID, price, category, model, brand, warranty, quantity, imageURL, ram, gpu, cpu, storageType);

                    break;

                case "TV":

                    size = unique.getString("size");
                    resolution = unique.getString("resolution");

                    p = new TV(name, ID, price, category, model, brand, warranty, quantity, imageURL, size, resolution);

                    break;

                case "Watch":

                    color = unique.getString("color");
                    size = unique.getString("size");

                    p = new Watch(name, ID, price, category, model, brand, warranty, quantity, imageURL, color, size);

                    break;

                case "Phone":

                    os = unique.getString("os");
                    color = unique.getString("color");

                    p = new Phone(name, ID, price, category, model, brand, warranty, quantity, imageURL, os, color);

                    break;

                default:
                    throw new RuntimeException("InvalidCategoryException");

            }

            //We can't add these to the table yet because it hasn't been created yet
            //Since initialize() hasn't been called yet on MainController
            //These products will be on hold and will be added once setTable is called

            productsFromJSON.add(p);

        }


    }

    /**
     * Saves the JSONObject to the file
     */
    private void save() {

        try {

            FileWriter fw = new FileWriter(f, false);
            fw.write(json.toString());
            fw.close();

        } catch (Exception e) {
            System.out.println("[ERROR] It wasn't possible to save the data!");
            throw new RuntimeException(e);
        }

    }

    /**
     * Resets all the saved data. No confirmation prompt.
     */
    public void reset() {

        table.getItems().clear();
        json = new JSONArray();

        //Don't make changes to the files if using sample data
        if (isUsingSampleData) {
            return;
        }

        //If using the user data, recreate the file and parse it again
        f.delete();
        createFile();
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
                table.getItems().add(p);
            }

        }

    }

    /**
     * Add product to the table and if parsing mode is set to false, save this product to the JSON file
     *
     * @param product Product instance to be added
     */
    public void addProduct(Product product) {
        addToJson(product);
        table.getItems().add(product);

        save(); //Save the changes on the fly

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        System.out.println(Arrays.toString(stackTraceElements));

    }

    /**
     * Add product to JSONObject
     *
     * @param product Product instance to be added
     */
    private void addToJson(Product product) {

        //Avoids not adding the imageURL property if it is null
        String imgURL = product.getImageURL();

        if (imgURL == null) {
            imgURL = "";
        }

        String finalImgURL = imgURL;

        //Create JSON obj
        JSONObject values = new JSONObject() {{
            put("name", product.getName());
            put("id", product.getID());
            put("price", product.getPrice());
            put("category", product.getCategory());
            put("model", product.getModel());
            put("brand", product.getBrand());
            put("warranty", product.getWarranty());
            put("quantity", product.getQuantity());
            put("imageURL", finalImgURL);
            put("unique", new JSONObject());
        }};

        JSONObject unique = values.getJSONObject("unique");

        switch (product.getCategory()) {
            case "Computer":

                Computer c = (Computer) product;

                unique.put("ram", c.getRam());
                unique.put("gpu", c.getGpu());
                unique.put("cpu", c.getCpu());
                unique.put("storageType", c.getStorageType());

                break;

            case "TV":

                TV tv = (TV) product;

                unique.put("size", tv.getSize());
                unique.put("resolution", tv.getResolution());

                break;

            case "Watch":

                Watch w = (Watch) product;

                unique.put("color", w.getColor());
                unique.put("size", w.getSize());

                break;

            case "Phone":

                Phone p = (Phone) product;

                unique.put("color", p.getColor());
                unique.put("os", p.getOs());

                break;

            default:
                throw new RuntimeException("InvalidCategoryException");
        }

        //Append to the current obj
        json.put(values);

    }

    /**
     * Update product info by removing the old object and adding a new one as the updated version
     *
     * @param product Product instance to be updated
     */
    public void updateProduct(Product product) {

        Product old = null;

        //Get old version of the product by ID since it's immutable
        for (Product p : table.getItems()) {

            if (p.getID().equals(product.getID())) {

                old = p;

            }

        }

        if (old != null) {
            //Remove the old obj and a new one (the updated version)
            removeProduct(old);
            addProduct(product);

            //Workaround to force table update
            for (TableColumn<Product, ?> c : table.getColumns()) {
                c.setVisible(false);
                c.setVisible(true);
            }

        }

    }

    /**
     * Remove product from the table.
     * No confirmation prompt.
     *
     * @param product Product instance to be removed
     */
    public void removeProduct(Product product) {

        if (product == null) {
            return;
        }

        removeFromJson(product);
        table.getItems().remove(product);
        save(); //Save changes of the fly
    }

    /**
     * Remove product from the table by ID
     * No confirmation prompt
     *
     * @param id String ID of the product to be removed
     */
    public void removeProduct(String id) {

        //Product to remove to avoid concurrent modification exception
        Product toRemove = null;

        for (Product p : table.getItems()) {

            if (p.getID().equals(id)) {

                toRemove = p;

            }

        }

        removeProduct(toRemove);

    }


    /**
     * Removes the given product from the JSONObject, if present.
     *
     * @param product Product to be removed from the JSONObject
     */
    private void removeFromJson(Product product) {

        for (int i = 0; i < json.length(); i++) {

            if (((JSONObject) json.get(i)).get("id").equals(product.getID())) {

                json.remove(i);

            }

        }

    }

}
