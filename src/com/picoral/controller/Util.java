package com.picoral.controller;

import javax.imageio.ImageIO;
import java.net.URL;

public abstract class Util {

    //All possible product categories
    //TODO Move this to an external file
    public static final String[] possibleCategories = new String[]{
            "Smart Phone",
            "Watch",
            "TV",
            "Computer"
    };

    public static final String DATA_FILE_PATH = "data.json";

    //URL validation methods
    private static boolean isURLValid(String url) {
        try {
            (new URL(url)).openStream().close();
            return true;
        } catch (Exception ignored) { }

        return false;
    }

    public static boolean isURLImage(String url) {
        try {
            return isURLValid(url) && ImageIO.read(new URL(url)) != null;
        } catch (Exception ignored) {
            return false;
        }
    }


}
