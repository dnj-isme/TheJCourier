package app.main.game.object.other;

import app.main.controller.asset.AssetManager;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Gate extends GameObject {

  private Image gate;
  private Image checkpoint;
  private Image gateGlow;
  
  public Gate(GameScene owner) {
    super(owner);
    setSize(100, 104);
    setLayer(ObjectLayer.Block);
    
    AssetManager assets = AssetManager.getInstance();
    gate = assets.findImage("gate");
    checkpoint = assets.findImage("checkpoint");
    gateGlow = assets.findImage("gate_glow");
  }
  
  int cd = 10;

  @Override
  public void render(RenderProperties properties) {
    Vector2 position = getPosition();
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderCenter(position, getSize(), new Vector2(60 * 1.2, 100 * 1.2));
    int index = (int) (3 + ((properties.getFrameCount()) / cd) % 5);
    context.drawImage(gateGlow, 60 * index, 0, 60, 100, renderPos.getX(), renderPos.getY() - 25, 60 * 1.2, 120 * 1.2);
    context.drawImage(gate, position.getX(), position.getY());
    context.drawImage(checkpoint, position.getX() + 20, position.getY() + 84);
  }
}
