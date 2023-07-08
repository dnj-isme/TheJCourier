package app.main.game.scene;

import java.util.TimerTask;
import java.util.Vector;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.Hittable;
import app.main.game.object.boss.Boss;
import app.main.game.object.boss.BossSword;
import app.main.game.object.boss.state.levitate.BossGroundFire;
import app.main.game.object.other.*;
import app.main.game.object.other.DemonHiveController.HiveTag;
import app.main.game.object.player.Player;
import app.main.game.object.player.shuriken.Shuriken;
import app.main.game.object.player.shuriken.ShurikenPool;
import app.main.game.object.player.swing.PlayerGlideSwing;
import app.main.game.object.player.swing.PlayerSwing;
import app.utility.Utility;
import app.utility.canvas.*;
import javafx.application.Platform;

public class BossRoom extends GameScene {

  private Player player;
  private PlayerSwing playerSwing;
  private PlayerGlideSwing playerGlideSwing;
  private ShurikenPool pool;
  private AssetManager manager;
  private SceneEventObserver observer;
  private KeyBinding binding;
  private YouWinUI youWinUI;
  
  private Boss boss;
  private BossSword sword1, sword2;
  
  private Vector<GameObject> enemyEntities;
  
  private Runnable onTransition;
  private DemonHiveController hive;
  private BossGroundFire fire;
  private boolean youWinScreen = false;
  private Runnable onWin;
  private Runnable onStop;

  public void setOnWin(Runnable onWin) {
    this.onWin = onWin;
  }

  public void setOnTransition(Runnable onTransition) {
    this.onTransition = onTransition;
  }

  @Override
  protected void initializeGameObjects() {
    enemyEntities = new Vector<>();
    manager = AssetManager.getInstance();
    observer = SceneEventObserver.getInstance();
    binding = KeyBinding.getIntance();

    addGameObject(player = Player.getInstance(this));
    player.reset(this);
    player.setPosition(50, 50);
    player.setSpawn(true);
    player.setReverseSpawn(false);
    player.setFreezeInput(true);
    Utility.delayAction(2000, new TimerTask() {
      
      @Override
      public void run() {
        player.setFreezeInput(false);
      }
    });

    playerSwing = player.getPlayerSwing();
    playerGlideSwing = player.getPlayerGlideSwing();
    pool = player.getPool();
    player.setFloorHeight(20);
    player.setPosition(50, GameScene.HEIGHT - 20 - player.getSize().getY());
    player.restoreShuriken();
    addGameObject(new YouWinUI(this));
    boss = Boss.getInstance(this);
    hive = boss.getDemonHiveController();
    double centerX = (GameScene.WIDTH - 40) / 2;
    double centerY = ((GameScene.HEIGHT - 20) / 2);
    addEnemyEntity(boss);
    boss.reset(this);
    boss.setPosition(
        GameScene.WIDTH - boss.getSize().getX() - 100,
        GameScene.HEIGHT - boss.getSize().getY() - 20);
    boss.setFacing(Vector2.LEFT());
    boss.setLayer(ObjectLayer.Player);
    addEnemyEntity(sword1 = boss.getSword1());
    addEnemyEntity(sword2 = boss.getSword2());
    addEnemyEntity(fire = boss.getFire());
    
    int diff = 60;
    hive.add(new Vector2(centerX, centerY), HiveTag.E);
    hive.add(new Vector2(centerX + diff * 1, centerY + 50), HiveTag.D);
    hive.add(new Vector2(centerX + diff * 1, centerY - 50), HiveTag.D);
    hive.add(new Vector2(centerX - diff * 1, centerY + 50), HiveTag.D);
    hive.add(new Vector2(centerX - diff * 1, centerY - 50), HiveTag.D);
    hive.add(new Vector2(centerX - diff * 2, centerY), HiveTag.C);
    hive.add(new Vector2(centerX + diff * 2, centerY), HiveTag.C);
    hive.add(new Vector2(centerX + diff * 3, centerY + 50), HiveTag.B);
    hive.add(new Vector2(centerX + diff * 3, centerY - 50), HiveTag.B);
    hive.add(new Vector2(centerX - diff * 3, centerY + 50), HiveTag.B);
    hive.add(new Vector2(centerX - diff * 3, centerY - 50), HiveTag.B);
    hive.add(new Vector2(centerX - diff * 4, centerY), HiveTag.A);
    hive.add(new Vector2(centerX + diff * 4, centerY), HiveTag.A);
    
    enemyEntities.addAll(hive.getHives());
    addEnemyEntity(boss.getSword1());
    addFloor();
    addForeground();
    addBackground();
    addGameObject(new Background(this));
    addGameObject(youWinUI = new YouWinUI(this));

    double yFront = GameScene.HEIGHT - 70;
    double yStatue1 = GameScene.HEIGHT - 140;
    double yStatue2 = GameScene.HEIGHT - 120;

    addGameObject(new Foreground1(this, 40, yFront));
    addGameObject(new Foreground1(this, 120, yFront));
    addGameObject(new Foreground2(this, 500, yFront));
    addGameObject(new Foreground2(this, 120, yFront));
    addGameObject(new Foreground2(this, 350, yFront));
    addGameObject(new Throne(this, (GameScene.WIDTH - 120) / 2, yStatue1));
    addGameObject(new Statue2(this, (GameScene.WIDTH - 80) / 2 + 120, yStatue2, false));
    addGameObject(new Statue2(this, (GameScene.WIDTH - 80) / 2 - 120, yStatue2, true));
    addGameObject(new Statue1(this, (GameScene.WIDTH - 100) / 2 + 250, yStatue1, false));
    addGameObject(new Statue1(this, (GameScene.WIDTH - 100) / 2 - 250, yStatue1, true));
  }

  private void addForeground() {
    
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
    if(boss.getHp() <= 0) {
      boss.setDead();
      if (!youWinScreen) {
        youWinScreen = true;
        if(onStop != null) {
          onStop.run();
        }
        Utility.delayAction(5000, new TimerTask() {
          @Override
          public void run() {
            youWinUI.show();
            if(onWin != null) {
              onWin.run();
            }
          }
        });
      }
    }

    for (GameObject enemy : enemyEntities) {
      if (enemy instanceof Collidable) {
        if (player.collides(enemy) && !player.isInvincible() && !(enemy instanceof BossGroundFire && !((BossGroundFire) enemy).isActive())) {
          player.receiveCollision(enemy);
          if(!player.isAlive()) {
            player.setDead();
          }
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

  public void setOnStop(Runnable onStop) {
    this.onStop = onStop;
  }
}
