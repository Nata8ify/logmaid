package xyz.n8ify.logmaid.component;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import xyz.n8ify.logmaid.BaseApplication;
import xyz.n8ify.logmaid.constant.LabelConstant;
import xyz.n8ify.logmaid.constant.UIConstant;

public class BottomComponent extends AbstractComponent {

    public static VBox init(BaseApplication application) {
        VBox container = new VBox(UIConstant.M_INSET);
        container.getChildren().addAll(
                initExtractButton(application)
        );
        return container;
    }

    private static Button initExtractButton(BaseApplication application) {

        Button btnExtract = new Button(LabelConstant.EXTRACT_IT_TEXT);
        btnExtract.setOnMouseClicked(application::onExtractClick);

        return btnExtract;
    }

}
