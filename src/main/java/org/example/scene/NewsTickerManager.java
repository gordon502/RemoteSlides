package org.example.scene;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;

class NewsTickerManager extends Thread {

    // reference to Text object in scene
    private Text text;

    NewsTickerManager(Text text) {
        this.text = text;
    }


    void play(String informationText, Integer duration) {
        text.setText(informationText);
        double sceneWidth = text.getScene().getWidth();
        double msgWidth = text.getLayoutBounds().getWidth();

        KeyValue initKeyValue = new KeyValue(text.translateXProperty(), sceneWidth);
        KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);

        KeyValue endKeyValue = new KeyValue(text.translateXProperty(), -1.0
                * msgWidth);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(duration), endKeyValue);

        Timeline timeline = new Timeline(initFrame, endFrame);

        timeline.setCycleCount(1);
        timeline.play();

    }
}
