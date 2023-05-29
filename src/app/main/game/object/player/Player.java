package app.main.game.object.player;

import app.main.controller.GameController;
import app.main.controller.KeyBinding;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.state.PlayerAttackDuckState;
import app.main.game.object.player.state.PlayerAttackGlideState;
import app.main.game.object.player.state.PlayerAttackState;
import app.main.game.object.player.state.PlayerAttackWalkState;
import app.main.game.object.player.state.PlayerDuckState;
import app.main.game.object.player.state.PlayerFallState;
import app.main.game.object.player.state.PlayerGlideState;
import app.main.game.object.player.state.PlayerIdleState;
import app.main.game.object.player.state.PlayerJumpState;
import app.main.game.object.player.state.PlayerWalkState;
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
  private int health = 8;
  private int shuriken = 4;
  private boolean glide = false;
  private int attackCount = 0;

  // Player Movements
  private boolean cloudStep = false;
  private boolean jump = false;
  private boolean duck = false;
  private boolean attack = false;

  // Related to input
  private boolean releaseJump = true;
  private boolean releaseAttack = true;
  private boolean releaseGlide = true;

  // Movement constants
  private double gravity = 3000;
  private double maxGlideFall = 160;
  private double moveSpeed = 400;
  private double jumpForce = 750;

  // Movement calculations
  private Vector2 facing = Vector2.RIGHT();
  private Vector2 velocity = Vector2.ZERO();

  public static Vector2 SIZE = new Vector2(24, 40);

  public void reset() {
    health = 8;
    shuriken = 4;
    glide = false;
    cloudStep = false;
    jump = false;
    duck = false;
    releaseJump = true;
    releaseAttack = true;
    releaseGlide = true;
    facing = Vector2.RIGHT();
    velocity = Vector2.ZERO();
    attackCount = 0;
  }

  public Player(GameScene owner) {
    super(owner);
    gameController = GameController.getInstance();
    eventObserver = SceneEventObserver.getInstance();
    keyBinding = KeyBinding.getIntance();
    state = PlayerIdleState.load(this);
    super.setSize(SIZE);
  }

  public int getAttackCount() {
    return attackCount;
  }

  public void addAttackCount() {
    attackCount++;
  }

  public boolean isAttack() {
    return attack;
  }

  public void setAttack(boolean attack) {
    this.attack = attack;
  }

  public void setGlide(boolean glide) {
    this.glide = glide;
  }

  public boolean isReleaseJump() {
    return releaseJump;
  }

  public void setReleaseJump(boolean releaseJump) {
    this.releaseJump = releaseJump;
  }

  public boolean isReleaseAttack() {
    return releaseAttack;
  }

  public void setReleaseAttack(boolean releastAttack) {
    this.releaseAttack = releastAttack;
  }

  public boolean isReleaseGlide() {
    return releaseGlide;
  }

  public void setReleaseGlide(boolean releaseGlide) {
    this.releaseGlide = releaseGlide;
  }

  public double getGlideFall() {
    return maxGlideFall;
  }

  public boolean isDucking() {
    return duck;
  }

  public void setDuck(boolean duck) {
    this.duck = duck;
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

  public void setJump(boolean jump) {
    this.jump = jump;
  }

  public boolean isJumping() {
    return jump;
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

  public void setState(PlayerState from, PlayerState to) {
    this.state = to;
  }

  public Vector2 getFacing() {
    return facing;
  }

  public void setFacing(Vector2 facing) {
    this.facing = facing;
  }

  public boolean isTouchingGround() {
    return getPosition().getY() >= GameScene.HEIGHT - getSize().getY();
  }

  public boolean isGliding() {
    return glide;
  }

  public Vector2 getWeaponSwingPosition(Vector2 swingSize) {
    double x = getPosition().getX();
    double y = getPosition().getY();

    if (facing.getX() < 0) {
      x -= swingSize.getX();
    } else {
      x += getSize().getX();
    }

    if (duck) {
      y += getSize().getY() * 0.5;
    }

    return new Vector2(x, y);
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
      if (glide) {
        return PlayerInputState.Glide;
      }

      return velocity.getY() >= 0 ? PlayerInputState.Fall : PlayerInputState.Jump;
    }

    if (observer.isPressing(jumpBinding) && releaseJump && releaseGlide) {
      return PlayerInputState.Jump;
    }

    if (observer.isPressing(duckBinding)) {
      return PlayerInputState.Duck;
    }

    if (observer.isPressing(leftBinding) || observer.isPressing(rightBinding)) {
      return PlayerInputState.Walk;
    }

    if (observer.isPressing(attackBinding) && releaseAttack || attack) {
      return PlayerInputState.Attack;
    }

    return PlayerInputState.Idle;
  }

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    if (gameController.isHitbox()) {
      context.setFill(Color.GREEN);
      context.fillRect(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
    }
    state.render(properties);
  }

  @Override
  public void receiveCollision(GameObject object) {
    System.out.println("Receive Collision from " + object.getClass().toString());
  }

  @Override
  public void update(RenderProperties properties) {
    if (jump && !eventObserver.isPressing(keyBinding.getBinding(KeyBinding.JUMP))) {
      jump = false;
      cloudStep = false;
      releaseJump = true;
    }

    if (!eventObserver.isPressing(keyBinding.getBinding(KeyBinding.JUMP)) && (!isTouchingGround() || glide)) {
      releaseGlide = true;
    }

    if (!eventObserver.isPressing(keyBinding.getBinding(KeyBinding.DUCK))) {
      duck = false;
      PlayerDuckState.load(this).reset();
    }

    if (!eventObserver.isPressing(keyBinding.getBinding(KeyBinding.ATTACK))) {
      releaseAttack = true;
    }

    Utility.cls();
    System.out.println("Attack  = " + attack);
    System.out.println("Count   = " + attackCount);
    System.out.println("Release = " + releaseAttack);

    PlayerState to = PlayerIdleState.load(this);

    switch (getCurrentState()) {
    case Attack:
      to = PlayerAttackState.load(this);
      break;
    case AttackDuck:
      to = PlayerAttackDuckState.load(this);
      break;
    case AttackGlide:
      to = PlayerAttackGlideState.load(this);
      break;
    case AttackWalk:
      to = PlayerAttackWalkState.load(this);
      break;
    case Duck:
      to = PlayerDuckState.load(this);
      break;
    case Fall:
      to = PlayerFallState.load(this);
      break;
    case Glide:
      to = PlayerGlideState.load(this);
      break;
    case Jump:
      to = PlayerJumpState.load(this);
      break;
    case Walk:
      to = PlayerWalkState.load(this);
      break;
    case Idle:
    default:
      if (velocity.getX() != 0) {
        velocity.setX(0);
      }
      break;
    }
    handlePositioning(state, to);
    state = to;
    state.update(properties);
  }

  private void handlePositioning(PlayerState from, PlayerState to) {
    PlayerSize fromSize = PlayerSize.getSize(from);
    PlayerSize toSize = PlayerSize.getSize(to);

    if (fromSize != toSize) {
      switch (toSize) {
      case TOP:
        if (fromSize == PlayerSize.FULL) {
          topFromFull();
        } else if (fromSize == PlayerSize.BOTTOM) {
          topFromBottom();
        }
        break;

      case BOTTOM:
        if (fromSize == PlayerSize.FULL) {
          bottomFromFull();
        } else if (fromSize == PlayerSize.TOP) {
          bottomFromTop();
        }
        break;

      case FULL:
        if (fromSize == PlayerSize.TOP) {
          fullFromTop();
        } else if (fromSize == PlayerSize.BOTTOM) {
          fullFromBottom();
        }
        break;
      }
    }
  }

  private void topFromBottom() {
    double oldHeight = getSize().getY();
    setSize(SIZE.getX(), Player.SIZE.getY() / 2);
    double newHeight = getSize().getY();
    getPosition().addY(newHeight - oldHeight);
  }

  private void bottomFromTop() {
    double oldHeight = getSize().getY();
    setSize(SIZE.getX(), Player.SIZE.getY() / 2);
    double newHeight = getSize().getY();
    getPosition().addY(newHeight - oldHeight); // move the player up.
  }

  private void fullFromTop() {
    double oldHeight = getSize().getY();
    setSize(SIZE);
    double newHeight = getSize().getY();
    getPosition().addY(oldHeight - newHeight);
  }

  private void fullFromBottom() {
    double oldHeight = getSize().getY();
    setSize(SIZE);
    double newHeight = getSize().getY();
    getPosition().addY(oldHeight - newHeight);
  }

  private void topFromFull() {
    setSize(SIZE.getX(), Player.SIZE.getY() / 2);
  }

  private void bottomFromFull() {
    double oldHeight = getSize().getY();
    setSize(SIZE.getX(), SIZE.getY() / 2);
    double newHeight = getSize().getY();
    getPosition().addY(oldHeight - newHeight);
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    state.fixedUpdate(properties);
    Vector2 movement = getVelocity().mult(properties.getFixedDeltaTime());
    Vector2 pos = getPosition();
    double x = Utility.range(pos.getX() + movement.getX(), 0, GameScene.WIDTH);
    double y = Utility.range(pos.getY() + movement.getY(), 0, GameScene.HEIGHT);
    x = Utility.range(x, 0, GameScene.WIDTH - getSize().getX());
    y = Utility.range(y, 0, GameScene.HEIGHT - getSize().getY());
    setPosition(x, y);
  }

  public boolean collides(GameObject enemy) {
    Vector2 posA = this.getPosition();
    Vector2 sizeA = this.getSize();
    Vector2 posB = enemy.getPosition();
    Vector2 sizeB = enemy.getSize();

    double leftA = posA.getX();
    double rightA = posA.getX() + sizeA.getX();
    double topA = posA.getY();
    double bottomA = posA.getY() + sizeA.getY();

    double leftB = posB.getX();
    double rightB = posB.getX() + sizeB.getX();
    double topB = posB.getY();
    double bottomB = posB.getY() + sizeB.getY();

    return rightA >= leftB && leftA <= rightB && bottomA >= topB && topA <= bottomB;
  }
}
