package app.main.game.object.boss;

import app.main.game.object.boss.state.BossIdleState;
import app.main.game.object.player.Player;
import app.main.game.object.player.shuriken.Shuriken;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.ObjectTag;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;

public class Boss extends GameObject implements Updatable, Collidable{
  
  private static Boss instance;
  public static Boss getInstance(GameScene owner) {
    if(instance == null) {
      instance = new Boss(owner);
    }
    return instance;
  }
  
  public void reset(GameScene owner) {
    setOwner(owner);
    setState(BossIdleState.load(this));
  }

  private int hp = 60;
  
  private int playerAttackNo = -1;
  
  private BossState state;
  
  public BossState getState() {
    return state;
  }

  public void setState(BossState state) {
    this.state = state;
  }

  public Boss(GameScene owner) {
    super(owner);
    setLayer(ObjectLayer.Block);
    setTag(ObjectTag.Enemy);
  }

  @Override
  public void render(RenderProperties properties) {
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
}
