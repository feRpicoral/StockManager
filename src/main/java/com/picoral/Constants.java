package com.picoral;

/**
 * Constants final class with private constructor
 */
public final class Constants {

    /**
     * Constructor is private to prevent instantiation
     */
    private Constants() {}

    /**
     * Name of the JSON that will be used to load and store data.
     * The file doesn't need to exist - if it doesn't it will be created.
     * It's located on {user.home}/.StockManager/data/{DATA_FILE_NAME}
     */
    public static final String DATA_FILE_NAME = "data.json";

    /**
     * Path to the sample data JSON relative to the /resources/com/picoral/ folder
     */
    public static final String SAMPLE_DATA_FILE_PATH = "data/sample_data.json";

    /**
     * All products categories to validate the JSON once it's loaded
     * Although adding a new category here would result in a new option appearing in the
     * category selection drop box, it wouldn't work since implementation in multiple
     * different classes is needed as well in order to it to work as expected
     */
    public static final String[] CATEGORIES = {
            "Phone",
            "Watch",
            "TV",
            "Computer"
    };

    /**
     * Possible image formats in lower case supported by JavaFX's ImageView
     */
    public static final String[] VALID_IMAGE_FORMATS = {
            "png",
            "jgp",
            "jpeg"
    };


}
