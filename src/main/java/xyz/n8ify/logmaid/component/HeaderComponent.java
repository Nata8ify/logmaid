package xyz.n8ify.logmaid.component;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import xyz.n8ify.logmaid.BaseApplication;
import xyz.n8ify.logmaid.config.AppConfig;
import xyz.n8ify.logmaid.constant.LabelConstant;
import xyz.n8ify.logmaid.constant.StringConstant;
import xyz.n8ify.logmaid.constant.UIConstant;
import xyz.n8ify.logmaid.enums.LogLevel;
import xyz.n8ify.logmaid.enums.Widget;
import xyz.n8ify.logmaid.fatory.control.DefaultTextFieldFactory;
import xyz.n8ify.logmaid.model.ExtractInfo;
import xyz.n8ify.logmaid.model.Preset;
import xyz.n8ify.logmaid.utils.DatabaseUtil;
import xyz.n8ify.logmaid.utils.LogContentUtil;
import xyz.n8ify.logmaid.utils.StringUtil;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class HeaderComponent extends AbstractComponent {

    public static VBox init(BaseApplication application) throws SQLException {

        VBox container = new VBox(UIConstant.M_INSET);
        container.getChildren().addAll(
                initPresetOption(application),
                initInputDir(application),
                initOutputDir(application)
        );
        container.requestFocus();
        return container;
    }


    private static Pane initPresetOption(BaseApplication application) throws SQLException {
        List<String> presets = new ArrayList<>(DatabaseUtil.loadPresetNames());
        presets.add(LabelConstant.ADD_PRESET);
        ComboBox<String> cb = new ComboBox<>(FXCollections.observableArrayList(presets));
        cb.setId(Widget.PresetComboBox.getId());
        HBox hb = new HBox(cb);
        cb.getSelectionModel().select(0);
        cb.setMinWidth(UIConstant.HEADER_INPUT_BOX_WIDTH);
        cb.setOnAction(actionEvent -> {
            if (LabelConstant.ADD_PRESET.equals(cb.getValue())) {
                addPreset(application, addPresetName -> {
                    presets.add(presets.size() - 1, addPresetName);
                    cb.setItems(FXCollections.observableArrayList(presets));
                    application.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Preset [%s] was created", addPresetName)));
                    return null;
                });
                cb.getSelectionModel().select(presets.size() > 2 ? presets.size() - 2 : 0); /* Selected latest item (not include add preset option) */
            } else {
                application.onPresetSelect(cb.getValue());
            }
        });

        Button btnSavePreset = new Button();
        btnSavePreset.setText(StringConstant.SAVE);
        btnSavePreset.setOnMouseClicked(event -> {
            application.refreshExtractInfo();
            savePreset(application, preset -> {
                application.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Preset [%s] was saved", preset.getName())));
                return null;
            });
        });

        Button btnDeletePreset = new Button();
        btnDeletePreset.setText(StringConstant.TRASH);
        btnDeletePreset.setOnMouseClicked(event -> {
            int removeIndex = cb.getSelectionModel().getSelectedIndex();
            if (removeIndex == 0) {
                application.onLog(LogContentUtil.generate(LogLevel.WARN, "Default preset cannot be deleted"));
                return;
            }
            deletePreset(application, deletePresetName -> {
                presets.remove(removeIndex);
                cb.setItems(FXCollections.observableArrayList(presets));
                application.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Preset [%s] was deleted", deletePresetName)));
                cb.getSelectionModel().select(removeIndex - 1);
                return null;
            });
        });

        HBox container = new HBox(UIConstant.SM_INSET);
        container.getChildren().addAll(
                hb,
                btnSavePreset,
                btnDeletePreset
        );
        return container;
    }

    private static Pane initInputDir(BaseApplication application) {

        TextField tfLogInputDir = DefaultTextFieldFactory.newInstance(LabelConstant.INPUT_PATH_HINT, Widget.InputLogDirectoryTextField.getId());
        tfLogInputDir.setMinWidth(UIConstant.HEADER_INPUT_BOX_WIDTH);
        DirectoryChooser chooser = new DirectoryChooser();

        if (StringUtil.isNotNullOrEmpty(ExtractInfo.getInstance().getInputLogDirPath())) {
            tfLogInputDir.setText(ExtractInfo.getInstance().getInputLogDirPath());
        } else {
            tfLogInputDir.setText(Optional.ofNullable(AppConfig.DEFAULT_INPUT_LOG_DIR_PATH).orElse(StringConstant.EMPTY));
        }

        Button btnBrowse = new Button();
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
        DirectoryChooser chooser = new DirectoryChooser();

        if (StringUtil.isNotNullOrEmpty(ExtractInfo.getInstance().getOutputLogDirPath())) {
            tfLogOutputDir.setText(ExtractInfo.getInstance().getOutputLogDirPath());
        } else {
            tfLogOutputDir.setText(Optional.ofNullable(AppConfig.DEFAULT_OUTPUT_LOG_DIR_PATH).orElse(StringConstant.EMPTY));
        }

        Button btnBrowse = new Button();
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

    /* ----- */
    private static void addPreset(BaseApplication application, Function<String, Void> onSuccess) {
        TextInputDialog tidNamePreset = new TextInputDialog();
        tidNamePreset.setHeaderText(LabelConstant.ADD_PRESET);
        String addPresetName = tidNamePreset.showAndWait().orElse(StringConstant.EMPTY);
        if (!StringUtil.isNotNullOrEmpty(addPresetName)) {
            return;
        }
        onSuccess.apply(addPresetName);
    }

    private static void savePreset(BaseApplication application, Function<Preset, Void> onSuccess) {
        try {
            String addPresetName = application.<ComboBox<String>>findByWidget(Widget.PresetComboBox).getValue();
            final Preset preset = new Preset();
            preset.setName(addPresetName);
            final Preset.Config config = new Preset.Config();
            final ExtractInfo extractInfo = ExtractInfo.getInstance();
            config.setInputLogDirectory(extractInfo.getInputLogDirPath());
            config.setOutputLogDirectory(extractInfo.getOutputLogDirPath());
            config.setInterestedKeyWordValues(extractInfo.getInterestedKeywordList());
            config.setAdhocKeyWordValues(extractInfo.getAdhocKeywordList());
            config.setIgnoredKeyWordValues(extractInfo.getIgnoredKeywordList());
            config.setGroupedThreadKeyWordValues(extractInfo.getGroupedThreadKeywordList());
            preset.setConfig(config);
            DatabaseUtil.upsertPreset(preset);
            onSuccess.apply(preset);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deletePreset(BaseApplication application, Function<String, Void> onSuccess) {
        try {
            String deletePresetName = application.<ComboBox<String>>findByWidget(Widget.PresetComboBox).getValue();
            DatabaseUtil.deletePresetByName(deletePresetName);
            onSuccess.apply(deletePresetName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
