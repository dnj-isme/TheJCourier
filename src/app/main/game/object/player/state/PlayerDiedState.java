package app.main.game.object.player.state;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.main.view.YouLostMenu;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PlayerDiedState extends PlayerState {

  private static PlayerDiedState instance;

  public static PlayerDiedState load(Player player) {
    if (instance == null) {
      instance = new PlayerDiedState(player);
    }
    return instance;
  }

  private Image diedSprite;
  private Image hurtSprite;
  private Vector2 imageSize;
  private Player player;
  private long duckFrameStarted = -1;
  private AssetManager asset;
  private boolean playDeadSound;

  public PlayerDiedState(Player player) {
    super(player);
    this.player = player;

    asset = AssetManager.getInstance();
    diedSprite = asset.findImage("player_dead");
    hurtSprite = asset.findImage("player_hurt");
    imageSize = new Vector2(60, 60);
    start = false;
    playDeadSound = false;
    startFrame = -1;
  }

  int cd = 2;
  int startDuration = 30;
  private boolean start;
  private long startFrame;

  public void reset() {
    start = true;
    playDeadSound = false;
  }

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderBottomCenter(player.getPosition(), player.getSize(), imageSize);
    
    Vector2 facing = player.getFacing();
    if(facing.getX() < 0) {
      renderPos.setX(renderPos.getX() + 60);
    }
    
    if(properties.getFrameCount() < startFrame + startDuration) {
      context.drawImage(hurtSprite, 0, 0, imageSize.getX(), 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
    }
    else {
      long index = (properties.getFrameCount() - (startFrame + startDuration)) / cd;
      context.drawImage(diedSprite, imageSize.getX() * index, 0, imageSize.getX(), 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
      if(index >= 40) {
        SceneController.getInstance().switchScene(new YouLostMenu());
        player.getOwner().stop();
      }
    }
  }

  @Override
  public void update(RenderProperties properties) {
    if(start) {
      start = false;
      startFrame = properties.getFrameCount();
      AudioFactory.createSfxHandler(asset.findAudio("sfx_player_hurt")).playThenDestroy();
    }
    else if(properties.getFrameCount() >= startFrame + startDuration && !playDeadSound) {
      playDeadSound = true;
      AudioFactory.createSfxHandler(asset.findAudio("sfx_died")).playThenDestroy();
    }
    player.setVelocity(Vector2.ZERO());
  } 

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub

  }
}
