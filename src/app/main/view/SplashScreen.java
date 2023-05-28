package app.main.view;

import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.view.component.Copyright;
import app.main.view.component.TopLogo;
import app.utility.SceneTemplate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SplashScreen extends SceneTemplate{
  private AssetManager manager;
  private SceneController sceneController;

  private BorderPane base;
  
  // TOP
  private Label logo;
  
  // CENTER
  private Label center;
  
  // BOTTOM
  private VBox copyright;

  @Override
  public Node initComponents() {
    // TODO Auto-generated method stub
    manager = AssetManager.getInstance();
    sceneController = SceneController.getInstance();
    
    base = new BorderPane();
    
    base.setTop(logo = new TopLogo());
    base.setCenter(center = new Label("Press ENTER to play"));
    base.setBottom(copyright = new Copyright());

    return base;
  }

  @Override
  public void initStyle() {
    base.setBackground(new Background(new BackgroundImage(
        manager.findImage("main_menu"), 
        BackgroundRepeat.NO_REPEAT, 
        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, 
        new BackgroundSize(100, 100, true, true, true, true))));
    
    Font font24 = FontManager.loadFont(24);

    
    applyFont(center, font24);
    
    BorderPane.setAlignment(logo, Pos.CENTER);
    BorderPane.setAlignment(copyright, Pos.CENTER);
    BorderPane.setAlignment(center, Pos.CENTER);
    
    logo.setPadding(new Insets(100));
    copyright.setPadding(new Insets(50));
    
    copyright.setAlignment(Pos.CENTER);
  }
  
  private void applyFont(Label label, Font font) {
    label.setFont(font);
    label.setTextFill(Color.WHITE);
  }
  
  @Override
  public void eventHandling() {
    
  }
  
  private boolean pressed = false;

  @Override
  public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {
    if(sceneEventObserver.isPressing(KeyCode.ENTER) && !pressed) {
      AudioFactory.createSfxHandler(manager.findAudio("sfx_game_start")).playThenDestroy();
      pressed = true;
      sceneController.switchScene(new MainMenu(), 500);
    }
  }
}
