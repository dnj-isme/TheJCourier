package app.main.game.scene;

import java.util.Vector;

import app.main.game.object.boss.BossPlaceholder;
import app.main.game.object.player.Player;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;

public class DeveloperRoom extends GameScene {

  public DeveloperRoom() {
    super();
    setBorder(3);
  }
  
  private Player player;
  private BossPlaceholder placeholder;
  
  private Vector<GameObject> enemyEntities;

  @Override
  protected void initializeGameObjects() {
    enemyEntities = new Vector<GameObject>();
    
    getGameObjects().add(player = new Player(this));
    player.setPosition(50, 50);
    placeholder = new BossPlaceholder(this);
    placeholder.setPosition(200, GameScene.HEIGHT - placeholder.getSize().getY());
    addEnemyEntity(placeholder);
  }
  
  private void addEnemyEntity(GameObject object) {
    getGameObjects().add(object);
    enemyEntities.add(object);
  }

  @Override
  public void performGameLogic(RenderProperties properties) {
    for (GameObject enemy : enemyEntities) {
      if(enemy instanceof Collidable) {
        if(player.collides(enemy)) {
          player.receiveCollision(enemy);
        }
      }
    }
  }
}
