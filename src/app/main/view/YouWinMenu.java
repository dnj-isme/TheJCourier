package app.main.view;

import app.main.controller.GameController;
import app.main.controller.HighScoreController;
import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.utility.Message;
import app.utility.SceneTemplate;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

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
        title.setTextAlignment(TextAlignment.CENTER);

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
    String defaultStyle = "-fx-background-color: transparent; "
            + "-fx-border-width: 3px; "
            + "-fx-border-radius: 5px; "
            + "-fx-text-fill: white; ";
    button.setFont(font);
    button.setStyle(defaultStyle + "-fx-border-color: transparent;");

    final PauseTransition pause = new PauseTransition(Duration.millis(200));

    button.setOnMouseEntered(event -> {
      pause.stop();
      button.requestFocus();
    });

    button.setOnMouseExited(event -> {
      pause.setOnFinished(e -> base.requestFocus());
      pause.play();
    });

    button.focusedProperty().addListener((obs, oldVal, newVal)-> {
      button.setStyle(defaultStyle + (newVal ? "-fx-border-color: #fcdc80;" : "-fx-border-color: transparent;"));

      if(newVal) {
        AudioFactory.createSfxHandler(AssetManager.getInstance().findAudio("sfx_menu_cursor_8")).playThenDestroy();
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
            if(name.getText().length() == 0) {
            	title.setText("The name cannot be\nempty!");
                return;
            }
            HighScoreController.getInstance().saveHighScore(name.getText(), GameController.getInstance().getWinTime());
            controller.switchScene(new MainMenu(), 500);
        });
    }

    @Override
    public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {

    }
}
