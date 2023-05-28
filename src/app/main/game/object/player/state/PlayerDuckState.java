package app.main.game.object.player.state;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.image.Image;

public class PlayerDuckState extends PlayerState {

  private static PlayerDuckState instance;
  
  public static PlayerDuckState load(Player player) {
    if(instance == null) {
      instance = new PlayerDuckState(player);
    }
    return instance;
  }
  
  private Image sprite;
  private Vector2 imageSize;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private Player player;
  private long frameStarted = -1;

  private PlayerDuckState(Player player) {
    super(player);
    this.player = player;
    eventObserver = player.getEventObserver();
    keyBinding = player.getKeyBinding();

    AssetManager asset = AssetManager.getInstance();
    sprite = asset.findImage("player_fall");
    imageSize = new Vector2(60, 60);
  }

  @Override
  public void render(RenderProperties properties) {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void update(RenderProperties properties) {
    super.update(properties);
    updatePlayerDirection();
    if(frameStarted == -1) {
      frameStarted = properties.getFrameCount();
    }
    
    
  }
}
