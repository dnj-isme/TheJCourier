package app.main.view;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.utility.SceneTemplate;
import app.utility.Utility;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class IntroVideo extends SceneTemplate {

  private MediaView view;

  private AssetManager manager;
  private MediaPlayer mediaPlayer;
  private SceneController sceneController;
  
  private KeyBinding keyBind;
  
  @Override
  public Node initComponents() {
    // TODO Auto-generated method stub
    manager = AssetManager.getInstance();
    keyBind = KeyBinding.getIntance();
    
    sceneController = SceneController.getInstance();
    Media video = manager.findVideo("intro");
    
    mediaPlayer = new MediaPlayer(video);
    view = new MediaView(mediaPlayer);
    view.setPreserveRatio(true);
    view.setFitHeight(sceneController.getScene().getHeight());
    view.setFitWidth(sceneController.getScene().getWidth());
    return view;
  }
  
  @Override
  public void initStyle() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void eventHandling() {
    // TODO Auto-generated method stub
    
    mediaPlayer.setOnReady(() -> {
      mediaPlayer.setAutoPlay(true);
      mediaPlayer.play();
      Utility.debug("READY TO PLAY");
    });
    
  }

  @Override
  public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {
    if(sceneEventObserver.isPressing(keyBind.getBinding(KeyBinding.PAUSE)) ||
        sceneEventObserver.isPressing(keyBind.getBinding(KeyBinding.JUMP))) {
      mediaPlayer.stop();
      sceneController.switchScene(new SplashScreen());
    }
  }
}
