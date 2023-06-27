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

        TextField tfLogInputDir = DefaultTextFieldFactory.newInstance(LabelConstant.INPUT_PATH_HINT);
        Button btnBrowse = new Button();
        DirectoryChooser chooser = new DirectoryChooser();

        Optional.ofNullable(AppConfig.DEFAULT_INPUT_LOG_DIR_PATH).ifPresent(value -> {
            application.onInputLogDirectorySelect(new File(value));
            tfLogInputDir.setText(Optional.ofNullable(AppConfig.DEFAULT_INPUT_LOG_DIR_PATH).orElse(StringConstant.EMPTY));
        });
        btnBrowse.setText(StringConstant.BROWSE);
        btnBrowse.setOnMouseClicked(mouseEvent -> {
            File destination = chooser.showDialog(application.getStage());
            if (destination != null) {
                application.onInputLogDirectorySelect(destination);
                tfLogInputDir.setText(application.getDataStorage().getInputLogDirPath());
            }
        });
        tfLogInputDir.textProperty().addListener((observableValue, oldText, newText) -> {
            application.onInputLogDirectorySelect(new File(newText));
        });

        HBox container = new HBox(UIConstant.SM_INSET);
        container.getChildren().addAll(
                tfLogInputDir,
                btnBrowse
        );
        return container;
    }

    private static HBox initOutputDir(BaseApplication application) {

        TextField tfLogOutputDir = DefaultTextFieldFactory.newInstance(LabelConstant.OUTPUT_PATH_HINT);
        Button btnBrowse = new Button();
        DirectoryChooser chooser = new DirectoryChooser();

        Optional.ofNullable(AppConfig.DEFAULT_OUTPUT_LOG_DIR_PATH).ifPresent(value -> {
            application.onOutputLogDirectorySelect(new File(value));
            tfLogOutputDir.setText(Optional.ofNullable(AppConfig.DEFAULT_OUTPUT_LOG_DIR_PATH).orElse(StringConstant.EMPTY));
        });
        btnBrowse.setText(StringConstant.BROWSE);
        btnBrowse.setOnMouseClicked(mouseEvent -> {
            File destination = chooser.showDialog(application.getStage());
            if (destination != null) {
                application.onOutputLogDirectorySelect(destination);
                tfLogOutputDir.setText(application.getDataStorage().getOutputLogDirPath());
            }
        });
        tfLogOutputDir.textProperty().addListener((observableValue, oldText, newText) -> {
            application.onOutputLogDirectorySelect(new File(newText));
        });

        HBox container = new HBox(UIConstant.SM_INSET);
        container.getChildren().addAll(
                tfLogOutputDir,
                btnBrowse
        );
        return container;
    }

}
