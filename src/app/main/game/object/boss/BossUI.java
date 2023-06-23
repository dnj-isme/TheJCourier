package app.main.game.object.boss;

import app.utility.Utility;
import app.utility.canvas.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BossUI extends GameObject{

  private Boss boss;
  private Vector2 size;
  
  public BossUI(GameScene owner, Boss boss) {
    super(owner);
    this.boss = boss;
    setLayer(ObjectLayer.UI);
    setSize(50, 5);
    reset(owner);
  }

  public void reset(GameScene owner) {
    setOwner(owner);
  }

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderTopCenter(boss.getPosition(), boss.getSize(), getSize());
    renderPos.subsY(10);
    int p1 = Utility.clamp(boss.getHp() - 60, 0, 40);
    int p2 = Utility.clamp(boss.getHp() - 30, 0 ,30);
    int p3 = Utility.clamp(boss.getHp(), 0, 30);
    if(p3 > 0) {
      double width = (double) p3 / 30 * getSize().getX();
      context.setFill(Color.rgb(250, 50, 0));
      context.fillRect(renderPos.getX(), renderPos.getY(), width, 5);
    }
    if(p2 > 0) {
      double width = (double) p2 / 30 * getSize().getX();
      context.setFill(Color.rgb(250, 166, 0));
      context.fillRect(renderPos.getX(), renderPos.getY(), width, 5);
    }
    if(p1 > 0) {
      double width = (double) p1 / 40 * getSize().getX();
      context.setFill(Color.rgb(10, 200, 50));
      context.fillRect(renderPos.getX(), renderPos.getY(), width, 5);
    }
  }
}
