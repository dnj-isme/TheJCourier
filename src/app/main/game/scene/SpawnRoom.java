package app.main.game.scene;

import java.util.Vector;

import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.game.object.boss.BossPlaceholder;
import app.main.game.object.other.DemonHiveController;
import app.main.game.object.other.Lantern;
import app.main.game.object.other.DemonHiveController.HiveTag;
import app.main.game.object.player.Player;
import app.main.game.object.player.shuriken.ShurikenPool;
import app.main.game.object.player.swing.PlayerGlideSwing;
import app.main.game.object.player.swing.PlayerSwing;
import app.utility.Utility;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.application.Platform;

public class SpawnRoom extends GameScene{

  private Player player;
  private PlayerSwing playerSwing;
  private PlayerGlideSwing playerGlideSwing;
  private ShurikenPool pool;
  private AssetManager manager;
  
  private Vector<GameObject> enemyEntities;
  
  @Override
  protected void initializeGameObjects() {
    enemyEntities = new Vector<GameObject>();
    manager = AssetManager.getInstance();
    
    addGameObject(player = Player.getInstance(this));
    player.reset(this);
    player.setPosition(50, 50);
    
    playerSwing = player.getPlayerSwing();
    playerGlideSwing = player.getPlayerGlideSwing();
    pool = player.getPool();
    
//    addEnemyEntity(lantern1 = new Lantern(this));
//    addEnemyEntity(lantern2 = new Lantern(this));
    
//    lantern1.setPosition(100, GameScene.HEIGHT - placeholder.getSize().getY() - 50);
//    lantern2.setPosition(300, GameScene.HEIGHT - placeholder.getSize().getY() - 100);
  }
  
  private void addEnemyEntity(GameObject entity) {
    addGameObject(entity);
    enemyEntities.add(entity);
  }
  
  private long lastDone = 0;

  @Override
  public void performGameLogic(RenderProperties properties) {
    for (GameObject enemy : enemyEntities) {
      if(enemy instanceof Collidable) {
        if(player.collides(enemy)) {
          player.receiveCollision(enemy);
        }
        if(player.isAttacking() && playerSwing.collides(enemy)) {
          ((Collidable) enemy).receiveCollision(player);
          player.setCloudStep(true);
          if(lastDone != player.getAttackCount()) {
            Platform.runLater(() -> {
              AudioFactory.createSfxHandler(manager.findAudio("sfx_swordhit_" + Utility.random(1, 3))).playThenDestroy();
              lastDone = player.getAttackCount();
            });
          }
        }
        if(player.isAttacking() && playerGlideSwing.collides(enemy)) {
          ((Collidable) enemy).receiveCollision(player);
          player.setCloudStep(true);
          player.setGlideHit(true);
          if(lastDone != player.getAttackCount()) {
            Platform.runLater(() -> {
              AudioFactory.createSfxHandler(manager.findAudio("sfx_swordhit_" + Utility.random(1, 3))).playThenDestroy();
              lastDone = player.getAttackCount();
            });
          }
        }
      }
    }
  }
}