package app.main.game.scene;

import java.util.Vector;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.boss.BossPlaceholder;
import app.main.game.object.other.Background;
import app.main.game.object.other.DemonHiveController;
import app.main.game.object.other.FloorTile;
import app.main.game.object.other.Gate;
import app.main.game.object.other.Lantern;
import app.main.game.object.other.DemonHiveController.HiveTag;
import app.main.game.object.player.Player;
import app.main.game.object.player.shuriken.ShurikenPool;
import app.main.game.object.player.swing.PlayerGlideSwing;
import app.main.game.object.player.swing.PlayerSwing;
import app.main.view.game.BossScene;
import app.utility.Utility;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.application.Platform;

public class SpawnRoom extends GameScene {
  
  private Player player;
  private Gate gate;
  private PlayerSwing playerSwing;
  private PlayerGlideSwing playerGlideSwing;
  private ShurikenPool pool;
  private AssetManager manager;
  private SceneEventObserver observer;
  private KeyBinding binding;
  
  private Vector<GameObject> enemyEntities;
  
  private Runnable onTransition;
  
  public void setOnTransition(Runnable onTransition) {
    this.onTransition = onTransition;
  }

  @Override
  protected void initializeGameObjects() {
    enemyEntities = new Vector<GameObject>();
    manager = AssetManager.getInstance();
    observer = SceneEventObserver.getInstance();
    binding = KeyBinding.getIntance();

    addGameObject(player = Player.getInstance(this));
    player.reset(this);
    player.setPosition(50, 50);
    player.setHideTimer(true);
    player.setSpawn(true);
    player.setReverseSpawn(false);

    playerSwing = player.getPlayerSwing();
    playerGlideSwing = player.getPlayerGlideSwing();
    pool = player.getPool();
    player.setHideTimer(true);
    player.setFloorHeight(20);
    player.setPosition(GameScene.WIDTH / 2 - player.getSize().getX() / 2, GameScene.HEIGHT - 20 - player.getSize().getY());
    
    addFloor();
    addForeground();
    addBackground();
  }
  
  private void addForeground() {
    // TODO Auto-generated method stub
    Lantern left = new Lantern(this);
    Lantern right = new Lantern(this);
    
    double y = (GameScene.HEIGHT - 20) - 120;
    double distance = 100;
    left.setPosition(GameScene.WIDTH / 2 - distance - left.getSize().getX() / 2, y);
    right.setPosition(GameScene.WIDTH / 2 + distance - right.getSize().getX() / 2, y);
    
    gate = new Gate(this);
    gate.setPosition(GameScene.WIDTH/2 - gate.getSize().getX()/2, GameScene.HEIGHT - gate.getSize().getY());
    
    addEnemyEntity(left);
    addEnemyEntity(right);
    addGameObject(gate);
    addGameObject(new Background(this));
  }
  
  private void addBackground() {
    
  }
  
  private void addFloor() {
    int y = (int) (GameScene.HEIGHT - 20);
    for (int x = 0; x < GameScene.WIDTH; x += 20) {
      FloorTile ins = new FloorTile(this);
      ins.setPosition(x, y);
      addGameObject(ins);
    }
  }
  
  private void addEnemyEntity(GameObject entity) {
    addGameObject(entity);
    enemyEntities.add(entity);
  }
  
  private long lastDone = 0;
  private boolean started = false;
  
  @Override
  public void performGameLogic(RenderProperties properties) {
    if(!started && gate.collides(player) && observer.isPressing(binding.getBinding(KeyBinding.INTERACT))) {
      System.out.println("HEYOOO");
      started = true;
      player.setSpawn(true);
      player.setReverseSpawn(true);
    }
    
    if(started && !player.isSpawn()) {
      this.stop();
      onTransition.run();
    }
    
    for (GameObject enemy : enemyEntities) {
      if (enemy instanceof Collidable) {
        if (player.collides(enemy)) {
          player.receiveCollision(enemy);
        }
        if (player.isAttacking() && playerSwing.collides(enemy)) {
          ((Collidable) enemy).receiveCollision(player);
          player.setCloudStep(true);
          if (lastDone != player.getAttackCount()) {
            Platform.runLater(() -> {
              AudioFactory.createSfxHandler(manager.findAudio("sfx_swordhit_" + Utility.random(1, 3)))
                  .playThenDestroy();
              lastDone = player.getAttackCount();
            });
          }
        }
        if (player.isAttacking() && playerGlideSwing.collides(enemy)) {
          ((Collidable) enemy).receiveCollision(player);
          player.setCloudStep(true);
          player.setGlideHit(true);
          if (lastDone != player.getAttackCount()) {
            Platform.runLater(() -> {
              AudioFactory.createSfxHandler(manager.findAudio("sfx_swordhit_" + Utility.random(1, 3)))
                  .playThenDestroy();
              lastDone = player.getAttackCount();
            });
          }
        }
      }
    }
  }
}
