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

public class DeveloperScene2 extends GamePageTemplate{
  public DeveloperScene2() {
    super(new DeveloperRoom());
  }

}
