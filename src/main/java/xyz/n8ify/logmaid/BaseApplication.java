package xyz.n8ify.logmaid;

import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import xyz.n8ify.logmaid.callback.ApplicationCallback;
import xyz.n8ify.logmaid.callback.LogCallback;
import xyz.n8ify.logmaid.constant.LabelConstant;
import xyz.n8ify.logmaid.constant.StringConstant;
import xyz.n8ify.logmaid.enums.LogLevel;
import xyz.n8ify.logmaid.enums.Widget;
import xyz.n8ify.logmaid.model.ExtractInfo;
import xyz.n8ify.logmaid.model.Preset;
import xyz.n8ify.logmaid.utils.DatabaseUtil;
import xyz.n8ify.logmaid.utils.LogContentUtil;
import xyz.n8ify.logmaid.utils.LogExtractorUtil;

public abstract class BaseApplication extends Application implements ApplicationCallback, LogCallback {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public <T extends Control> T findByWidget(Widget widget) {
        return (T) this.stage.getScene().lookup(widget.getQualifiedId());
    }

    @Override
    public void onPresetSelect(String presetName) {
        try {
            final Preset preset = DatabaseUtil.loadPresetByName(presetName);
            if (preset != null) {
                ExtractInfo.setInstance(preset);
                this.refreshWidget();
                this.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Preset [%s] was selected...", presetName)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.onLog(LogContentUtil.generate(LogLevel.ERROR, e.getMessage()));
        }
    }

    @Override
    public void onExtractClick(MouseEvent event) {
        try {
            this.refreshExtractInfo();
            boolean isInputOutputDirectoryProvide = ExtractInfo.getInstance().isInputOutputDirectoryProvide();
            boolean isRequiredDataProvided = ExtractInfo.getInstance().isRequiredDataProvided();
            if (isInputOutputDirectoryProvide && isRequiredDataProvided) {
                LogExtractorUtil.proceed(ExtractInfo.getInstance(), this);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(LabelConstant.WARNING);
                alert.getDialogPane().setContent(new Label(String.format(LabelConstant.WARNING_REQUIRED_DATA_MUST_BE_PROVIDED, isInputOutputDirectoryProvide, isRequiredDataProvided)));
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLog(String log) {
        TextArea taLogTextArea =  ((TextArea) stage.getScene().lookup(Widget.LogTextArea.getQualifiedId()));
        taLogTextArea.appendText(StringConstant.NEW_LINE + log);
    }

    /* --- Helper function --- */
    public void refreshExtractInfo() {
        String inputDirectoryPath = this.<TextField>findByWidget((Widget.InputLogDirectoryTextField)).getText();
        String outputDirectoryPath = this.<TextField>findByWidget((Widget.OutputLogDirectoryTextField)).getText();
        String interestedKeyWord = this.<TextArea>findByWidget((Widget.InterestedKeyWordTextArea)).getText();
        String adhocKeyWord = this.<TextArea>findByWidget((Widget.AdhocKeyWordTextArea)).getText();
        String ignoredKeyWord = this.<TextArea>findByWidget((Widget.IgnoredKeyWordTextArea)).getText();
        String groupedThreadKeyWord = this.<TextArea>findByWidget((Widget.GroupedThreadKeyWordTextArea)).getText();
        ExtractInfo.setInstance(new ExtractInfo(inputDirectoryPath, outputDirectoryPath
                , interestedKeyWord
                , adhocKeyWord
                , ignoredKeyWord
                , groupedThreadKeyWord));
    }
    public void refreshWidget() {
        ExtractInfo extractInfo = ExtractInfo.getInstance();
        this.<TextField>findByWidget((Widget.InputLogDirectoryTextField)).setText(extractInfo.getInputLogDirPath());
        this.<TextField>findByWidget((Widget.OutputLogDirectoryTextField)).setText(extractInfo.getOutputLogDirPath());
        this.<TextArea>findByWidget((Widget.InterestedKeyWordTextArea)).setText(extractInfo.getRawInterestedKeywordString());
        this.<TextArea>findByWidget((Widget.AdhocKeyWordTextArea)).setText(extractInfo.getRawAdhocKeywordString());
        this.<TextArea>findByWidget((Widget.IgnoredKeyWordTextArea)).setText(extractInfo.getRawIgnoredKeywordString());
        this.<TextArea>findByWidget((Widget.GroupedThreadKeyWordTextArea)).setText(extractInfo.getRawGroupedThreadKeywordString());
    }
}
