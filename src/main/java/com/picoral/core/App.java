package com.picoral.core;

import com.picoral.data.DataHandler;
import com.picoral.gui.windows.Main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private Stage window;
    public static final DataHandler dataHandler = new DataHandler();

    /**
     * Handles the request to close the program regardless from where it was called
     */
    @Override
    public void stop() {


        //Close window
        window.close();

    }

    /**
     * Start the program - called by JavaFX
     *
     * @param window Main Stage
     */
    @Override
    public void start(Stage window) throws IOException {

        this.window = window;

        //Window properties
        window.setTitle("Stock Manager");
        window.setMinWidth(800);
        window.setMinHeight(500);

        //Main scene
        window.setScene(new Scene(
                new Main(this)
        ));

        //Avoids skipping handling in stop() if closed through the X
        window.setOnCloseRequest(e -> {
            e.consume();
            stop();
        });

        window.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
