package xyz.n8ify.logmaid;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;
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
        initialStage(stage, instatiatePanel());
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

    private Panel instatiatePanel() throws SQLException {
        Panel panel = new Panel(CommonConstant.APP_NAME);
        panel.getStyleClass().add("panel-default");

        BorderPane content = new BorderPane();
        content.setPadding(new Insets(UIConstant.SM_INSET));
        content.setTop(HeaderComponent.init(this));
        content.setCenter(CenterComponent.init(this));
        content.setBottom(BottomComponent.init(this));

        panel.setBody(content);
        return panel;
    }

    private void initialStage(Stage stage, Panel panel) {
        Scene scene = new Scene(panel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle(String.format("%s %s", CommonConstant.APP_NAME, CommonConstant.VERSION));
        stage.setScene(scene);
        stage.setMinWidth(UIConstant.WINDOW_MIN_WIDTH);
        stage.setMinHeight(UIConstant.WINDOW_MIN_HEIGHT);
        stage.setMaxWidth(UIConstant.WINDOW_MAX_WIDTH);
        stage.setMaxHeight(UIConstant.WINDOW_MAX_HEIGHT);
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