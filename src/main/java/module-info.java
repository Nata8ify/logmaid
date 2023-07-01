module xyz.n8ify.logmaid {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.xerial.sqlitejdbc;
    requires com.google.gson;
    requires java.sql;

    opens xyz.n8ify.logmaid to javafx.fxml;
    opens xyz.n8ify.logmaid.model to com.google.gson;
    exports xyz.n8ify.logmaid;
    exports xyz.n8ify.logmaid.controller;
    opens xyz.n8ify.logmaid.controller to javafx.fxml;
}