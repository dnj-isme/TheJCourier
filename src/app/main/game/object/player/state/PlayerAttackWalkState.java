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

public class PlayerAttackWalkState extends PlayerState{

  private static PlayerAttackWalkState instance;
  
  public static PlayerAttackWalkState load(Player player) {
    if(instance == null) {
      instance = new PlayerAttackWalkState(player);
    }
    return instance;
  }

  private Image spriteTop;
  private Image spriteBottom;

  private Vector2 imageSizeBottom;
  private Vector2 imageSizeTop;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private Player player;
  private AssetManager assetManager;

  private PlayerAttackWalkState(Player player) {
    super(player);
    this.player = player;
    eventObserver = player.getEventObserver();
    keyBinding = player.getKeyBinding();

    assetManager = AssetManager.getInstance();
    spriteTop = assetManager.findImage("player_walk_attack");
    spriteBottom = assetManager.findImage("player_walk_bottom");
    imageSizeBottom = new Vector2(60, 60);
    imageSizeTop = new Vector2(80, 60);
  }
  
  double cd = 2;

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderBottomPos = Vector2.renderBottomCenter(player.getPosition(), player.getSize(), imageSizeBottom);
    Vector2 renderTopPos = Vector2.renderBottomCenter(player.getPosition(), player.getSize(), imageSizeTop);

    int ticks = (int) (properties.getFrameCount() / cd);
    int xBottom = ticks % 8 * 60;
    
    int index = Math.min(7, (int) ((properties.getFrameCount() - player.getStartAttackFrame())));

    if(index >= 7) {
      index = 6;
      player.setFinishedAttack(true);
      player.setAttack(false);
    }
    
    int xTop = (int) (index * imageSizeTop.getX());
    
    Vector2 facing = player.getFacing();
    if(facing.getX() < 0) {
      renderBottomPos.setX(renderBottomPos.getX() + 60);
      renderTopPos.setX(renderTopPos.getX() + 80);
    }
    
    context.drawImage(spriteTop, xTop, 0, 60, 60, renderTopPos.getX(), renderTopPos.getY(), 60 * facing.getX(), 60);
    context.drawImage(spriteBottom, xBottom, 0, 60, 60, renderBottomPos.getX(), renderBottomPos.getY(), 60 * facing.getX(), 60);
  }
  
  public void reset() {
  }

  @Override
  public void update(RenderProperties properties) {
    // TODO Auto-generated method stub
    hanldeHorizontalMovement();

    if (player.getStartAttackFrame() == -1) {
      player.setStartAttackFrame(properties.getFrameCount());
      player.setAttack(true);
      player.addAttackCount();
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
