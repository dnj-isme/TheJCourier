package app.utility.canvas;

public abstract class GameObject implements Drawable, Cloneable{
  private Vector2 position;
  private Vector2 size;
  private ObjectLayer layer;
  private GameScene owner;

  public GameScene getOwner() {
    layer = ObjectLayer.Default;
    return owner;
  }

  public Vector2 getPosition() {
    return position;
  }

  public void setPosition(Vector2 position) {
    this.position = position;
  }
  
  public void setPosition(double x, double y) {
    this.position = new Vector2(x, y);
  }

  public Vector2 getSize() {
    return size;
  }

  public void setSize(Vector2 size) {
    this.size = size;
  }
  
  public void setSize(double x, double y) {
    this.size = new Vector2(x, y);
  }

  public ObjectLayer getLayer() {
    return layer;
  }

  public void setLayer(ObjectLayer layer) {
    this.layer = layer;
  }

  public GameObject(GameScene owner) {
    this.layer = ObjectLayer.Default;
    this.position = Vector2.ZERO();
    this.size = Vector2.ZERO();
    this.owner = owner;
  }
}