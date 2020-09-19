package com.picoral;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    Stage window;

    @Override
    public void stop() {
        window.close();
    }

    @Override
    public void start(Stage window) throws Exception{

        this.window = window;

        //Title
        window.setTitle("Stock Manager v0.1");

        //Main scene
        window.setScene(new Scene(
                FXMLLoader.load(getClass().getResource("views/main.fxml"))
        ));

        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
