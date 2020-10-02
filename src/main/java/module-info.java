module com.picoral.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.json;
    requires jdk.crypto.ec;
    opens com.picoral.core;
    opens com.picoral.gui.popups;
    opens com.picoral.gui.windows;
    opens com.picoral.models;
    exports com.picoral;
}