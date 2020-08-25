package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.example.files.XMLSettingsLoader;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private final KeyCombination FullScreenKeyCombo =
            new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_ANY);
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("mediaPlayer"));

        // ALT + ENTER MAKES FULLSCREEN
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(FullScreenKeyCombo.match(event)) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });


        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {

        //read program arguments
        if (args.length > 0) {
            if (args[0].equals("config")) {
                XMLSettingsLoader.load("settings.xml");
            }
        }

        launch();
    }

}