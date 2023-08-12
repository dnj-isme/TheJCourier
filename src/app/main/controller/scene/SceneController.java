package app.main.controller.scene;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import app.utility.SceneTemplate;
import app.utility.StageProperty;
import app.utility.Utility;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SceneController {
  public static double getScreenWidth() {
    return Screen.getPrimary().getVisualBounds().getWidth();
  }

  public static double getScreenHeight() {
    return Screen.getPrimary().getVisualBounds().getHeight();
  }
  
  public static void sleep(int millis) {
    try {
      TimeUnit.MILLISECONDS.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static SceneController instance;

  public static SceneController getInstance() {
    if (instance == null) {
      instance = new SceneController();
    }
    return instance;
  }

  private SceneController() {
    observer = SceneEventObserver.getInstance();
  }

  private SceneTemplate activeScene;
  private SceneEventObserver observer;

  private Stage primaryStage;
  private Scene scene;
  private StackPane base;
  private boolean binded;
  
  public SceneTemplate getActiveScene() {
    return activeScene;
  }

  public Scene getScene() {
    return scene;
  }

  public boolean isBinded() {
    return binded;
  }
  
  public void bind(Stage stage) {
    if (!binded) {
      primaryStage = stage;
      primaryStage.setScene(scene = new Scene(base = new StackPane()));
      observer.start(scene);
//      scene.setCursor(Cursor.NONE);
      binded = true;
    } else {
      Utility.debug("Primary stage has been binded!");
    }
  }

  public void bind(Stage stage, StageProperty property) {
    property.apply(stage);
    bind(stage);
  }

  public void switchScene(SceneTemplate target) {
    if (binded) {
      Utility.debug("Scenes = ");
      for(Node node : base.getChildren()) {
        Utility.debug(node.getId());
      }
      target.initalize();
      base.getChildren().clear();
      observer.removeAll();
      base.getChildren().add(target.getNode());
      observer.register(target);
      activeScene = target;
//      Utility.debug("Switching to " + target.getClass().getName());
    } else {
//      Utility.debug("Primary stage hasn't been binded yet!");
    }
  }

  public void switchScene(SceneTemplate target, int millis) {
    Utility.delayAction(millis, new TimerTask() {
      @Override
      public void run() {
        // TODO Auto-generated method stub
        Platform.runLater(() -> {
          switchScene(target);
        });
      }
    });
  }
}
