package xyz.n8ify.logmaid;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import xyz.n8ify.logmaid.callback.ApplicationCallback;
import xyz.n8ify.logmaid.constant.LabelConstant;
import xyz.n8ify.logmaid.enums.Widget;
import xyz.n8ify.logmaid.model.ExtractProperty;
import xyz.n8ify.logmaid.storage.DataStorage;
import xyz.n8ify.logmaid.utils.LogExtractorUtil;

import java.io.File;
import java.util.List;

import static xyz.n8ify.logmaid.constant.StringConstant.NEW_LINE;

public class BaseApplication extends Application implements ApplicationCallback {

    private Stage stage;
    private final DataStorage dataStorage = DataStorage.newInstance();

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;;
    }

    public Stage getStage() {
        return stage;
    }
    public DataStorage getDataStorage() {
        return dataStorage;
    }

    @Override
    public void onInputLogDirectorySelect(File destination) {
        this.dataStorage.setInputLogDirPath(destination.getAbsolutePath());
    }

    @Override
    public void onOutputLogDirectorySelect(File destination) {
        this.dataStorage.setOutputLogDirPath(destination.getAbsolutePath());
    }

    @Override
    public void onExtractClick(MouseEvent event) {
        TextArea taInterestedKeyWord = (TextArea) stage.getScene().lookup(Widget.InterestedKeyWordTextArea.getQualifiedId());
        TextArea taAdhocKeyWord = (TextArea) stage.getScene().lookup(Widget.AdhocKeyWordTextArea.getQualifiedId());
        TextArea taIgnoredKeyWord = (TextArea) stage.getScene().lookup(Widget.IgnoredKeyWordTextArea.getQualifiedId());
        TextArea taGroupedThreadKeyWord = (TextArea) stage.getScene().lookup(Widget.GroupedThreadKeyWordTextArea.getQualifiedId());
        ExtractProperty extractProperty = new ExtractProperty(taInterestedKeyWord.getText()
                , taAdhocKeyWord.getText()
                , taIgnoredKeyWord.getText()
                , taGroupedThreadKeyWord.getText());

        try {
            boolean isInputOutputDirectoryProvide = this.dataStorage.isInputOutputDirectoryProvide();
            boolean isRequiredDataProvided = extractProperty.isRequiredDataProvided();
            if (isInputOutputDirectoryProvide && isRequiredDataProvided) {
                LogExtractorUtil.proceed(extractProperty, this.dataStorage);
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



}
