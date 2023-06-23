package app.main.view;

import app.main.controller.GameController;
import app.main.controller.HighScoreController;
import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.model.HighScoreRecord;
import app.utility.SceneTemplate;
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

import java.util.List;

public class HighScoreMenu extends SceneTemplate {
    private BorderPane base;
    private BorderPane center;
    private Label title;
    private GridPane scores;
        private Label name1, name2, name3, name4, name5;
        private Label score1, score2, score3, score4, score5;
    private Button back;
    private SceneController controller;
    private AssetManager asset;
    private HighScoreController highScoreController;

    @Override
    public Node initComponents() {
        asset = AssetManager.getInstance();
        base = new BorderPane();
        controller = SceneController.getInstance();
        highScoreController = HighScoreController.getInstance();

        base.setCenter(center = new BorderPane());
        
        center.setTop(title = new Label("High Scores"));
        center.setCenter(scores = new GridPane());
        center.setBottom(back = new Button("Back"));
        
        List<HighScoreRecord> recordList = highScoreController.getRanking();

        // Top 1
        if (!recordList.isEmpty()) {
            HighScoreRecord rank1 = recordList.get(0);
            if (rank1 != null) {
                scores.add(name1 = new Label(rank1.getName()), 0, 0);
                scores.add(score1 = new Label(rank1.getTimeSpentFormat()), 1, 0);
            }
        }

        // Top 2
        if (recordList.size() > 1) {
            HighScoreRecord rank2 = recordList.get(1);
            if (rank2 != null) {
                scores.add(name2 = new Label(rank2.getName()), 0, 1);
                scores.add(score2 = new Label(rank2.getTimeSpentFormat()), 1, 1);
            }
        }

        // Top 3
        if (recordList.size() > 2) {
            HighScoreRecord rank3 = recordList.get(2);
            if (rank3 != null) {
                scores.add(name3 = new Label(rank3.getName()), 0, 2);
                scores.add(score3 = new Label(rank3.getTimeSpentFormat()), 1, 2);
            }
        }

        // Top 4
        if (recordList.size() > 3) {
            HighScoreRecord rank4 = recordList.get(3);
            if (rank4 != null) {
                scores.add(name4 = new Label(rank4.getName()), 0, 3);
                scores.add(score4 = new Label(rank4.getTimeSpentFormat()), 1, 3);
            }
        }

        // Top 5
        if (recordList.size() > 4) {
            HighScoreRecord rank5 = recordList.get(4);
            if (rank5 != null) {
                scores.add(name5 = new Label(rank5.getName()), 0, 4);
                scores.add(score5 = new Label(rank5.getTimeSpentFormat()), 1, 4);
            }
        }

        return base;
    }

    @Override
    public void initStyle() {
        Image bigFrame = asset.findImage("frame");

        base.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        center.setMaxSize(800, 500);
        center.setPadding(new Insets(20));
        center.setBackground(new Background(new BackgroundImage(bigFrame, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(800, 500, false, false, false, false))));

        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(back, Pos.CENTER);
        BorderPane.setAlignment(scores, Pos.CENTER);

        Font titleFont = FontManager.loadFont(32);
        Font textFont = FontManager.loadFont(24);

        applyFont(title, titleFont);

        if (name1 != null) {
            applyFont(name1, textFont);
            name1.setMinWidth(500);
            applyFont(score1, textFont);
            score1.setAlignment(Pos.BASELINE_RIGHT);
        }

        if (name2 != null) {
            applyFont(name2, textFont);
            applyFont(score2, textFont);
            score2.setAlignment(Pos.BASELINE_RIGHT);
        }

        if (name3 != null) {
            applyFont(name3, textFont);
            applyFont(score3, textFont);
            score3.setAlignment(Pos.BASELINE_RIGHT);
        }

        if (name4 != null) {
            applyFont(name4, textFont);
            applyFont(score4, textFont);
            score4.setAlignment(Pos.BASELINE_RIGHT);
        }

        if (name5 != null) {
            applyFont(name5, textFont);
            applyFont(score5, textFont);
            score5.setAlignment(Pos.BASELINE_RIGHT);
        }

        applyButtonStyle(back, textFont);
    }

    private void applyFont(Label label, Font font) {
        label.setFont(font);
        label.setTextFill(Color.WHITE);
        label.setPadding(new Insets(0,0,30, 0));
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
        back.setOnMouseClicked((e) -> {
            AudioFactory.createSfxHandler(asset.findAudio("sfx_menu_select_8")).playThenDestroy();
            controller.switchScene(new MainMenu(), 500);
        });
    }

    @Override
    public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {

    }
}
