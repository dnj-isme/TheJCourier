package app.main.game.object.player;

import app.main.controller.KeyBinding;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.state.PlayerFallState;
import app.main.game.object.player.state.PlayerIdleState;
import app.main.game.object.player.state.PlayerJumpState;
import app.main.game.object.player.state.PlayerWalkState;
import app.utility.Utility;
import app.utility.canvas.Drawable;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import app.utility.canvas.Vector2;

public abstract class PlayerState implements Drawable, Updatable {  
  private Player player;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  
  public PlayerState(Player player) {
    super();
    this.player = player;
    eventObserver = player.getEventObserver();
    keyBinding = player.getKeyBinding();
  }
  
  protected Player getPlayer() {
    return player;
  }
  
  protected Vector2 updatePlayerDirection() {
    Vector2 direction = Vector2.ZERO();
    if(eventObserver.isPressing(keyBinding.getBinding(KeyBinding.RIGHT))) {
      direction = Vector2.RIGHT();
    }
    else if (eventObserver.isPressing(keyBinding.getBinding(KeyBinding.LEFT))) {
      direction = Vector2.LEFT();
    }
    
    if(!direction.equals(Vector2.ZERO())) {
      player.setFacing(direction);      
    }
    return direction;
  }
  
  protected void hanldeHorizontalMovement() {
    Vector2 direction = updatePlayerDirection();
    double movement = direction.getX() * player.getMoveSpeed();
    player.getVelocity().setX(movement);
  }
  
  protected void handleGravity(double deltaTime, double gravity) {
    double velocityY = player.getVelocity().getY();
    if(!player.isTouchingGround() || velocityY < 0) {
      player.getVelocity().setY(velocityY + gravity * deltaTime);
    }
    else {
      player.getVelocity().setY(0);
    }
  }
  
  protected void handleGravity(double deltaTime, double gravity, double maxFallSpeed) {
    double velocityY = player.getVelocity().getY();
    if(!player.isTouchingGround() || velocityY < 0) {
      player.getVelocity().setY(Math.min(velocityY + gravity * deltaTime, maxFallSpeed));
    }
    else {
      player.getVelocity().setY(0);
    }
  }
  
  protected void detectGlide() {
    if(!player.isTouchingGround() && player.isReleaseJump() && 
        eventObserver.isPressing(keyBinding.getBinding(KeyBinding.JUMP))) {
      player.setGlide(true);
      player.setReleaseGlide(false);
    }
    else {
      player.setGlide(false);
    }
  }
}
