package xyz.n8ify.logmaid.component;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import xyz.n8ify.logmaid.BaseApplication;
import xyz.n8ify.logmaid.config.AppConfig;
import xyz.n8ify.logmaid.constant.LabelConstant;
import xyz.n8ify.logmaid.constant.StringConstant;
import xyz.n8ify.logmaid.constant.UIConstant;
import xyz.n8ify.logmaid.enums.Widget;
import xyz.n8ify.logmaid.fatory.control.DefaultTextFieldFactory;

import java.io.File;
import java.util.Optional;

public class HeaderComponent extends AbstractComponent {

    public static VBox init(BaseApplication application) {

        VBox container = new VBox(UIConstant.M_INSET);
        container.getChildren().addAll(
                initInputDir(application),
                initOutputDir(application)
        );
        container.requestFocus();
        return container;
    }

    private static HBox initInputDir(BaseApplication application) {

        TextField tfLogInputDir = DefaultTextFieldFactory.newInstance(LabelConstant.INPUT_PATH_HINT, Widget.InputLogDirectoryTextField.getId());
        tfLogInputDir.setMinWidth(350);
        Button btnBrowse = new Button();
        DirectoryChooser chooser = new DirectoryChooser();

        Optional.ofNullable(AppConfig.DEFAULT_INPUT_LOG_DIR_PATH).ifPresent(value -> {
            tfLogInputDir.setText(Optional.of(AppConfig.DEFAULT_INPUT_LOG_DIR_PATH).orElse(StringConstant.EMPTY));
        });
        btnBrowse.setText(StringConstant.BROWSE);
        btnBrowse.setOnMouseClicked(mouseEvent -> {
            File destination = chooser.showDialog(application.getStage());
            if (destination != null) {
                tfLogInputDir.setText(destination.getAbsolutePath());
            }
        });

        HBox container = new HBox(UIConstant.SM_INSET);
        container.getChildren().addAll(
                tfLogInputDir,
                btnBrowse
        );
        return container;
    }

    private static HBox initOutputDir(BaseApplication application) {

        TextField tfLogOutputDir = DefaultTextFieldFactory.newInstance(LabelConstant.OUTPUT_PATH_HINT, Widget.OutputLogDirectoryTextField.getId());
        tfLogOutputDir.setMinWidth(350);
        Button btnBrowse = new Button();
        DirectoryChooser chooser = new DirectoryChooser();

        Optional.ofNullable(AppConfig.DEFAULT_OUTPUT_LOG_DIR_PATH).ifPresent(value -> {
            tfLogOutputDir.setText(Optional.of(AppConfig.DEFAULT_OUTPUT_LOG_DIR_PATH).orElse(StringConstant.EMPTY));
        });
        btnBrowse.setText(StringConstant.BROWSE);
        btnBrowse.setOnMouseClicked(mouseEvent -> {
            File destination = chooser.showDialog(application.getStage());
            if (destination != null) {
                tfLogOutputDir.setText(destination.getAbsolutePath());
            }
        });

        HBox container = new HBox(UIConstant.SM_INSET);
        container.getChildren().addAll(
                tfLogOutputDir,
                btnBrowse
        );
        return container;
    }

}
