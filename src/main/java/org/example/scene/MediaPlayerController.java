package org.example.scene;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.Settings;
import org.example.database.DBHandler;
import org.example.database.adapter.DBHandlerSQLite;
import org.example.files.FTPConnector;
import org.example.files.FileManager;
import org.example.files.XMLSettingsLoader;
import org.example.model.NewsTicker;
import org.example.model.Properties;
import org.example.model.Slide;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MediaPlayerController {

    @FXML
    public ImageView imageView;

    @FXML
    public MediaView mediaView;

    @FXML
    public Rectangle rectangle;

    @FXML
    public Text text;

    @FXML
    public VBox startMenu;

    @FXML
    public TextField ipAddressTextField;

    @FXML
    public TextField contentPathTextField;

    @FXML
    public TextField dbFileNameTextField;

    @FXML
    public TextField loginTextField;

    @FXML
    public PasswordField passwordTextField;

    @FXML
    public Label errorLabel;

    @FXML
    public Slider barSizeSlider;

    @FXML
    public Slider barOpacitySlider;

    @FXML
    public ColorPicker textColorPicker;

    @FXML
    public ColorPicker barColorPicker;


    private MediaContainersManager mediaManager;
    private NewsTickerManager newsTickerManager;
    private DBHandler dbHandler;
    private FTPConnector ftpConnector;

    @FXML
    public void initialize() {


        //podpięcie mediaView do kontenera, dzięki czemu zawsze rozmiar będzie równemu rozmiarowi okna
        mediaView.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                mediaView.fitWidthProperty().bind(newScene.widthProperty());
                mediaView.fitHeightProperty().bind(newScene.heightProperty());
                imageView.fitWidthProperty().bind(newScene.widthProperty());
                imageView.fitHeightProperty().bind(newScene.heightProperty());
            }
        });

        // injecting containers to manager
        mediaManager = new MediaContainersManager(mediaView, imageView);
        newsTickerManager = new NewsTickerManager(text);

        if (Settings.isSettingsLoaded) Platform.runLater(this::play);
    }

    private void setAllSceneProperties() {
        startMenu.setVisible(false);
        rectangle.setVisible(true);
        text.setVisible(true);
        Stage stage = (Stage) text.getScene().getWindow();
        rectangle.setHeight(stage.getHeight() * barSizeSlider.getValue() / 100);
        rectangle.setFill(barColorPicker.getValue());
        rectangle.setWidth(5000);
        rectangle.setOpacity(1 - barOpacitySlider.getValue() / 100);
        text.setFill(textColorPicker.getValue());
        text.setFont(Font.font(
                "System",
                FontWeight.NORMAL,
                FontPosture.REGULAR,
                stage.getHeight() * barSizeSlider.getValue() / 100 - 10));

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            rectangle.setHeight(newVal.intValue() * barSizeSlider.getValue() / 100);
            text.setFont(Font.font(
                    "System",
                    FontWeight.NORMAL,
                    FontPosture.REGULAR,
                    newVal.intValue() * barSizeSlider.getValue() / 100 - 10));
        });

    }

    // TODO: make informationBar animation independent
    @FXML
    public void play() {
        if (!Settings.isSettingsLoaded) {
            Settings.login = loginTextField.getText();
            Settings.password = passwordTextField.getText();
            Settings.serverAddr = ipAddressTextField.getText();
            Settings.contentPath = contentPathTextField.getText();
            Settings.dbFileName = dbFileNameTextField.getText();
        }

        Settings.isSettingsLoaded = false;

        ftpConnector = new FTPConnector(
                Settings.login,
                Settings.password,
                Settings.serverAddr,
                21
        );

        //check if it is possible to connect to ftp server
        boolean connectionStatus = ftpConnector.checkConnection();
        if (!connectionStatus) {
            errorLabel.setVisible(true);
            errorLabel.setText("Cannot connect to FTP Server!");
            return;
        }

        //download db file for first time from ftp server
        boolean dbFileDownloadSuccess = ftpConnector.downloadFile(Settings.contentPath, Settings.dbFileName);
        if (!dbFileDownloadSuccess) {
            errorLabel.setVisible(true);
            errorLabel.setText("Cannot download db file from FTP Server!");
            return;
        }

        startSlideShow();
    }

    private void startSlideShow() {
        setAllSceneProperties();
        dbHandler = new DBHandlerSQLite("jdbc:sqlite:./content/" + Settings.dbFileName);

        //thread responsible for showing new slides/videos
        //have to be in thread to not block UI thread
        Thread mediaThread = new Thread(() -> {
            while(true) {

                ftpConnector.downloadFile(Settings.contentPath, Settings.dbFileName);
                //setup DB Connection
                dbHandler = new DBHandlerSQLite("jdbc:sqlite:./content/" + Settings.dbFileName);

                List<Slide> slides = dbHandler.readSlides(); //slide representation from db
                Set<String> fileNames = slides.stream()
                        .flatMap(slide -> Stream.of(slide.getName()))
                        .collect(Collectors.toSet()); //get set of filenames required in this iteration

                FileManager.updateFilesInDirectory(fileNames, ftpConnector, Settings.contentPath);

                //change properties of news ticher
                Properties properties = dbHandler.readProperties();
                if (properties != null) Platform.runLater(() -> changeProperties(properties));

                for (Slide slide : slides) {
                    boolean status = mediaManager.setResource("./content/" + slide.getName(),
                            slide.isImage() ? MediaContainersManager.MediaType.IMAGE : MediaContainersManager.MediaType.VIDEO);

                    //if setting new resource was fail skip slide (play time == 0)
                    int duration = status ? slide.getDuration() : 0;

                    // run changes in JavaFX thread
                    Platform.runLater(() -> {
                        try {
                            mediaManager.play();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });

                    //wait until time pass
                    try {
                        TimeUnit.SECONDS.sleep(duration);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //thread responsible from changing and showing text in news ticker bar
        Thread newsTickerThread = new Thread(() -> {
            while(true) {
                List<NewsTicker> newsTickers = dbHandler.readNewsTickers();

                for (NewsTicker newsTicker : newsTickers) {
                    // run animation in JavaFX thread for each element
                    Platform.runLater(() -> {
                        newsTickerManager.play(newsTicker.getText(), newsTicker.getDuration());
                    });

                    // wait until time pass
                    try {
                        TimeUnit.SECONDS.sleep(newsTicker.getDuration());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        // thanks to this after main app close all threads will automatically terminate
        mediaThread.setDaemon(true);
        newsTickerThread.setDaemon(true);

        mediaThread.start();
        newsTickerThread.start();
    }

    public void changeProperties(Properties properties) {
        Stage stage = (Stage) text.getScene().getWindow();
        rectangle.setFill(Color.web(properties.getBarColor()));
        rectangle.setOpacity(1 - properties.getBarOpacity().doubleValue() / 100);
        text.setFill(Color.web(properties.getTextColor()));
        text.setFont(Font.font(
                "System",
                FontWeight.NORMAL,
                FontPosture.REGULAR,
                stage.getHeight() * properties.getScreenShare() / 100 - 10)
        );
        rectangle.setHeight(stage.getHeight() * properties.getScreenShare() / 100);
        barSizeSlider.setValue(properties.getScreenShare());
    }
}
