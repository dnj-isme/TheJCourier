package app.main.game.object.boss;

import app.main.controller.GameController;
import app.main.game.object.Hittable;
import app.main.game.object.boss.state.BossIdleState;
import app.main.game.object.other.DemonHiveController;
import app.main.game.object.player.Player;
import app.main.game.object.player.shuriken.Shuriken;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.ObjectTag;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Boss extends GameObject implements Updatable, Collidable, Hittable{
  
  private static Boss instance;
  public static Boss getInstance(GameScene owner) {
    if(instance == null) {
      instance = new Boss(owner);
    }
    return instance;
  }
  
  public void reset(GameScene owner) {
    setOwner(owner);
    state = BossIdleState.load(this);
    hp = 60;
    playerAttackNo = -1;
    facing = Vector2.RIGHT();
    demonHiveController = new DemonHiveController(getOwner());
    sword1 = new BossSword(owner);
    sword2 = new BossSword(owner);
    first = true;
  }

  private int hp = 60;
  private Vector2 facing;
  private boolean first = true;
  
  private int playerAttackNo = -1;
  
  private BossState state;
  private DemonHiveController demonHiveController;
  private GameController gameController;
  private BossSword sword1;
  private BossSword sword2;
  
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

  public Boss(GameScene owner) {
    super(owner);
    setLayer(ObjectLayer.Block);
    setTag(ObjectTag.Enemy);
    gameController = GameController.getInstance();
    setSize(35, 60);
    reset(owner);
  }
  
  @Override
  public void render(RenderProperties properties) {
    if(gameController.isHitbox()) {
      GraphicsContext context = properties.getContext();
      context.setFill(Color.RED);
      Vector2 position = getPosition();
      Vector2 size = getSize();
      context.fillRect(position.getX(), position.getY(), size.getX(), size.getY());
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
    System.out.println("The boss receives damage from Player");
  }

  @Override
  public void update(RenderProperties properties) {
    state.update(properties);
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    state.fixedUpdate(properties);
  }

  public DemonHiveController getDemonHiveController() {
    return demonHiveController;
  }
}
