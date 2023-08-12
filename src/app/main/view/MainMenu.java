package app.main.view;

import java.util.Optional;
import java.util.TimerTask;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.view.component.Copyright;
import app.main.view.component.TopLogo;
import app.main.view.game.DeveloperScene;
import app.main.view.game.SpawnScene;
import app.utility.Message;
import app.utility.SceneTemplate;
import app.utility.Utility;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

public class MainMenu extends SceneTemplate {

  private BorderPane base;
  private AssetManager manager;
  private SceneController controller;

  // TOP
  private Label logo;

  // CENTER
  private VBox center;
    private Button playBtn;
    private Button highScoreBtn;
    private Button optionBtn;
    private Button quitBtn;

  Media submit_sfx = manager.findAudio("sfx_game_start");

  // BOTTOM
  private VBox bottom;

  @Override
  public Node initComponents() {
    // TODO Auto-generated method stub
    manager = AssetManager.getInstance();
    controller = SceneController.getInstance();

    base = new BorderPane();

    base.setBackground(new Background(new BackgroundImage(manager.findImage("main_menu"), BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
    base.setTop(logo = new TopLogo());
    base.setCenter(center = new VBox(
        playBtn = new Button("Play"), 
        highScoreBtn = new Button("High Score"),
        optionBtn = new Button("Options"),
        quitBtn = new Button("Quit to Desktop")));
    base.setBottom(bottom = new Copyright());

    base.setFocusTraversable(true);
    base.requestFocus();
    return base;
  }

  @Override
  public void initStyle() {
    // TODO Auto-generated method stub

    logo.setPadding(new Insets(100));
    bottom.setPadding(new Insets(50));

    center.setAlignment(Pos.CENTER);
    bottom.setAlignment(Pos.CENTER);
    BorderPane.setAlignment(logo, Pos.CENTER);
    BorderPane.setAlignment(center, Pos.CENTER);
    BorderPane.setAlignment(bottom, Pos.CENTER);

    Font font = FontManager.loadFont(18);
    applyButtonStyle(playBtn, font);
    applyButtonStyle(optionBtn, font);
    applyButtonStyle(highScoreBtn, font);
    applyButtonStyle(quitBtn, font);
    center.setSpacing(10);
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
  private boolean pressSomething = false;

  @Override
  public void eventHandling() {
    // TODO Auto-generated method stub
    playBtn.setOnAction((e) -> {
      if(!pressSomething) {
        AudioFactory.createSfxHandler(manager.findAudio("sfx_game_start")).playThenDestroy();
        pressSomething = true;
        SceneTemplate target = GameController.getInstance().isDebug() ? new DeveloperScene() : new SpawnScene();
        controller.switchScene(target, 500);
      }
    });

    highScoreBtn.setOnAction((e) -> {
      if(!pressSomething) {
        AudioFactory.createSfxHandler(manager.findAudio("sfx_game_start")).playThenDestroy();
        pressSomething = true;
        controller.switchScene(new HighScoreMenu(), 500);
      }
    });

    optionBtn.setOnAction((e) -> {
      if(!pressSomething) {        
        AudioFactory.createSfxHandler(manager.findAudio("sfx_game_start")).playThenDestroy();
        pressSomething = true;
        controller.switchScene(new OptionMenu(), 500);
      }
    });

    quitBtn.setOnAction((e) -> {
      if(!pressSomething) {       
        AudioFactory.createSfxHandler(manager.findAudio("sfx_game_start")).playThenDestroy();
        pressSomething = true;
        
        Utility.delayAction(500, new TimerTask() {
          
          @Override
          public void run() {
            // TODO Auto-generated method stub
            Platform.runLater(() -> {
              System.exit(0);
            });
          }
        });
      }
    });
  }

  @Override
  public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {
    // TODO Auto-generated method stub
    
  }
}
