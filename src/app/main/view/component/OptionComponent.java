package app.main.view.component;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.audio.AudioHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class OptionComponent extends BorderPane implements Component {
    private EventHandler<ActionEvent> onBackEvent;
    private EventHandler<ActionEvent> onControlEvent;

    private AssetManager asset;
    private GameController gameController;
    
    public OptionComponent() {
        asset = AssetManager.getInstance();
        gameController = GameController.getInstance();

        Font fontTitle = FontManager.loadFont(28);
        Font fontText = FontManager.loadFont(18);

        Image bigFrame = asset.findImage("frame");

        this.setMaxSize(800, 500);
        this.setPadding(new Insets(10));
        this.setBackground(new Background(new BackgroundImage(bigFrame, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(800, 500, false, false, false, false))));

        Label top = new Label("Options");
        BorderPane center = new BorderPane();
        GridPane options = new GridPane();

        // Music Option
        Label musicLbl = new Label("Music");
        CustomSlider musicSlider = new CustomSlider(gameController.getMusic(), 8, 20);

        // SFX Option
        Label sfxLbl = new Label("Sound FX");
        CustomSlider sfxSlider = new CustomSlider(gameController.getSfx(), 8, 20);

        // Show Timer
        Label showTimeLbl = new Label("Show Timer");
        CheckBox showTimeCB = new CheckBox();
        showTimeCB.setSelected(gameController.isShowTimer());

        HBox buttons = new HBox();
        Button backBtn = new Button("[   Back   ]");
        Button setControlBtn = new Button("[ Controls ]");

        center.setMaxSize(700, 400);
        options.setHgap(300);

        center.setBackground(new Background(new BackgroundImage(bigFrame, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(700, 400, false, false, false, false))));

        this.setTop(top);
        this.setCenter(center);

        center.setTop(options);
        center.setBottom(buttons);

        options.add(musicLbl, 0, 0);
        options.add(sfxLbl, 0, 1);
        options.add(showTimeLbl, 0, 2);

        options.add(musicSlider, 1, 0);
        options.add(sfxSlider, 1, 1);
        options.add(showTimeCB, 1, 2);

        buttons.getChildren().addAll(setControlBtn, backBtn);

        applyFont(top, fontTitle);
        applyFont(sfxLbl, fontText);
        applyFont(musicLbl, fontText);
        applyFont(showTimeLbl, fontText);

        applyButtonStyle(setControlBtn, fontText);
        applyButtonStyle(backBtn, fontText);

        BorderPane.setAlignment(center, Pos.CENTER);
        BorderPane.setAlignment(top, Pos.CENTER);
        top.setPadding(new Insets(10));

        options.setPadding(new Insets(30, 20, 0, 20));
        options.setVgap(10);

        AudioHandler musicHandler = AudioFactory.createMusicHandler(asset.findAudio("sample_music"), true);
        MediaPlayer player = musicHandler.getPlayer();

        musicSlider.setOnMouseReleased((e) -> {
            if (player.getStatus() == Status.PLAYING) {
                musicHandler.getPlayer().stop();
            }
        });

        musicSlider.setOnValueChanged((oldVal, newVal) -> {
            gameController.setMusic(newVal);
            player.setVolume((double) newVal / 10);
            if (player.getStatus() != Status.PLAYING) {
                musicHandler.play();
            }
        });

        showTimeCB.setOnAction((e) -> {
            AudioFactory.createSfxHandler(asset.findAudio("sfx_menu_cursor_8")).playThenDestroy();
            gameController.setShowTimer(showTimeCB.isSelected());
        });

        sfxSlider.setOnValueChanged((oldVal, newVal) -> {
            gameController.setSfx(newVal);
            AudioFactory.createSfxHandler(asset.findAudio("sfx_menu_cursor_8")).playThenDestroy();
        });

        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(20));
        buttons.setSpacing(30);

        backBtn.setOnAction((e) -> {
            if (onBackEvent != null) {
                onBackEvent.handle(e);
            }
        });

        setControlBtn.setOnAction((e) -> {
            if (onControlEvent != null) {
                onControlEvent.handle(e);
            }
        });
    }

    public void setOnBackEvent(EventHandler<ActionEvent> eventHandler) {
        this.onBackEvent = eventHandler;
    }

    public void setOnControlEvent(EventHandler<ActionEvent> eventHandler) {
        this.onControlEvent = eventHandler;
    }

    private void applyFont(Labeled node, Font font) {
        node.setFont(font);
        node.setTextFill(Color.WHITE);
    }

    private void applyButtonStyle(Button button, Font font) {
        AssetManager manager = AssetManager.getInstance();
        String defaultStyle = "-fx-background-color: transparent; " + "-fx-border-width: 3px; "
                + "-fx-border-radius: 5px; " + "-fx-text-fill: white; ";
        button.setFont(font);
        button.setStyle(defaultStyle + "-fx-border-color: transparent;");
        button.hoverProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal && !oldVal) {
                AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_cursor_8")).playThenDestroy();
                button.setStyle(defaultStyle + "-fx-border-color: #fcdc80;");
            } else {
                button.setStyle(defaultStyle + "-fx-border-color: transparent;");
            }
        });
    }
}
