package app.main.game.object.boss;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.audio.AudioHandler;
import app.main.controller.scene.SceneController;
import app.main.game.object.Hittable;
import app.main.game.object.boss.state.BossIdleState;
import app.main.game.object.boss.state.levitate.BossGroundFire;
import app.main.game.object.other.DemonHiveController;
import app.main.game.object.player.Player;
import app.main.game.object.player.shuriken.Shuriken;
import app.utility.Utility;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.ObjectTag;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Boss extends GameObject implements Updatable, Collidable, Hittable{
  
  private static Boss instance;
  private boolean dead;
  private Vector2 velocity;
  private Image bossDead;
  private long startDeadFrame = -1;

  public static Boss getInstance(GameScene owner) {
    if(instance == null) {
      instance = new Boss(owner);
    }
    return instance;
  }

  public Boss(GameScene owner) {
    super(owner);
    setTag(ObjectTag.Enemy);
    gameController = GameController.getInstance();
    setSize(35, 60);
    fire = BossGroundFire.getInstance(owner);
    demonHiveController = new DemonHiveController(getOwner());
    sword1 = new BossSword(owner);
    sword2 = new BossSword(owner);
    ui = new BossUI(owner, this);
    velocity = Vector2.ZERO();
    AssetManager assets = AssetManager.getInstance();
    bossDead = assets.findImage("boss_dead");
    explosion = assets.findImage("boss_explosion");
    reset(owner);
  }
  
  public void reset(GameScene owner) {
    setOwner(owner);
    startDeadFrame = -1;
    ui.reset(owner);
    getOwner().addGameObject(ui);
    state = BossIdleState.load(this);
    dead = false;
    jumped = false;
    velocity = Vector2.ZERO();
    playerAttackNo = -1;
    facing = Vector2.RIGHT();
    demonHiveController.reset(owner);
    sword1.reset(owner);
    sword2.reset(owner);
    first = true;
    fire.reset(owner);
    hp = 100;
  }

  private int hp = 100;
  private Vector2 facing;
  private boolean first = true;

  private int playerAttackNo = -1;

  private BossUI ui;
  private BossState state;
  private DemonHiveController demonHiveController;
  private GameController gameController;
  private BossSword sword1;
  private BossSword sword2;
  private BossGroundFire fire;
  boolean jumped = false;
  private Image explosion;
  private Image diedSprite;
  
  public boolean isFirst() {
    return first;
  }

  public void setFirst(boolean first) {
    this.first = first;
  }

  public BossSword getSword1() {
    return sword1;
  }

  public BossSword getSword2() {
    return sword2;
  }

  public BossState getState() {
    return state;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public Vector2 getFacing() {
    return facing;
  }

  public void setFacing(Vector2 facing) {
    this.facing = facing;
  }

  public int getPlayerAttackNo() {
    return playerAttackNo;
  }

  public void setPlayerAttackNo(int playerAttackNo) {
    this.playerAttackNo = playerAttackNo;
  }

  public void setState(BossState state) {
    this.state = state;
  }

  public void activateFire() {
    fire.reset(getOwner());
    fire.activate();
  }
  public void deactivateFire() {
    fire.deactivate();
  }
  public BossGroundFire getFire() {
    return fire;
  }

  private Vector2 startDeadPos = Vector2.ZERO();
  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    if(gameController.isHitbox() && !dead) {
      context.setFill(Color.RED);
      Vector2 position = getPosition();
      Vector2 size = getSize();
      context.fillRect(position.getX(), position.getY(), size.getX(), size.getY());
    }

    if(dead) {
      // TODO: Render the custom sprite instead.
      if(startDeadFrame == -1) {
        startDeadPos = getPosition().copy();
        startDeadFrame = properties.getFrameCount();
      }
      Vector2 renderPos = Vector2.renderBottomCenter(getPosition(), getSize(), new Vector2(120, 120));
      context.drawImage(bossDead, renderPos.getX(), renderPos.getY());

      renderPos = Vector2.renderCenter(startDeadPos, getSize(), new Vector2(100, 100));
      int index = (int) ((properties.getFrameCount() - startDeadFrame) / 10);
      if(index < 8) {
        int posX = index % 4 * 50;
        int posY = index / 4 * 50;
        context.drawImage(explosion, posX, posY, 50, 50, renderPos.getX(), renderPos.getY(), 100, 100);
      }

      return;
    }

    state.render(properties);
  }

  @Override
  public void receiveCollision(GameObject object) {
    boolean receiveDamage = false;
    
    if(object instanceof Shuriken) {
      receiveDamage();
    }
    if(object instanceof Player) {
      if(playerAttackNo == -1) {
        playerAttackNo = ((Player) object).getAttackCount();
        receiveDamage = true;
      }
      else {
        receiveDamage = ((Player) object).getAttackCount() != playerAttackNo;
      }
    }
    if(receiveDamage) {
      playerAttackNo = ((Player) object).getAttackCount();
      receiveDamage();
    }
  }
  
  private void receiveDamage() {
    hp--;
  }

  @Override
  public void update(RenderProperties properties) {
    if(dead && !jumped) {
      // TODO: Apply custom update instead
      jumped = true;
      velocity.setY(-220);
      velocity.setX(Utility.random(25, 50));
      return;
    }
    state.update(properties);
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    if(dead) {
      // TODO: Apply custom update instead
      double velocityY = velocity.getY();
      velocity.setY(velocityY + 500 * properties.getFixedDeltaTime());
      getPosition().setAdd(velocity.mult(properties.getFixedDeltaTime()));
      return;
    }
    state.fixedUpdate(properties);
  }

  public DemonHiveController getDemonHiveController() {
    return demonHiveController;
  }

  public boolean isDead() {
    return dead;
  }
  public void setDead() {
    if(!dead) {
      dead = true;
      getOwner().setDead(true);
      getOwner().setException(this);
      AudioFactory.createSfxHandler(AssetManager.getInstance().findAudio("sfx_boss_defeat")).playThenDestroy();
    }
  }
}
