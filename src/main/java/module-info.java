module com.picoral.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.json;
    opens com.picoral;
    opens com.picoral.controller;
    opens com.picoral.models;
    exports com.picoral;
}