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
  
  public Player getPlayer() {
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
  
  @Override
  public void update(RenderProperties properties) {
//    System.out.println(player.getCurrentState());
    switch(player.getCurrentState()) {
    case Attack:
      break;
    case AttackDuck:
      break;
    case AttackGlide:
      break;
    case AirborneJump:
      break;
    case AttackWalk:
      break;
    case Duck:
      break;
    case Fall:
      player.setState(PlayerFallState.load(player));
      break;
    case Glide:
      break;
    case Jump:
      player.setState(PlayerJumpState.load(player));
      break;
    case Walk:
      player.setState(PlayerWalkState.load(player));
      break;
    case Idle: default:
      player.setState(PlayerIdleState.load(player));
      break;
    }
  }
  
  public void hanldeHorizontalMovement() {
    Vector2 direction = updatePlayerDirection();
    double movement = direction.getX() * player.getMoveSpeed();
    player.getVelocity().setX(movement);
  }
  

  public void handleGravity(double deltaTime, double gravity) {
    if(!player.isTouchingGround()) {
      player.getVelocity().setY(player.getVelocity().getY() + gravity * deltaTime);
    }
    else {
      player.getVelocity().setY(0);
    }
  }
  
  public void handleGravity(double deltaTime, double gravity, double maxFallSpeed) {
    if(!player.isTouchingGround()) {
      player.getVelocity().setY(Math.min(player.getVelocity().getY() + gravity * deltaTime, maxFallSpeed));
    }
    else {
      player.getVelocity().setY(0);
    }
  }
  
  
  @Override
  public void fixedUpdate(RenderProperties properties) {
    Vector2 movement = player.getVelocity().mult(properties.getFixedDeltaTime());
    Vector2 pos = player.getPosition();
    double x = Utility.range(pos.getX() + movement.getX(), 0, GameScene.WIDTH);
    double y = Utility.range(pos.getY() + movement.getY(), 0, GameScene.HEIGHT);
    x = Utility.range(x, 0, GameScene.WIDTH - player.getSize().getX());
    y = Utility.range(y, 0, GameScene.HEIGHT - player.getSize().getY());
    player.setPosition(x, y);
  }
}
