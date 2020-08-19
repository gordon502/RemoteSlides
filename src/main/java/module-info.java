module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.sql;
    requires commons.net;
    requires org.apache.commons.io;
    exports org.example;
    exports org.example.files;
    exports org.example.scene;
    exports org.example.model;
    exports org.example.database;
    exports org.example.database.adapter;
}
