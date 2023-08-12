package app.main.view;

import app.main.controller.scene.SceneEventObserver;
import app.main.game.scene.InfamousBolaPantul;
import app.utility.SceneTemplate;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class TestScene extends SceneTemplate{
  private BorderPane base;
  private InfamousBolaPantul bolaPantul;
  @Override
  public Node initComponents() {
    base = new BorderPane();
    bolaPantul = new InfamousBolaPantul();
    base.setCenter(bolaPantul.getCanvas());
    bolaPantul.start();
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
