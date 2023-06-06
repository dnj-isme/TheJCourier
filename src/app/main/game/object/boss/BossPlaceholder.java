package app.main.game.object.boss;


import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.main.game.object.player.Player;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class BossPlaceholder extends GameObject implements Collidable {

  private Image sprite;
  private Vector2 imageSize;
  private GameController gameController;    
  
  public BossPlaceholder(GameScene owner) {
    super(owner);
    
    setLayer(ObjectLayer.Background);
    setSize(30, 65);
    
    AssetManager asset = AssetManager.getInstance();
    sprite = asset.findImage("boss_placeholder");
    imageSize = new Vector2(120, 120);
    gameController = GameController.getInstance();
  }

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderBottomCenter(getPosition(), getSize(), imageSize);
    
    if (gameController.isHitbox()) {
      context.setFill(Color.RED);
      context.fillRect(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
    }
    
    context.drawImage(sprite, 0, 0, 120, 120, renderPos.getX(), renderPos.getY(), 120, 120);
  }
  
  private int playerAttackNo = -1;

  @Override
  public void receiveCollision(GameObject object) {
    boolean receiveDamage = false;
    if(object instanceof Player) {
      if(playerAttackNo == -1) {
        playerAttackNo = ((Player) object).getAttackCount();
        receiveDamage = true;
      }
      else {
        receiveDamage = ((Player) object).getAttackCount() != playerAttackNo;
      }
    }
    
    if(receiveDamage) {
      playerAttackNo = ((Player) object).getAttackCount();
      System.out.println("The boss receives damage from Player");
    }
  }
}
