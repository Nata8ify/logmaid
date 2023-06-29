package xyz.n8ify.logmaid.component;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import xyz.n8ify.logmaid.BaseApplication;
import xyz.n8ify.logmaid.config.AppConfig;
import xyz.n8ify.logmaid.constant.LabelConstant;
import xyz.n8ify.logmaid.constant.StringConstant;
import xyz.n8ify.logmaid.constant.UIConstant;
import xyz.n8ify.logmaid.enums.Widget;
import xyz.n8ify.logmaid.fatory.control.DefaultTextFieldFactory;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class HeaderComponent extends AbstractComponent {

    public static VBox init(BaseApplication application) {

        VBox container = new VBox(UIConstant.M_INSET);
        container.getChildren().addAll(
                initPresetOption(application),
                initInputDir(application),
                initOutputDir(application)
        );
        container.requestFocus();
        return container;
    }


    private static Pane initPresetOption(BaseApplication application) {
        ComboBox<String> cb = new ComboBox<>(FXCollections.observableArrayList("Default", "1"));
        HBox hb = new HBox(cb);
        cb.getSelectionModel().select(0);
        cb.setMinWidth(UIConstant.HEADER_INPUT_BOX_WIDTH);
        cb.setOnAction(actionEvent -> {
            application.onPresetSelect(cb.getValue());
        });

        Button btnSavePreset = new Button();
        btnSavePreset.setText(StringConstant.SAVE);

        HBox container = new HBox(UIConstant.SM_INSET);
        container.getChildren().addAll(
                hb,
                btnSavePreset
        );
        return container;
    }

    private static Pane initInputDir(BaseApplication application) {

        TextField tfLogInputDir = DefaultTextFieldFactory.newInstance(LabelConstant.INPUT_PATH_HINT, Widget.InputLogDirectoryTextField.getId());
        tfLogInputDir.setMinWidth(UIConstant.HEADER_INPUT_BOX_WIDTH);
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
        tfLogOutputDir.setMinWidth(UIConstant.HEADER_INPUT_BOX_WIDTH);
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
