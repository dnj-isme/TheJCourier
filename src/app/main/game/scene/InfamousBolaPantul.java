package app.main.game.scene;

import app.main.game.object.other.Bola;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.paint.Color;

public class InfamousBolaPantul extends GameScene{

  public InfamousBolaPantul() {
    super();
    setBorder(1);
  }

  @Override
  protected void initializeGameObjects() {
    Bola bola = new Bola(this);
    bola.setColor(Color.RED);
    bola.setDirection(new Vector2(1, 4));
    bola.setSize(10, 10);
    bola.setPosition(4, 4);
    bola.setSize(50, 50);
    bola.setSpeed(500);
    addGameObject(bola);
  }

  @Override
  public void performGameLogic(RenderProperties properties) {
    // TODO Auto-generated method stub
    
  }
}
