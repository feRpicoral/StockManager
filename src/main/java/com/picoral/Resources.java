package com.picoral;

import java.net.URL;

/**
 * Workaround to be able to access resources folder
 *
 * https://stackoverflow.com/questions/64111442/how-to-access-resources-folder-from-inner-package-in-gradle
 * > SO Question is now closed, link will lead to similar question
 */
public class Resources {

    /**
     * Workaround to be able to access files from resources from any package within com.picoral.
     *
     * @param name Relative path to the file
     * @return URL Object pointing to the file or null if the path (name param) is invalid
     */
    public static URL getFileAsURL(String name) {
        return Resources.class.getResource(name);
    }

}
