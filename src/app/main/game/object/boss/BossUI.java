package app.main.game.object.boss;

import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.RenderProperties;

public class BossUI extends GameObject{

  private Boss boss;
  
  public BossUI(GameScene owner, Boss boss) {
    super(owner);
    this.boss = boss;
    setLayer(ObjectLayer.UI);
  }

  @Override
  public void render(RenderProperties properties) {
    
  }
}
