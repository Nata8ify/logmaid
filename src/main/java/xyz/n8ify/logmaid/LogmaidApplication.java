package xyz.n8ify.logmaid;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import xyz.n8ify.logmaid.component.BottomComponent;
import xyz.n8ify.logmaid.component.CenterComponent;
import xyz.n8ify.logmaid.component.HeaderComponent;
import xyz.n8ify.logmaid.constant.CommonConstant;
import xyz.n8ify.logmaid.constant.UIConstant;
import xyz.n8ify.logmaid.enums.LogLevel;
import xyz.n8ify.logmaid.model.ExtractInfo;
import xyz.n8ify.logmaid.model.Preset;
import xyz.n8ify.logmaid.utils.DatabaseUtil;
import xyz.n8ify.logmaid.utils.LogContentUtil;

import java.sql.SQLException;
import java.util.List;

public class LogmaidApplication extends BaseApplication {

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        initialPreProcess();
        initialStage(stage, instantiatePanel());
        initialPostProcess();
    }

    private void initialPreProcess() throws SQLException {
        DatabaseUtil.initial();
        final List<Preset> presets = DatabaseUtil.loadPresets();
        if (!presets.isEmpty()) {
            ExtractInfo.setInstance(presets.get(0));
        }
    }

    private void initialPostProcess() throws SQLException {
        refreshWidget();
    }

    private Pane instantiatePanel() throws SQLException {
        BorderPane content = new BorderPane();
        content.setPadding(new Insets(UIConstant.SM_INSET));
        content.setTop(HeaderComponent.init(this));
        content.setCenter(CenterComponent.init(this));
        content.setBottom(BottomComponent.init(this));
        return content;
    }

    private void initialStage(Stage stage, Pane panel) {
        Scene scene = new Scene(panel);
        stage.setTitle(String.format("%s %s", CommonConstant.APP_NAME, CommonConstant.VERSION));
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        super.onLog(LogContentUtil.generate(LogLevel.INFO, String.format("Logmaid v%s is initialized", CommonConstant.VERSION)));
    }

    public static void main(String[] args) {
        launch();
    }
}