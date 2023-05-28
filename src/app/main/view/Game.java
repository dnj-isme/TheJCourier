package app.main.view;

import app.main.controller.scene.SceneEventObserver;
import app.utility.SceneTemplate;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class Game extends SceneTemplate{
  private BorderPane base;
  @Override
  public Node initComponents() {
    base = new BorderPane();
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
