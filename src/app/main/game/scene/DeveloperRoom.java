package app.main.game.scene;

import java.util.Vector;

import app.main.game.object.player.Player;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;

public class DeveloperRoom extends GameScene {

  public DeveloperRoom() {
    super();
    setBorder(3);
  }
  
  private Player player;

  @Override
  protected void initializeGameObjects(Vector<GameObject> gameObjects) {
    gameObjects.add(player = new Player(this));
    player.setPosition(50, 50);
  }
}
