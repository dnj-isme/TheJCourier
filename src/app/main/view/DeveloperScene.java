package app.main.view;

import app.main.controller.scene.SceneEventObserver;
import app.main.game.scene.DeveloperRoom;
import app.utility.SceneTemplate;
import app.utility.canvas.GameScene;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class DeveloperScene extends SceneTemplate{
  private BorderPane base;
  private DeveloperRoom dev;
  @Override
  public Node initComponents() {
    base = new BorderPane();
    dev = new DeveloperRoom();
    base.setCenter(dev.getCanvas());
    dev.start();
    return base;
  }

  @Override
  public void initStyle() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void eventHandling() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {
    // TODO Auto-generated method stub
    
  }
}
