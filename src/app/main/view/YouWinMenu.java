package app.main.view;

import app.main.controller.GameController;
import app.main.controller.HighScoreController;
import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.utility.SceneTemplate;
import app.utility.Utility;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.TimerTask;

public class YouWinMenu extends SceneTemplate {
    private BorderPane base;
    private VBox center;
    private Label title;
    private TextField name;
    private HBox buttons;
    private Button submit;
    private Button cancel;
    private AssetManager asset;
    private SceneController controller;

    @Override
    public Node initComponents() {
        System.out.println(GameController.getInstance().getWinTime());

        base = new BorderPane();
        controller = SceneController.getInstance();

        asset = AssetManager.getInstance();
        base.setCenter(center = new VBox(
                title = new Label("Input Your Name"),
                name = new TextField(),
                buttons = new HBox(
                        cancel = new Button("Cancel"),
                        submit = new Button("Submit")
                )
        ));

        return base;
    }

    @Override
    public void initStyle() {
        Image bigFrame = asset.findImage("frame");

        base.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        center.setMaxSize(800, 500);
        center.setPadding(new Insets(10));
        center.setBackground(new Background(new BackgroundImage(bigFrame, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(800, 500, false, false, false, false))));
        center.setAlignment(Pos.CENTER);

        Font titleFont = FontManager.loadFont(32);
        Font textFont = FontManager.loadFont(24);

        applyFont(title, titleFont);
        applyButtonStyle(cancel, textFont);
        applyButtonStyle(submit, textFont);

        title.setPadding(new Insets(0, 0, 60, 0));

        name.setMaxWidth(600);
        name.setFont(textFont);
        name.setAlignment(Pos.CENTER);
        name.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: #fcdc80; " +
                "-fx-border-radius: 8; " +
                "-fx-padding: 10 24 10 24; " +
                "-fx-border-width: 1.5; " +
                "-fx-text-fill: white;");

        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(30);
        center.setSpacing(20);
    }

    private void applyFont(Label label, Font font) {
        label.setFont(font);
        label.setTextFill(Color.WHITE);
    }

    private void applyButtonStyle(Button button, Font font) {
        AssetManager manager = AssetManager.getInstance();
        String defaultStyle = "-fx-background-color: transparent; " + "-fx-border-width: 3px; "
                + "-fx-border-radius: 5px; " + "-fx-text-fill: white; ";
        button.setFont(font);
        button.setStyle(defaultStyle + "-fx-border-color: transparent;");
        button.focusedProperty().addListener((obs, oldVal, newVal) -> {
            button.setStyle(
                    defaultStyle + (newVal ? "-fx-border-color: #fcdc80;" : "-fx-border-color: transparent;"));
        });
        button.hoverProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                button.requestFocus();
                AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_cursor_8")).playThenDestroy();
            } else {
                button.getParent().requestFocus();
            }
        });
    }

    @Override
    public void eventHandling() {
        name.textProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.length() > 20) {
                name.setText(oldVal);
            }
        });

        cancel.setOnMouseClicked((e) -> {
            AudioFactory.createSfxHandler(asset.findAudio("sfx_menu_select_8")).playThenDestroy();
            controller.switchScene(new MainMenu(), 500);

        });
        submit.setOnMouseClicked((e) -> {
            AudioFactory.createSfxHandler(asset.findAudio("sfx_menu_select_8")).playThenDestroy();
            HighScoreController.getInstance().saveHighScore(name.getText(), GameController.getInstance().getWinTime());
            controller.switchScene(new MainMenu(), 500);
        });
    }

    @Override
    public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {

    }
}
