package app.main.game.scene;

import java.util.Vector;

import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.game.object.Hittable;
import app.main.game.object.boss.BossPlaceholder;
import app.main.game.object.boss.BossSword;
import app.main.game.object.other.DemonHiveController;
import app.main.game.object.other.DemonHiveController.HiveTag;
import app.main.game.object.other.Lantern;
import app.main.game.object.player.Player;
import app.main.game.object.player.shuriken.Shuriken;
import app.main.game.object.player.shuriken.ShurikenPool;
import app.main.game.object.player.swing.PlayerGlideSwing;
import app.main.game.object.player.swing.PlayerSwing;
import app.utility.Utility;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectTag;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.application.Platform;

public class DeveloperRoom extends GameScene {

  public DeveloperRoom() {
    super();
    setBorder(3);
  }

  private Player player;
  private PlayerSwing playerSwing;
  private PlayerGlideSwing playerGlideSwing;
  private ShurikenPool pool;
  private AssetManager manager;

  private BossPlaceholder placeholder;

  private Lantern lantern1;
  private Lantern lantern2;

  private Vector<GameObject> enemyEntities;
  private DemonHiveController demonHiveController;

  public Player getPlayer() {
    return player;
  }

  @Override
  protected void initializeGameObjects() {
    enemyEntities = new Vector<GameObject>();
    demonHiveController = new DemonHiveController(this);
    manager = AssetManager.getInstance();

    addGameObject(player = Player.getInstance(this));
    player.reset(this);
    player.setPosition(50, 50);

    playerSwing = player.getPlayerSwing();
    playerGlideSwing = player.getPlayerGlideSwing();
    pool = player.getPool();
    
    System.out.println(pool == null);

    addEnemyEntity(placeholder = new BossPlaceholder(this));
    placeholder.setPosition(200, GameScene.HEIGHT - placeholder.getSize().getY());

    addEnemyEntity(lantern1 = new Lantern(this));
    addEnemyEntity(lantern2 = new Lantern(this));
    
    BossSword sword = new BossSword(this);
    sword.setPosition(200, 100);
    sword.setDestination(500, 120);
    sword.startAnimation();
    addEnemyEntity(sword);

    lantern1.setPosition(100, GameScene.HEIGHT - placeholder.getSize().getY() - 50);
    lantern2.setPosition(300, GameScene.HEIGHT - placeholder.getSize().getY() - 100);

    demonHiveController.add(new Vector2(500, 300), HiveTag.A);
    demonHiveController.add(new Vector2(500, 200), HiveTag.B);
    demonHiveController.add(new Vector2(600, 300), HiveTag.C);
    demonHiveController.add(new Vector2(600, 200), HiveTag.B);

    enemyEntities.addAll(demonHiveController.getHives());
  }

  private void addEnemyEntity(GameObject object) {
    addGameObject(object);
    enemyEntities.add(object);
  }

  private long lastDone = 0;

  @Override
  public void performGameLogic(RenderProperties properties) {
    if(!player.isAlive()) {
      deadTrigger();
      setException(player);
    }
    
    for (GameObject enemy : enemyEntities) {
      if (enemy instanceof Collidable) {
        if (player.collides(enemy) && !player.isInvincible()) {
          player.receiveCollision(enemy);
        }
        if (player.isAttacking() && playerSwing.collides(enemy) && enemy instanceof Hittable) {
          ((Collidable) enemy).receiveCollision(player);
          player.setCloudStep(true);
          if (lastDone != player.getAttackCount()) {
            Platform.runLater(() -> {
              AudioFactory.createSfxHandler(manager.findAudio("sfx_swordhit_" + Utility.random(1, 3)))
                  .playThenDestroy();
            });
            lastDone = player.getAttackCount();
          }
        }
        if (player.isAttacking() && playerGlideSwing.collides(enemy) && enemy instanceof Hittable) {
          ((Collidable) enemy).receiveCollision(player);
          player.setCloudStep(true);
          player.setGlideHit(true);
          if (lastDone != player.getAttackCount()) {
            Platform.runLater(() -> {
              AudioFactory.createSfxHandler(manager.findAudio("sfx_swordhit_" + Utility.random(1, 3)))
                  .playThenDestroy();
            });
            lastDone = player.getAttackCount();
          }
        }
        for (Shuriken shuriken : pool.getAll()) {
          if (shuriken.isEnabled() && enemy.getTag() == ObjectTag.Enemy && shuriken.collides(enemy) && enemy instanceof Hittable) {
            ((Collidable) enemy).receiveCollision(shuriken);
            Platform.runLater(() -> {
              AudioFactory.createSfxHandler(manager.findAudio("sfx_shuriken_stick")).playThenDestroy();
            });
            shuriken.stop();
          }
        }
      }
    }
  }

  private void deadTrigger() {
    setDead(true);
  }
}
