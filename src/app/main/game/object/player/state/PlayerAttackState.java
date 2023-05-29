package app.main.game.object.player.state;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
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

  private long startFrame;
  private boolean finished;

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
    finished = false;

    AssetManager asset = AssetManager.getInstance();
    sprite1 = asset.findImage("player_attack_1");
    sprite2 = asset.findImage("player_attack_2");
    imageSize = new Vector2(80, 60);
    startFrame = -1;
  }

  public void reset() {
    startFrame = -1;
    finished = false;
  }

  private int cd = 1;

  @Override
  public void render(RenderProperties properties) {
    if (startFrame != -1) {
      Image sprite = player.getAttackCount() % 2 == 0 ? sprite1 : sprite2;
      GraphicsContext context = properties.getContext();
      Vector2 renderPos = Vector2.renderBottomCenter(player.getPosition(), player.getSize(), imageSize);

      int index = Math.min(7, (int) ((properties.getFrameCount() - startFrame) / cd));

      if(index >= 7) {
        finished = true;
        index = 6;
      }
      
      double spriteX = index * imageSize.getX();

      Vector2 facing = player.getFacing();
      if (facing.getX() < 0) {
        renderPos.addX(imageSize.getX());
      }

      context.drawImage(sprite, spriteX, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(), renderPos.getY(),
          imageSize.getX() * facing.getX(), imageSize.getY());
    }
  }

  @Override
  public void update(RenderProperties properties) {
    if (startFrame == -1) {
      startFrame = properties.getFrameCount();
      player.addAttackCount();
      player.setAttack(true);
      player.setReleaseAttack(false);
    }
    
    if(finished) {
      player.setAttack(false);
      player.setState(this, PlayerIdleState.load(player));
      reset();
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub

  }
}
