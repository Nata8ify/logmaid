package xyz.n8ify.logmaid;

import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import xyz.n8ify.logmaid.callback.ApplicationCallback;
import xyz.n8ify.logmaid.enums.Widget;
import xyz.n8ify.logmaid.storage.DataStorage;

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

    public void setStage(Stage stage) {
        this.stage = stage;
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

        List<String> interestedKeyWordValues = List.of(taInterestedKeyWord.getText().split(NEW_LINE));
        List<String> adhocKeyWordValues = List.of(taAdhocKeyWord.getText().split(NEW_LINE));
        List<String> ignoredKeyWordValues = List.of(taIgnoredKeyWord.getText().split(NEW_LINE));
        List<String> groupedThreadKeyWordValues = List.of(taGroupedThreadKeyWord.getText().split(NEW_LINE));

    }



}
