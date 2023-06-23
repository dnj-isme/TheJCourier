package app.main.game.object.other;

import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Bola extends GameObject implements Updatable{

  private double speed;
  private Paint color;
  private Vector2 direction;
  
  public Bola(GameScene owner) {
    super(owner);
    this.direction = new Vector2();
    this.speed = 0;
    this.color = Color.BLACK;
  }
  
  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public Paint getColor() {
    return color;
  }

  public void setColor(Paint color) {
    this.color = color;
  }

  public Vector2 getDirection() {
    return direction;
  }

  public void setDirection(Vector2 direction) {
    this.direction = direction;
  }

  @Override
  public void update(RenderProperties properties) {
    // TODO Auto-generated method stub
    if(getOwner().isCollidingWall(this)) {
      if(getPosition().getX() <= getOwner().getBorder()) {
        direction.inverseX();
        getPosition().setX(0);
      }
      
      if(getPosition().getY() <= getOwner().getBorder()) {
        direction.inverseY();
        getPosition().setY(0);
      }
      
      if(getPosition().getX() + getSize().getX() >= getOwner().getCanvas().getWidth() - getOwner().getBorder()) {
        direction.inverseX();
        getPosition().setX(getOwner().getCanvas().getWidth() - getOwner().getBorder() - getSize().getY());
      }
      
      if(getPosition().getY() + getSize().getY() >= getOwner().getCanvas().getHeight() - getOwner().getBorder()) {
        direction.inverseY();
        getPosition().setY(getOwner().getCanvas().getHeight() - getOwner().getBorder() - getSize().getY());
      }
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub
    Vector2 movement = direction.getNormalized().mult(properties.getFixedDeltaTime() * speed);
    this.setPosition(Vector2.add(getPosition(), movement));
  }

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 position = getPosition();
    Vector2 size = getSize();
    
    context.setFill(color);
    context.fillOval(position.getX(), position.getY(), size.getY(), size.getX());
  }

}
