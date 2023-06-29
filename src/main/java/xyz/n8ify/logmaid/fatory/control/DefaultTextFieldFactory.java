package xyz.n8ify.logmaid.fatory.control;

import javafx.scene.control.TextField;
import xyz.n8ify.logmaid.enums.Widget;

public class DefaultTextFieldFactory {

    public static TextField newInstance(String promptText, String id) {
        TextField tf = new TextField();
        tf.setId(id);
        tf.setFocusTraversable(false);
        tf.setPromptText(promptText);
        return tf;
    }

}
