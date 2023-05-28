package app.main.controller.scene;

import java.util.ArrayList;
import java.util.HashMap;

import app.utility.SceneTemplate;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class SceneEventObserver {
  private static SceneEventObserver instance;
  public static SceneEventObserver getInstance() {
    if(instance == null) {
      instance = new SceneEventObserver();
    }
    return instance;
  }

  private HashMap<KeyCode, Boolean> pressStatus;
  private ArrayList<SceneTemplate> observants;
  
  private SceneEventObserver() {
    pressStatus = new HashMap<>();
    observants = new ArrayList<>();
  }
  
  public void start(Scene scene) {
    scene.setOnKeyPressed((e) -> {
      pressStatus.put(e.getCode(), true);
      
      notifyTarget();
      e.consume();
    });
    scene.setOnKeyReleased((e) -> {
      pressStatus.put(e.getCode(), false);
       
      notifyTarget(); 
      e.consume();  
    }); 
  }
  
  private void notifyTarget() {
    for (SceneTemplate target : observants) {
      target.handleSceneKeyChanges(this);
    }
  }
  
  public boolean isPressing(KeyCode code) {
    if(!pressStatus.containsKey(code)) return false;
    return pressStatus.get(code);
  }
  
  public void setPressing(KeyCode code, boolean isPressing) {
    pressStatus.put(code, isPressing);
    notifyTarget();
    System.out.println("Notified");
  }
  
  public void register(SceneTemplate target) {
     observants.add(target);
  }
  
  public void remove(SceneTemplate target) {
    observants.remove(target);
  }
  
  public void removeAll() {
    observants.clear();
  }
}
