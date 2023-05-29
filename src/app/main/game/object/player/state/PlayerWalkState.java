package app.main.game.object.player.state;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerInputState;
import app.main.game.object.player.PlayerState;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class PlayerWalkState extends PlayerState {
  
  private static PlayerWalkState instance;
  
  public static PlayerWalkState load(Player player) {
    if(instance == null) {
      instance = new PlayerWalkState(player);
    }
    return instance;
  }

  private Image spriteTop;
  private Image spriteBottom;

  private Vector2 imageSize;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private Player player;

  private PlayerWalkState(Player player) {
    super(player);
    this.player = player;
    eventObserver = player.getEventObserver();
    keyBinding = player.getKeyBinding();

    AssetManager asset = AssetManager.getInstance();
    spriteTop = asset.findImage("player_walk_top");
    spriteBottom = asset.findImage("player_walk_bottom");
    imageSize = new Vector2(60, 60);
  }

  private int cd = 2;

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderBottomCenter(player.getPosition(), player.getSize(), imageSize);

    int ticks = (int) properties.getFrameCount() / cd;
    int x = ticks % 8 * 60;
    
    Vector2 facing = updatePlayerDirection();
    if(facing.getX() < 0) {
      renderPos.setX(renderPos.getX() + 60);
    }
    
    context.drawImage(spriteTop, x, 0, 60, 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
    context.drawImage(spriteBottom, x, 0, 60, 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
  }
  
  @Override
  public void update(RenderProperties properties) {
    Vector2 direction = updatePlayerDirection();
    double movement = direction.getX() * player.getMoveSpeed();
    player.getVelocity().setX(movement);
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub
    
  }
}
