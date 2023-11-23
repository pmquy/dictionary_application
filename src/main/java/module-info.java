module com.example.dictionary {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires javafx.web;
    requires java.net.http;
    requires freetts;
    requires org.json;
    requires jdk.jsobject;
    requires org.jsoup;

    opens com.example.dictionary to javafx.fxml;
    exports com.example.dictionary;
    exports com.example.dictionary.controller;
    opens com.example.dictionary.controller to javafx.fxml;
    exports com.example.dictionary.scene;
    opens com.example.dictionary.scene to javafx.fxml;
    exports com.example.dictionary.user;
    opens com.example.dictionary.user to javafx.fxml;
    exports com.example.dictionary.word;
    opens com.example.dictionary.word to javafx.fxml;
}