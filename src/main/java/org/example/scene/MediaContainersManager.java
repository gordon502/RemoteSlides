package org.example.scene;


import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import org.example.Config;
import org.example.database.DBHandler;
import org.example.model.Slide;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Class responsible for easy switching between MediaView and ImageView
 * and using them simultaneously.
 */
class MediaContainersManager extends Thread{
    private MediaView mediaView;
    private ImageView imageView;

    private MediaPlayer mediaPlayer;

    private DBHandler dbHandler;

    private MediaType actualMediaType = null;

    MediaContainersManager(MediaView mediaView, ImageView imageView) {
        this.mediaView = mediaView;
        this.imageView = imageView;
        mediaView.setMediaPlayer(mediaPlayer);
    }

    enum MediaType{
        VIDEO,
        IMAGE
    }

    // TODO: load source from FTP? probably no, because FTPConnector

    /**
     *
     * @param path source path to file
     * @param mediaType content type (VIDEO, IMAGE)
     * @return if changing resource was successfull (true) or not (false)
     */
    boolean setResource(String path, MediaType mediaType) {
        // checking if file with given path is availible
        if (!new File(path).exists()) {
            return false;
        }

        if (mediaType == MediaType.IMAGE) {
            imageView.setImage(new Image(new File(path).toURI().toString()));
        }
        else {
            try {
                Media media = new Media(new File(path).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
            }
            // bad extensions e.g. JPG
            catch (MediaException exception) {
                return false;
            }
            mediaView.setMediaPlayer(mediaPlayer);
        }
        actualMediaType = mediaType;
        return true;
    }

    void play() throws InterruptedException{

        if (actualMediaType == MediaType.IMAGE) {
            mediaView.setVisible(false);
            imageView.setVisible(true);
        }
        else {
            imageView.setVisible(false);
            mediaView.setVisible(true);
            mediaPlayer.play();
        }
    }
}


