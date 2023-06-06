package app.main.view;


import java.util.TimerTask;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.shuriken.ShurikenPool;
import app.main.game.scene.DeveloperRoom;
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


public abstract class GamePageTemplate extends SceneTemplate{
  private StackPane base;
  private GameScene dev;
  private KeyBinding keyBinding;
  private AssetManager manager;
  
  private PauseComponent pauseComponent;
  private OptionComponent optionComponent;
  private KeyBindingComponent keyBindingComponent;
  public GamePageTemplate(GameScene scene) {
    super();
    startGame(scene);
  }
  
  private void startGame(GameScene scene) {
    dev = scene;
    BorderPane.setAlignment(dev.getCanvas(), Pos.CENTER);
    base.getChildren().add(dev.getCanvas());
    dev.start();
  }

  @Override
  public Node initComponents() {
    base = new StackPane();
    manager = AssetManager.getInstance();
    keyBinding = KeyBinding.getIntance();
    
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
      dev.resume();
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
      dev.stop();
      dev.reset();
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
    if(sceneEventObserver.isPressing(keyBinding.getBinding(KeyBinding.PAUSE)) && !dev.isPaused()) {
      System.out.println("Hello World");
      sceneEventObserver.setPressing(keyBinding.getBinding(KeyBinding.PAUSE), false);
      dev.pause();
      AudioFactory.createSfxHandler(AssetManager.getInstance().findAudio("sfx_pause")).playThenDestroy();
      base.getChildren().add(pauseComponent);
      pauseComponent.requestFocus();
    }
  }
}
