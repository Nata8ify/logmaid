package xyz.n8ify.logmaid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;
import xyz.n8ify.logmaid.constant.CommonConstant;
import xyz.n8ify.logmaid.constant.UIConstant;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle(CommonConstant.APP_NAME);
//        stage.setScene(scene);
//        stage.show();

        initialStage(stage, instatiatePanel());

    }

    private static Panel instatiatePanel() {
        Panel panel = new Panel(CommonConstant.APP_NAME);
        panel.getStyleClass().add("panel-primary");

        BorderPane content = new BorderPane();
        content.setPadding(new Insets(20));
        TextField tfLogInputDir = new TextField();
        tfLogInputDir.setPromptText("path/to/...");
        Button button = new Button("Extract it, Maid!");
        button.setOnMouseClicked(mouseEvent -> {
            System.out.println("Clieck!");
        });
        content.setCenter(tfLogInputDir);
//        content.setCenter(button);

        panel.setBody(content);
        return panel;
    }

    private static void initialStage(Stage stage, Panel panel) {
        Scene scene = new Scene(panel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle(CommonConstant.APP_NAME);
        stage.setScene(scene);
        stage.setMinWidth(UIConstant.WINDOW_MIN_WIDTH);
        stage.setMinHeight(UIConstant.WINDOW_MIN_HEIGHT);
        stage.setMaxWidth(UIConstant.WINDOW_MAX_WIDTH);
        stage.setMaxHeight(UIConstant.WINDOW_MAX_HEIGHT);
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}