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

public class PlayerAttackState extends PlayerState {

  private static PlayerAttackState instance;

  private Image sprite1;
  private Image sprite2;
  private Vector2 imageSize;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private Player player;
  private AssetManager assetManager;

  public static PlayerAttackState load(Player player) {
    if (instance == null) {
      instance = new PlayerAttackState(player);
    }
    return instance;
  }

  private PlayerAttackState(Player player) {
    super(player);
    this.player = player;
    eventObserver = player.getEventObserver();
    keyBinding = player.getKeyBinding();

    assetManager = AssetManager.getInstance();
    sprite1 = assetManager.findImage("player_attack_1");
    sprite2 = assetManager.findImage("player_attack_2");
    imageSize = new Vector2(80, 60);
    player.setStartAttackFrame(-1);
  }

  private double cd = 1.5;

  @Override
  public void render(RenderProperties properties) {
    int index = 7;
    Vector2 renderPos = Vector2.renderBottomCenter(player.getPosition(), player.getSize(), imageSize);
    Image sprite = player.getAttackCount() % 2 == 0 ? sprite1 : sprite2;
    GraphicsContext context = properties.getContext();
    if (player.getStartAttackFrame() != -1) {
      index = Math.min(7, (int) ((properties.getFrameCount() - player.getStartAttackFrame()) / cd));      
    }
    if(index >= 8) {
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
    hanldeHorizontalMovement();
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
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub

  }
}
