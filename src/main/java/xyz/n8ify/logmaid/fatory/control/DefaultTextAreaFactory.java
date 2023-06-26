package xyz.n8ify.logmaid.fatory.control;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import xyz.n8ify.logmaid.constant.UIConstant;
import xyz.n8ify.logmaid.enums.Widget;

public class DefaultTextAreaFactory {

    public static TextArea newInstance(Widget widget, String promptText) {
        TextArea ta = new TextArea();
        ta.setId(widget.getId());
        ta.setFocusTraversable(false);
        ta.setPromptText(promptText);
        ta.setPadding(new Insets(UIConstant.SM_INSET, 0, 0, 0));
        return ta;
    }

}
