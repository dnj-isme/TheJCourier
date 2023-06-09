package app.main.game.object.player;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PlayerUI extends GameObject{

  private Player player;
  private GameController controller;
  private Font font;
  private AssetManager manager;
  private Image health_empty, health_filled;
  private Image shuriken_empty, shuriken_filled;
  
  public PlayerUI(GameScene owner, Player player) {
    super(owner);
    this.setLayer(ObjectLayer.UI);
    this.player = player;
    controller = GameController.getInstance(); 
    font = FontManager.loadFont(12);
    
    manager = AssetManager.getInstance();
    health_empty = manager.findImage("health_empty");
    health_filled = manager.findImage("health_filled");
    shuriken_empty = manager.findImage("shuriken_empty");
    shuriken_filled = manager.findImage("shuriken_filled");
  }

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();

    context.setFill(Color.WHITE);
    context.setFont(font);
    context.fillText("Player", 20, 20);
    
    for(int i = 0; i < 8; i++) {
      context.drawImage(player.getHealth() > i ? health_filled : health_empty, 20 + 8 * i, 25);
    }
    
    for(int i = 0; i < 5; i++) {
      context.drawImage(player.getShuriken() > i ? shuriken_filled : shuriken_empty, 90 + 14 * i, 25);
    }
    
    if(controller.isShowTimer() && !player.isHideTimer()) {      
      context.fillText(getOwner().getTimeSpentFormat(), GameScene.WIDTH - 120, 30);
    }
  }
}
