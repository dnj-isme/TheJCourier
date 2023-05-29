package app.main.game.object.player;

import app.main.controller.GameController;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PlayerHorizontalSwing extends GameObject implements Collidable, Updatable{

  public static final Vector2 SIZE = new Vector2(30, 20);
  
  private GameController gameController;
  private Player player;
  
  public PlayerHorizontalSwing(GameScene owner, Player player) {
    super(owner);
    this.player = player;
    gameController = GameController.getInstance();
    setSize(SIZE);
  }

  @Override
  public void render(RenderProperties properties) {
    // TODO Auto-generated method stub
    GraphicsContext context = properties.getContext();
    if (gameController.isHitbox()) {
      context.setFill(Color.LIGHTBLUE);
      context.fillRect(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
    }
  }

  @Override
  public void receiveCollision(GameObject object) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void update(RenderProperties properties) {
    // TODO Auto-generated method stub
    player.setPosition(player.getWeaponSwingPosition(getSize()));
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub
    
  }
}
