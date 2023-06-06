package app.main.game.object.player.state;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.Utility;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PlayerAttackMidAir extends PlayerState {

  private static PlayerAttackMidAir instance;

  private Image sprite;
  private Vector2 imageSize;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private Player player;
  private AssetManager assetManager;

  public static PlayerAttackMidAir load(Player player) {
    if (instance == null) {
      instance = new PlayerAttackMidAir(player);
    }
    return instance;
  }

  private PlayerAttackMidAir(Player player) {
    super(player);
    this.player = player;
    eventObserver = player.getEventObserver();
    keyBinding = player.getKeyBinding();
    assetManager = AssetManager.getInstance();
    
    sprite = assetManager.findImage("player_attack_airborne");
    imageSize = new Vector2(80, 60);
    player.setStartAttackFrame(-1);
  }

  private double cd = 1.5;

  @Override
  public void render(RenderProperties properties) {
    int index = 7;
    Vector2 renderPos = Vector2.renderBottomCenter(player.getPosition(), player.getSize(), imageSize);
    GraphicsContext context = properties.getContext();
    if (player.getStartAttackFrame() != -1) {
      index = Math.min(7, (int) ((properties.getFrameCount() - player.getStartAttackFrame()) / cd));
    }
    if (index >= 8) {
      index = 7;
      player.setFinishedAttack(true);
    }

    double spriteX = index * imageSize.getX();

    Vector2 facing = player.getFacing();
    if (facing.getX() < 0) {
      renderPos.addX(imageSize.getX());
    }

    context.drawImage(sprite, spriteX, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(), renderPos.getY(),
        imageSize.getX() * facing.getX(), imageSize.getY());
  }

  @Override
  public void update(RenderProperties properties) {
    // TODO Auto-generated method stub
    Vector2 direction = updatePlayerDirection();
    double movement = direction.getX() * player.getMoveSpeed();
    player.getVelocity().setX(movement);
    
    if (player.getStartAttackFrame() == -1) {
      player.setStartAttackFrame(properties.getFrameCount());
      player.addAttackCount();
      player.setAttack(true);
      player.setReleaseAttack(false);
      Platform.runLater(() -> {
        AudioFactory.createSfxHandler(assetManager.findAudio("sfx_sword_" + Utility.random(1, 3))).playThenDestroy();
      });
    } 
    
    if(player.isFinishedAttack()) {
      player.setFinishedAttack(false);
      player.setAttack(false);
      player.setStartAttackFrame(-1);
      player.setState(this, PlayerIdleState.load(player));
    }
    
    if(eventObserver.isPressing(keyBinding.getBinding(KeyBinding.JUMP)) && player.canCloudStep() && player.isReleaseJump()) {
      // Condition 2: Player can jump again if cloud step is active
      player.setReleaseJump(false);
      player.setCloudStep(false);
      player.getVelocity().setY(-player.getJumpForce());
      player.setJump(true);
      player.setTeleport(true);
      player.setFinishedAttack(false);
      player.setAttack(false);
      player.setStartAttackFrame(-1);
      player.setState(this, PlayerJumpState.load(player));
      Platform.runLater(() -> {        
        AudioFactory.createSfxHandler(assetManager.findAudio("sfx_jump")).playThenDestroy();
      });
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    handleGravity(properties.getFixedDeltaTime(), player.getGravity());
  }
}
