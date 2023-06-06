package app.main.view;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class DeveloperScene extends SceneTemplate{
  private StackPane base;
  private GameScene gameScene;
  private KeyBinding keyBinding;
  private AssetManager manager;
  
  private PauseComponent pauseComponent;
  private OptionComponent optionComponent;
  private KeyBindingComponent keyBindingComponent;
  
  @Override
  public Node initComponents() {
    base = new StackPane();
    manager = AssetManager.getInstance();
    keyBinding = KeyBinding.getIntance();
    gameScene = new DeveloperRoom();
    BorderPane.setAlignment(gameScene.getCanvas(), Pos.CENTER);
    base.getChildren().add(gameScene.getCanvas());
    gameScene.start();
    
    pauseComponent = new PauseComponent();
    optionComponent = new OptionComponent();
    keyBindingComponent = new KeyBindingComponent();
    
    return base;
  }
  

  @Override
  public void initStyle() {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void eventHandling() {
    // TODO Auto-generated method stub
    pauseComponent.setOnBackEvent((e) -> {
      AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_select_8")).playThenDestroy();
      base.getChildren().remove(pauseComponent);
      gameScene.resume();
      SceneController.getInstance().getScene();
    });
    pauseComponent.setOnOptionsEvent((e) -> {
      AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_select_8")).playThenDestroy();
      base.getChildren().remove(pauseComponent);
      base.getChildren().add(optionComponent);
    });
    
    pauseComponent.setOnTitleEvent((e) -> {
      AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_select_8")).playThenDestroy();
      SceneController.getInstance().switchScene(new MainMenu());
      gameScene.stop();
      gameScene.reset();
    });
        
    optionComponent.setOnBackEvent((e) -> {
      AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_select_8")).playThenDestroy();
      base.getChildren().remove(optionComponent);
      base.getChildren().add(pauseComponent);
    });
    
    optionComponent.setOnControlEvent((e) -> {
      AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_select_8")).playThenDestroy();
      base.getChildren().remove(optionComponent);
      base.getChildren().add(keyBindingComponent);
    });
    
    keyBindingComponent.setOnBackEvent((e) -> {
      AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_select_8")).playThenDestroy();
      base.getChildren().remove(keyBindingComponent);
      base.getChildren().add(optionComponent);
    });
  }
  
  @Override
  public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {
    if(sceneEventObserver.isPressing(keyBinding.getBinding(KeyBinding.PAUSE)) && !gameScene.isPaused()) {
      System.out.println("Hello World");
      sceneEventObserver.setPressing(keyBinding.getBinding(KeyBinding.PAUSE), false);
      gameScene.pause();
      AudioFactory.createSfxHandler(AssetManager.getInstance().findAudio("sfx_pause")).playThenDestroy();
      base.getChildren().add(pauseComponent);
      pauseComponent.requestFocus();
    }
    if(sceneEventObserver.isPressing(KeyCode.G)) {
      SceneController.getInstance().switchScene(new SpawnScene());
      gameScene.stop();
      gameScene.reset();
    }
  }
}
