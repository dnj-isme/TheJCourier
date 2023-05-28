package app.utility;

import app.main.controller.scene.SceneEventObserver;
import javafx.scene.Node;

public abstract class SceneTemplate {
  private Node base;

  private boolean initialized = false;
  
  public SceneTemplate() {
    base = initComponents();
  }

  public Node getNode() {
    if(!initialized) {
      Utility.debug("Warning! The node isn't initialized yet");
    }
    return base;
  }

  public final void initalize() {
    initStyle();
    eventHandling();
    initialized = true;
  }

  public abstract Node initComponents();

  public abstract void initStyle();

  public abstract void eventHandling();

  public abstract void handleSceneKeyChanges(SceneEventObserver sceneEventObserver);

  @Override
  public SceneTemplate clone() {
    try {
      return (SceneTemplate) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}
