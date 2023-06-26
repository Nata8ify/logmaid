package xyz.n8ify.logmaid.fatory.control;

import javafx.scene.control.TextField;

public class DefaultTextFieldFactory {

    public static TextField newInstance(String promptText) {
        TextField tf = new TextField();
        tf.setFocusTraversable(false);
        tf.setPromptText(promptText);
        return tf;
    }

}
