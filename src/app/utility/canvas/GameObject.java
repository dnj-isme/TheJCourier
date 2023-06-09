package app.utility.canvas;

import java.util.Comparator;

public abstract class GameObject implements Drawable {
  private Vector2 position;
  private Vector2 size;
  private ObjectLayer layer;
  private GameScene owner;
  private ObjectTag tag;

  public GameScene getOwner() {
    layer = ObjectLayer.Default;
    return owner;
  }

  public ObjectTag getTag() {
    return tag;
  }

  public void setTag(ObjectTag tag) {
    this.tag = tag;
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
    this.tag = ObjectTag.Neutral;
    this.position = Vector2.ZERO();
    this.size = Vector2.ZERO();
    this.owner = owner;
  }

  protected void setOwner(GameScene owner) {
    this.owner = owner;
  }

  public boolean collides(GameObject enemy) {
    Vector2 posA = this.getPosition();
    Vector2 sizeA = this.getSize();
    Vector2 posB = enemy.getPosition();
    Vector2 sizeB = enemy.getSize();

    double leftA = posA.getX();
    double rightA = posA.getX() + sizeA.getX();
    double topA = posA.getY();
    double bottomA = posA.getY() + sizeA.getY();

    double leftB = posB.getX();
    double rightB = posB.getX() + sizeB.getX();
    double topB = posB.getY();
    double bottomB = posB.getY() + sizeB.getY();

    return rightA >= leftB && leftA <= rightB && bottomA >= topB && topA <= bottomB;
  }
}
