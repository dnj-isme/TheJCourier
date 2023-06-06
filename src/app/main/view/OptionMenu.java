package app.main.view;

import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.view.component.KeyBindingComponent;
import app.main.view.component.OptionComponent;
import app.utility.SceneTemplate;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;

public class OptionMenu extends SceneTemplate {
  SceneController controller;
  AssetManager manager;

  private BorderPane center;
  
  private OptionComponent option;
  private KeyBindingComponent keyBinding;

  @Override
  public Node initComponents() {
    controller = SceneController.getInstance();
    manager = AssetManager.getInstance();

    center = new BorderPane();
    
    option = new OptionComponent();
    keyBinding = new KeyBindingComponent();
    
    option.setOnBackEvent(this::navigateBack);
    option.setOnControlEvent(this::navigateControl);
    
    keyBinding.setOnBackEvent(this::navigateToOption);
    
    center.setCenter(option);

    return center;
  }
  
  private void navigateToOption(ActionEvent e) {
    AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_select_8")).playThenDestroy();
    center.setCenter(option);
  }
  
  private void navigateBack(ActionEvent e) {
    AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_select_8")).playThenDestroy();
    controller.switchScene(new MainMenu(), 500);
  }
  
  private void navigateControl(ActionEvent e) {
    AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_select_8")).playThenDestroy();
    center.setCenter(keyBinding);
  }


  @Override
  public void initStyle() {
    // TODO Auto-generated method stub
    center.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
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
