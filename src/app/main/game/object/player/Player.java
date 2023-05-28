package app.main.game.object.player;

import app.main.controller.GameController;
import app.main.controller.KeyBinding;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.state.PlayerIdleState;
import app.utility.Utility;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Player extends GameObject implements Updatable, Collidable {

  private GameController gameController;
  private PlayerState state;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;

  // Player stats
  private int health;
  private int shuriken;
  private boolean glide;
  
  // Player Movement
  private boolean cloudStep;
  private boolean duck;

  // Movement constants
  private double gravity;
  private double maxFallSpeed;
  private double moveSpeed;
  private double jumpForce;

  // Movement calculations
  private Vector2 facing;
  private Vector2 velocity;

  public Player(GameScene owner) {
    super(owner);
    super.setSize(new Vector2(25, 40));
    this.health = 8;
    this.shuriken = 4;
    this.glide = false;
    this.cloudStep = false;

    gameController = GameController.getInstance();
    eventObserver = SceneEventObserver.getInstance();
    keyBinding = KeyBinding.getIntance();
    facing = Vector2.RIGHT();
    velocity = Vector2.ZERO();
    moveSpeed = 400;
    jumpForce = 750;
    gravity = 3000;
    state = PlayerIdleState.load(this);
  }

  public double getJumpForce() {
    return jumpForce;
  }

  public double getGravity() {
    return gravity;
  }

  public boolean canCloudStep() {
    return cloudStep;
  }

  public void setCloudStep(boolean cloudStep) {
    this.cloudStep = cloudStep;
  }

  public double getMoveSpeed() {
    return moveSpeed;
  }

  public void setMoveSpeed(double moveSpeed) {
    this.moveSpeed = moveSpeed;
  }

  public Vector2 getVelocity() {
    return velocity;
  }

  public void setVelocity(Vector2 velocity) {
    this.velocity = velocity;
  }

  public void setVelocity(double x, double y) {
    velocity.setX(x);
    velocity.setY(y);
  }

  public GameController getGameController() {
    return gameController;
  }

  public SceneEventObserver getEventObserver() {
    return eventObserver;
  }

  public KeyBinding getKeyBinding() {
    return keyBinding;
  }

  public int getHealth() {
    return health;
  }

  public int getShuriken() {
    return shuriken;
  }

  @Override
  public void setSize(double x, double y) {
    Utility.debug("The dev said NOPE!!");
  }

  public void setState(PlayerState state) {
    this.state = state;
  }

  public Vector2 getFacing() {
    return facing;
  }

  public void setFacing(Vector2 facing) {
    this.facing = facing;
  }

  @Override
  public void receiveCollision(GameObject object) {
    // TODO Auto-generated method stub

  }

  @Override
  public void update(RenderProperties properties) {
    state.update(properties);
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    state.fixedUpdate(properties);
  }

  public boolean isTouchingGround() {
    return getPosition().getY() >= GameScene.HEIGHT - getSize().getY();
  }

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    if (gameController.isHitbox()) {
      context.setFill(Color.RED);
      context.fillRect(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
    }
    state.render(properties);
  }

  public boolean isGliding() {
    return glide;
  }

  public PlayerInputState getCurrentState() {
    SceneEventObserver observer = SceneEventObserver.getInstance();
    KeyBinding binding = KeyBinding.getIntance();

    KeyCode attackBinding = binding.getBinding(KeyBinding.ATTACK);
    KeyCode leftBinding = binding.getBinding(KeyBinding.LEFT);
    KeyCode rightBinding = binding.getBinding(KeyBinding.RIGHT);
    KeyCode jumpBinding = binding.getBinding(KeyBinding.JUMP);
    KeyCode duckBinding = binding.getBinding(KeyBinding.DUCK);

    if (!isTouchingGround()) {
      double vertical = velocity.getY();
      if (vertical >= 0) {
        return PlayerInputState.Fall;
      } else {
        if (observer.isPressing(leftBinding) || observer.isPressing(rightBinding)) {
          return PlayerInputState.Fall; // Keep in Fall state while still falling and moving horizontally
        } else {
          return PlayerInputState.Jump;
        }
      }
    }

    if (observer.isPressing(jumpBinding)) {
      return PlayerInputState.Jump;
    }

    if (observer.isPressing(leftBinding) || observer.isPressing(rightBinding)) {
      return PlayerInputState.Walk;
    }

    return PlayerInputState.Idle;
  }
}
