package xyz.n8ify.logmaid.component;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import xyz.n8ify.logmaid.BaseApplication;
import xyz.n8ify.logmaid.constant.LabelConstant;
import xyz.n8ify.logmaid.constant.UIConstant;

public class BottomComponent extends AbstractComponent {

    public static BorderPane init(BaseApplication application) {
        BorderPane container = new BorderPane();
        container.setRight(initExtractButton(application));
        return container;
    }

    private static Button initExtractButton(BaseApplication application) {

        Button btnExtract = new Button(LabelConstant.EXTRACT_IT_TEXT);
        btnExtract.setOnMouseClicked((e) -> {
            application.onExtractClick(btnExtract, e);
        });

        return btnExtract;
    }

}
