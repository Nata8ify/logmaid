package xyz.n8ify.logmaid;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import xyz.n8ify.logmaid.callback.ApplicationCallback;
import xyz.n8ify.logmaid.callback.LogCallback;
import xyz.n8ify.logmaid.constant.LabelConstant;
import xyz.n8ify.logmaid.constant.StringConstant;
import xyz.n8ify.logmaid.enums.LogLevel;
import xyz.n8ify.logmaid.enums.Widget;
import xyz.n8ify.logmaid.model.ExtractProperty;
import xyz.n8ify.logmaid.utils.LogContentUtil;
import xyz.n8ify.logmaid.utils.LogExtractorUtil;

public class BaseApplication extends Application implements ApplicationCallback, LogCallback {

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void onPresetSelect(String preset) {
        System.out.println(preset);
    }

    @Override
    public void onExtractClick(MouseEvent event) {
        String inputDirectoryPath = ((TextField) stage.getScene().lookup(Widget.InputLogDirectoryTextField.getQualifiedId())).getText();
        String outputDirectoryPath = ((TextField) stage.getScene().lookup(Widget.OutputLogDirectoryTextField.getQualifiedId())).getText();

        TextArea taInterestedKeyWord = (TextArea) stage.getScene().lookup(Widget.InterestedKeyWordTextArea.getQualifiedId());
        TextArea taAdhocKeyWord = (TextArea) stage.getScene().lookup(Widget.AdhocKeyWordTextArea.getQualifiedId());
        TextArea taIgnoredKeyWord = (TextArea) stage.getScene().lookup(Widget.IgnoredKeyWordTextArea.getQualifiedId());
        TextArea taGroupedThreadKeyWord = (TextArea) stage.getScene().lookup(Widget.GroupedThreadKeyWordTextArea.getQualifiedId());
        ExtractProperty extractProperty = new ExtractProperty(inputDirectoryPath, outputDirectoryPath
                , taInterestedKeyWord.getText()
                , taAdhocKeyWord.getText()
                , taIgnoredKeyWord.getText()
                , taGroupedThreadKeyWord.getText());

        try {
            boolean isInputOutputDirectoryProvide = extractProperty.isInputOutputDirectoryProvide();
            boolean isRequiredDataProvided = extractProperty.isRequiredDataProvided();
            if (isInputOutputDirectoryProvide && isRequiredDataProvided) {
                LogExtractorUtil.proceed(extractProperty, this);
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

}
