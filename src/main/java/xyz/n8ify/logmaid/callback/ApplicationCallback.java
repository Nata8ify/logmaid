package xyz.n8ify.logmaid.callback;

import javafx.scene.input.MouseEvent;

import java.io.File;

public interface ApplicationCallback {
    void onPresetSelect(String preset);
    void onExtractClick(MouseEvent event);

}
