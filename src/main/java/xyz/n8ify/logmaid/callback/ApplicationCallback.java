package xyz.n8ify.logmaid.callback;

import javafx.scene.input.MouseEvent;

import java.io.File;

public interface ApplicationCallback {

    void onInputLogDirectorySelect(File destination);
    void onOutputLogDirectorySelect(File destination);

    void onExtractClick(MouseEvent event);
}
