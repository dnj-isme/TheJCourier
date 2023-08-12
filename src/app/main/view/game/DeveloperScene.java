package app.main.view.game;

import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.scene.DeveloperRoom;
import javafx.geometry.Insets;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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
