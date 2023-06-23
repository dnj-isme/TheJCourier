package app.main.game.object.player;

import app.main.controller.GameController;
import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.boss.Boss;
import app.main.game.object.player.shuriken.Shuriken;
import app.main.game.object.player.shuriken.ShurikenPool;
import app.main.game.object.player.state.PlayerAttackGlideState;
import app.main.game.object.player.state.PlayerAttackMidAir;
import app.main.game.object.player.state.PlayerAttackState;
import app.main.game.object.player.state.PlayerAttackWalkState;
import app.main.game.object.player.state.PlayerDiedState;
import app.main.game.object.player.state.PlayerDuckState;
import app.main.game.object.player.state.PlayerFallState;
import app.main.game.object.player.state.PlayerGlideState;
import app.main.game.object.player.state.PlayerHurtState;
import app.main.game.object.player.state.PlayerIdleState;
import app.main.game.object.player.state.PlayerJumpState;
import app.main.game.object.player.state.PlayerSpawnedState;
import app.main.game.object.player.state.PlayerWalkState;
import app.main.game.object.player.swing.PlayerGlideSwing;
import app.main.game.object.player.swing.PlayerSwing;
import app.utility.Utility;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.ObjectTag;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import app.utility.canvas.Vector2;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Player extends GameObject implements Updatable, Collidable {

  private static Player instance;

  public static Player getInstance(GameScene owner) {
    if (instance == null) {
      instance = new Player(owner);
    }
    return instance;
  }

  private GameController gameController;
  private PlayerState state;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private AssetManager assets;
  
  private double floorHeight;
  private double ceilHeight;
  
  // Player stats
  private int health = 8;
  private int shuriken = 5;
  private boolean glide = false;
  private int attackCount = 0;
  private boolean hideTimer = false;
  private boolean firstGlide = false;

  // Player Movements
  private boolean cloudStep = false;
  private boolean jump = false;
  private boolean duck = false;
  private boolean attack = false;
  private boolean teleport = false;
  private boolean hurt = false;
  private boolean invincible = false;
  private int invincibleDuration = 2000; // satuan ms (1000 ms = 1 s)
  private long startInvincible = -1;

  private long startAttackFrame = -1;
  private boolean finishedAttack = false;

  private long lastHitOnGlide = -1;
  private boolean glideHit = false;

  private double teleportDistance = 200;
  private Vector2 teleportLocation = new Vector2();
  private double radius = 0;
  private double shrinkSpeed = 40;

  // Related to input
  private boolean releaseJump = true;
  private boolean releaseAttack = true;
  private boolean releaseGlide = true;
  private boolean releaseShuriken = true;
  private boolean releaseTeleport = true;

  private Image sprite = AssetManager.getInstance().findImage("player_cloudstep");

  // Movement constants
  private double gravity = 3000;
  private double maxGlideFall = 160;
  private double moveSpeed = 400;
  private double jumpForce = 750;
  private double smallJumpForce = 500;

  // Movement calculations
  private Vector2 facing = Vector2.RIGHT();
  private Vector2 velocity = Vector2.ZERO();

  private ShurikenPool pool;
  private PlayerSwing playerSwing;
  private PlayerGlideSwing playerGlideSwing;
  private long startRecharge = -1;
  private boolean dead;
  
  private boolean spawn;
  private boolean reverseSpawn;
  private boolean invisible;
  private boolean freezeInput;
  
  public void triggerSpawn(boolean disappear) {
    spawn = true;
    reverseSpawn = disappear;
  }

  public static Vector2 SIZE = new Vector2(24, 40);

  public void reset(GameScene owner) {
    setLayer(ObjectLayer.Player);
    health = 8;
    shuriken = 5;
    glide = false;
    invincible = false;
    hurt = false;
    cloudStep = false;
    jump = false;
    duck = false;
    releaseJump = true;
    releaseAttack = true;
    releaseGlide = true;
    releaseShuriken = true;
    releaseTeleport = true;
    facing = Vector2.RIGHT();
    velocity = Vector2.ZERO();
    attackCount = 0;
    startAttackFrame = -1;
    finishedAttack = false;
    lastHitOnGlide = -1;
    glideHit = false;
    dead = false;
    hideTimer = false;
    floorHeight = 0;
    ceilHeight = 0;
    invisible = false;
    hasDead = false;
    state = PlayerIdleState.load(this);

    setOwner(owner);
    pool = new ShurikenPool(shuriken, owner);
    playerSwing = new PlayerSwing(owner, this);
    playerGlideSwing = new PlayerGlideSwing(owner, this);

    owner.addGameObject(new PlayerUI(owner, this));
    owner.addGameObject(playerSwing);
    owner.addGameObject(playerGlideSwing);
  }

  public Player(GameScene owner) {
    super(owner);
    setLayer(ObjectLayer.Player);

    gameController = GameController.getInstance();
    eventObserver = SceneEventObserver.getInstance();
    keyBinding = KeyBinding.getIntance();
    state = PlayerIdleState.load(this);
    assets = AssetManager.getInstance();

    super.setSize(SIZE);
    pool = new ShurikenPool(shuriken, getOwner());
    playerSwing = new PlayerSwing(owner, this);
    playerGlideSwing = new PlayerGlideSwing(owner, this);

    owner.addGameObject(new PlayerUI(owner, this));
    owner.addGameObject(playerSwing);
    owner.addGameObject(playerGlideSwing);
  }

  public boolean isSpawn() {
    return spawn;
  }

  public void setSpawn(boolean spawn) {
    this.spawn = spawn;
  }

  public boolean isReverseSpawn() {
    return reverseSpawn;
  }

  public void setReverseSpawn(boolean reverseSpawn) {
    this.reverseSpawn = reverseSpawn;
  }

  public double getCeilHeight() {
    return ceilHeight;
  }

  public void setCeilHeight(double ceilHeight) {
    this.ceilHeight = ceilHeight;
  }

  public double getFloorHeight() {
    return floorHeight;
  }

  public void setFloorHeight(double floorHeight) {
    this.floorHeight = floorHeight;
  }

  public boolean isHideTimer() {
    return hideTimer;
  }

  public void setHideTimer(boolean hideTimer) {
    this.hideTimer = hideTimer;
  }

  public boolean isInvincible() {
    return invincible;
  }

  public boolean isHurt() {
    return hurt;
  }

  public void setHurt(boolean hurt) {
    this.hurt = hurt;
  }

  public boolean canTeleport() {
    return teleport;
  }

  public void setTeleport(boolean teleport) {
    this.teleport = teleport;
  }

  public boolean isReleaseTeleport() {
    return releaseTeleport;
  }

  public void setReleaseTeleport(boolean releaseTeleport) {
    this.releaseTeleport = releaseTeleport;
  }

  public ShurikenPool getPool() {
    return pool;
  }

  public void setPool(ShurikenPool pool) {
    this.pool = pool;
  }

  public PlayerSwing getPlayerSwing() {
    return playerSwing;
  }

  public void setPlayerSwing(PlayerSwing playerSwing) {
    this.playerSwing = playerSwing;
  }

  public PlayerGlideSwing getPlayerGlideSwing() {
    return playerGlideSwing;
  }

  public void setPlayerGlideSwing(PlayerGlideSwing playerGlideSwing) {
    this.playerGlideSwing = playerGlideSwing;
  }

  public boolean isFirstGlide() {
    return firstGlide;
  }

  public void setFirstGlide(boolean firstGlide) {
    this.firstGlide = firstGlide;
  }

  public boolean isGlideHit() {
    return glideHit;
  }

  public void setGlideHit(boolean glideHit) {
    this.glideHit = glideHit;
  }

  public long getLastHitOnGlide() {
    return lastHitOnGlide;
  }

  public void setLastHitOnGlide(long lastHitOnGlide) {
    this.lastHitOnGlide = lastHitOnGlide;
  }

  public double getSmallJumpForce() {
    return smallJumpForce;
  }

  public void setSmallJumpForce(double smallJumpForce) {
    this.smallJumpForce = smallJumpForce;
  }

  public boolean isFinishedAttack() {
    return finishedAttack;
  }

  public void setFinishedAttack(boolean finishedAttack) {
    this.finishedAttack = finishedAttack;
  }

  public long getStartAttackFrame() {
    return startAttackFrame;
  }

  public void setStartAttackFrame(long startAttackFrame) {
    this.startAttackFrame = startAttackFrame;
  }

  public int getAttackCount() {
    return attackCount;
  }

  public void addAttackCount() {
    attackCount++;
  }

  public boolean isAttacking() {
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
    return getPosition().getY() >= GameScene.HEIGHT - floorHeight - getSize().getY();
  }

  public boolean isGliding() {
    return glide;
  }

  public Vector2 getWeaponSwingPosition(Vector2 swingSize) {
    double x = getPosition().getX();
    double y = getPosition().getY();

    if (facing.getX() < 0) {
      x -= swingSize.getX() - 5;
    } else {
      x += getSize().getX() - 5;
    }

    if (duck) {
      y += getSize().getY() * 0.5;
    } else {
      y += 5;
    }

    return new Vector2(x, y);
  }

  public PlayerInputState getCurrentState() {

    if (dead || (health <= 0 && !gameController.isInvincible())) {
      return PlayerInputState.Dead;
    }
    
    if(spawn) {
      return PlayerInputState.Spawned;
    }

    if (hurt) {
      return PlayerInputState.Hurt;
    }

    SceneEventObserver observer = SceneEventObserver.getInstance();
    KeyBinding binding = KeyBinding.getIntance();

    KeyCode attackBinding = binding.getBinding(KeyBinding.ATTACK);
    KeyCode leftBinding = binding.getBinding(KeyBinding.LEFT);
    KeyCode rightBinding = binding.getBinding(KeyBinding.RIGHT);
    KeyCode jumpBinding = binding.getBinding(KeyBinding.JUMP);
    KeyCode duckBinding = binding.getBinding(KeyBinding.DUCK);

    if (isTouchingGround()) {
      if(freezeInput) {
        return PlayerInputState.Idle;
      }
      
      if (observer.isPressing(jumpBinding) && releaseJump && releaseGlide) {
        return PlayerInputState.Jump;
      }

      if (observer.isPressing(duckBinding)) {
        return PlayerInputState.Duck;
      }

      if (observer.isPressing(leftBinding) || observer.isPressing(rightBinding)) {
        if (observer.isPressing(attackBinding) && releaseAttack || attack) {
          return PlayerInputState.AttackWalk;
        }
        return PlayerInputState.Walk;
      }

      if (observer.isPressing(attackBinding) && releaseAttack || attack) {
        return PlayerInputState.Attack;
      }
      return PlayerInputState.Idle;
    } else {
      if (glide) {
        if (observer.isPressing(attackBinding) && releaseAttack || attack) {
          return PlayerInputState.AttackGlide;
        } else
          return PlayerInputState.Glide;
      }

      if (observer.isPressing(attackBinding) && releaseAttack || attack) {
        return PlayerInputState.AttackMidAir;
      }

      return velocity.getY() >= 0 ? PlayerInputState.Fall : PlayerInputState.Jump;
    }
  }

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    if (gameController.isHitbox() && !dead) {
      context.setFill(Color.GREEN);
      context.fillRect(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
    }

    if (radius > 0) {
      double topLeftX = teleportLocation.getX() - radius;
      double topLeftY = teleportLocation.getY() - radius;
      context.setFill(Color.rgb(173, 216, 230, 0.6));
      context.fillOval(topLeftX, topLeftY, radius * 2, radius * 2);
    }

    if (cloudStep) {
      int index = (int) ((properties.getFrameCount() / 2) % 4);
      int xPos = 24 * index;
      double xDes = getPosition().getX() + (getSize().getX() / 2) - 12;

      context.drawImage(sprite, xPos, 0, 24, 24, xDes, getPosition().getY() + getSize().getY(), 24, 24);
    }

    if (invisible || (invincible && ((getOwner().getTimeSpent() - startInvincible) / 100) % 2 == 0)) {
      return;
    }

    state.render(properties);
  }

  @Override
  public void receiveCollision(GameObject object) {
    if (object.getTag() == ObjectTag.Enemy && isAlive()) {
      AudioFactory.createSfxHandler(assets.findAudio("sfx_player_hurt")).playThenDestroy();
      if (gameController.isInvincible()) {
        health = Math.max(1, health - 1);
      } else {
        health--;
      }
      hurt = true;
      invincible = true;
      startInvincible = getOwner().getTimeSpent();
      PlayerHurtState.load(this).triggerHurt();
    }
  }

  @Override
  public void update(RenderProperties properties) {
    if(getOwner().isDead() && health > 0) return;

    if (invincible && getOwner().getTimeSpent() >= startInvincible + invincibleDuration) {
      invincible = false;
      startInvincible = -1;
    }

    if (radius > 0) {
      radius = Math.max(0, radius - shrinkSpeed * properties.getDeltaTime());
    }
    if (jump && !eventObserver.isPressing(keyBinding.getBinding(KeyBinding.JUMP))) {
      jump = false;
      cloudStep = false;
      releaseJump = true;
    }
    
    if(isTouchingGround()) {
      glide = false;
      cloudStep = false;
    }
    
    if (!eventObserver.isPressing(keyBinding.getBinding(KeyBinding.JUMP))) {
      releaseGlide = true;
    }

    if (!eventObserver.isPressing(keyBinding.getBinding(KeyBinding.DUCK))) {
      duck = false;
      PlayerDuckState.load(this).reset();
    }

    if (!eventObserver.isPressing(keyBinding.getBinding(KeyBinding.ATTACK))) {
      releaseAttack = true;
    }

    if (attack) {
      int index = Math.min(7, (int) ((properties.getFrameCount() - startAttackFrame) / 1));

      if (index == 7) {
        finishedAttack = true;
        attack = false;
      }
    }

    PlayerState to = PlayerIdleState.load(this);

    switch (getCurrentState()) {
    case Spawned:
      to = PlayerSpawnedState.load(this);
      break;
    case Hurt:
      to = PlayerHurtState.load(this);
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
    case AttackMidAir:
      to = PlayerAttackMidAir.load(this);
      break;
    case Attack:
      to = PlayerAttackState.load(this);
      break;
    case Dead:
      to = PlayerDiedState.load(this);
      break;
    case Idle:
    default:
      velocity = Vector2.ZERO();
      break;
    }
    handlePositioning(state, to);

    if (eventObserver.isPressing(keyBinding.getBinding(KeyBinding.SHURIKEN)) && !freezeInput) {
      if (!glide && shuriken > 0 && releaseShuriken) {
        pool.deploy(this);
        shuriken--;
        if (startRecharge == -1) {
          startRecharge = getOwner().getTimeSpent();
        }
        releaseShuriken = false;
      }
    } else {
      releaseShuriken = true;
    }

    if (startRecharge != -1 && getOwner().getTimeSpent() - startRecharge >= 10_000) {
      shuriken++;
      Platform.runLater(() -> {
        AudioFactory.createSfxHandler(assets.findAudio("sfx_shuriken_pickup")).playThenDestroy();
      });

      if (shuriken == 5) {
        startRecharge = -1;
      } else {
        startRecharge += 10_000;
      }
    }

    if (eventObserver.isPressing(keyBinding.getBinding(KeyBinding.TELEPORT))) {
      if (teleport && !isTouchingGround() && releaseTeleport && !glide) {
        teleport = false;
        releaseTeleport = false;
        getPosition().addX(facing.getX() * teleportDistance);
        velocity.setY(0);
        teleportLocation = getPosition().copy().add(getSize().getX() / 2, getSize().getY() / 2);
        radius = 25;
      }
    } else {
      releaseTeleport = true;
    }
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
    setSize(SIZE.getX(), Player.SIZE.getY() / 2);
  }

  private void bottomFromTop() {
    setSize(SIZE.getX(), Player.SIZE.getY() / 2);
  }

  private void fullFromTop() {
    setSize(SIZE);
  }

  private void fullFromBottom() {
    setSize(SIZE);
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
    if(getOwner().isDead()) return;

    state.fixedUpdate(properties);
    Vector2 movement = getVelocity().mult(properties.getFixedDeltaTime());
    Vector2 pos = getPosition();
    double x = Utility.clamp(pos.getX() + movement.getX(), 0, GameScene.WIDTH);
    double y = Utility.clamp(pos.getY() + movement.getY(), 0, GameScene.HEIGHT);
    x = Utility.clamp(x, 0, GameScene.WIDTH - getSize().getX());
    y = Utility.clamp(y, 0, GameScene.HEIGHT - floorHeight - getSize().getY());
    setPosition(x, y);
  }

  public boolean collides(GameObject enemy) {
    if(enemy instanceof Boss && ((Boss) enemy).isDead()) return false;
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

  boolean hasDead = false;

  public void setDead() {
    if(!hasDead) {
      PlayerDiedState.load(this).reset();
      hasDead = true;
      health = 0;
      dead = true;
      hurt = false;
      invincible = false;
      startInvincible = -1;
      getOwner().setException(this);
      getOwner().setDead(true);
      if(onDead != null) {
        onDead.run();
      }
    }
  }

  private Runnable onDead;

  public void setOnDead(Runnable onDead) {
    this.onDead = onDead;
  }

  public boolean isAlive() {
    return !dead && (gameController.isInvincible() || health > 0);
  }

  public void setInvisible(boolean b) {
    invisible = b;
  }

  public void setFreezeInput(boolean b) {
    freezeInput = b;
  }

  public Runnable getOnDead() {
    return onDead;
  }
}
