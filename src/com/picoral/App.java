package com.picoral;

import com.picoral.controller.DataHandler;
import com.picoral.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    private Stage window;
    DataHandler dataHandler = new DataHandler();

    //Handling closing the program
    @Override
    public void stop() {

        dataHandler.save();

        //Close window
        window.close();
    }

    @Override
    public void start(Stage window) throws Exception{

        this.window = window;

        //Window properties
        window.setTitle("Stock Manager");
        window.setMinWidth(800);
        window.setMinHeight(500);

        //Main scene
        window.setScene(new Scene(
                new MainController(this, dataHandler)
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

    public Stage getWindow() {
        return window;
    }
}
