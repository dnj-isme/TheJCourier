package app.main.view.game;

import java.util.TimerTask;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.shuriken.ShurikenPool;
import app.main.game.scene.DeveloperRoom;
import app.main.game.scene.SpawnRoom;
import app.main.view.component.KeyBindingComponent;
import app.main.view.component.OptionComponent;
import app.main.view.component.PauseComponent;
import app.utility.SceneTemplate;
import app.utility.Utility;
import app.utility.canvas.GameScene;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DeveloperScene extends GamePageTemplate{
  private static DeveloperRoom dev;
  public DeveloperScene() {
    super(dev = new DeveloperRoom());
    getBase().setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
  }
  
  @Override
  public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {
    super.handleSceneKeyChanges(sceneEventObserver);
    if(sceneEventObserver.isPressing(KeyCode.G)) {
      dev.stop();
      SceneController.getInstance().switchScene(new SpawnScene());
    }
    if(sceneEventObserver.isPressing(KeyCode.P)) {
      dev.getPlayer().setDead();
    }
  }

  @Override
  public void handlePause() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void handleResume() {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected void handleStop() {
  }
}
