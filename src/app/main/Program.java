package app.main;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.main.controller.scene.SceneController;
import app.main.game.scene.BossRoom;
import app.main.view.*;
import app.main.view.game.BossScene;
import app.main.view.game.DeveloperScene;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Program extends Application {
  public static void main(String[] args) {
    // Auto Migration
    boolean debug = false;
    boolean invincible = false;
    boolean hitbox = false;
    
    for(int i = 0; i < args.length; i++) {
    	System.out.println(args[i]);
    }	

    if (args.length > 0) {
      String mode = args[0];
      switch (mode) {
      case "--test":
        debug = true;
        hitbox = true;
        System.out.println("Running on Testing Mode");
        break;
      case "--dev":
        debug = true;
        invincible = true;
        hitbox = true;
        System.out.println("Running on Development Mode");
        break;
      case "--easy":
        invincible = true;
        System.out.println("Running on Easy Mode");
        break;
      case "--demo":
        hitbox = true;
        System.out.println("Running on Demo Mode");
        break;
      case "--demo-easy":
        hitbox = true;
        invincible = true;
        System.out.println("Running on Demo Mode but the player is having a Skill Issue");
        break;
      default:
        System.out.println("Running on Normal Mode");
        break;
      }
    } else {
      System.out.println("Running on Normal Mode");
    }

    GameController gameController = GameController.getInstance();
    gameController.configure(debug, invincible, hitbox);
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    SceneController sceneController = SceneController.getInstance();

    System.out.println(SceneController.getScreenWidth());
    System.out.println(SceneController.getScreenHeight());
    sceneController.bind(primaryStage, (stage) -> {
      AssetManager asset = AssetManager.getInstance();
      stage.setFullScreen(true);
      stage.setMaximized(true);
      stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
      stage.getIcons().add(asset.findImage("icon"));
    });
    sceneController.switchScene(new MainMenu());
    primaryStage.show();
  }
}
