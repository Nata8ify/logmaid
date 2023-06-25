module xyz.n8ify.logmaid {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens xyz.n8ify.logmaid to javafx.fxml;
    exports xyz.n8ify.logmaid;
}