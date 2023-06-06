package app.utility.canvas;

public class Vector2 {
  private double x;
  private double y;

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
  }
  
  public void setAdd(Vector2 vector) {
    setAdd(vector.getX(), vector.getY());
  }
  
  public void setAdd(double x, double y) {
    this.x += x;
    this.y += y;
  }
  
  public void setSubs(Vector2 vector) {
    setSubs(vector.getX(), vector.getY());
  }
  
  public void setSubs(double x, double y) {
    this.x -= x;
    this.y -= y;
  }
  
  public void setMult(double number) {
    this.x *= number;
    this.y *= number;
  }
  
  public void addX(double value) {
    this.x += value;
  }
  
  public void addY(double value) {
    this.y += value;
  }
  
  public void subsX(double value) {
    this.x -= value;
  }
  
  public void subsY(double value) {
    this.y -= value;
  }
  
  public void multX(double value) {
    this.x *= value;
  }
  
  public void multY(double value) {
    this.y *= value;
  }

  public void set(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void set(Vector2 a) {
    this.x = a.x;
    this.y = a.y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getMagnitude() {
    return magnitude(this);
  }

  public Vector2 getNormalized() {
    return normalize(this);
  }

  public Vector2() {
    this.x = 0;
    this.y = 0;
  }

  public Vector2(double x, double y) {
    super();
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return String.format("(%.2f, %.2f)", x, y);
  }

  public Vector2 add(Vector2 a) {
    return add(this, a);
  }
  
  public Vector2 add(double x, double y) {
    return add(this, new Vector2(x, y));
  }

  public Vector2 subs(Vector2 a) {
    return subs(this, a);
  }
  
  public Vector2 subs(double x, double y) {
    return subs(this, new Vector2(x, y));
  }

  public Vector2 mult(double a) {
    return mult(this, a);
  }

  public void inverseX() {
    this.x *= -1;
  }
  
  public void inverseY() {
    this.y *= -1;
  }
  
  public void inverse() {
    this.x *= -1;
    this.y *= -1;
  }

  public void normalize() {
    double length = getMagnitude();
    if (length != 0) {
      this.x /= length;
      this.y /= length;
    } else {
      this.x = 0;
      this.y = 0;
    }
  }
  
  public Vector2 copy() {
    return new Vector2(x, y);
  }

  public static Vector2 ONE() {
    return new Vector2(1, 1);
  }

  public static Vector2 ZERO() {
    return new Vector2(0, 0);
  }

  public static Vector2 UP() {
    return new Vector2(0, -1);
  }

  public static Vector2 DOWN() {
    return new Vector2(0, 1);
  }

  public static Vector2 LEFT() {
    return new Vector2(-1, 0);
  }

  public static Vector2 RIGHT() {
    return new Vector2(1, 0);
  }

  public static Vector2 add(Vector2 a, Vector2 b) {
    return new Vector2(a.x + b.x, a.y + b.y);
  }

  public static Vector2 subs(Vector2 a, Vector2 b) {
    return new Vector2(a.x - b.x, a.y - b.y);
  }

  public static Vector2 mult(Vector2 a, double value) {
    return new Vector2(a.x * value, a.y * value);
  }

  public static Vector2 normalize(Vector2 a) {
    double length = magnitude(a);

    if (length != 0) {
      double normalizedX = a.x / length;
      double normalizedY = a.y / length;
      return new Vector2(normalizedX, normalizedY);
    } else {
      return new Vector2(0, 0);
    }
  }

  public static double magnitude(Vector2 a) {
    return Math.sqrt(a.x * a.x + a.y * a.y);
  }
  
  public static double distance(Vector2 a, Vector2 b) {
    return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
  }
  
  public static Vector2 inverse(Vector2 a) {
    return new Vector2(-a.getX(), -a.getY());
  }
  
  public static Vector2 renderBottomCenter(Vector2 pos, Vector2 objSize, Vector2 imageSize) {
    return new Vector2(
        pos.getX() + (objSize.getX() / 2) - (imageSize.getX() / 2),
        pos.getY() + objSize.getY() - imageSize.getY()
    );
  }
  
  public static Vector2 renderCenter(Vector2 pos, Vector2 objSize, Vector2 imageSize) {
    return new Vector2(
        pos.getX() + (objSize.getX() / 2) - (imageSize.getX() / 2),
        pos.getY() + (objSize.getY() / 2) - (imageSize.getY() / 2)
    );
  }
  
  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof Vector2)) {
      return false;
    }
    Vector2 compare = (Vector2) obj;
    return compare.getX() == x && compare.getY() == y;
  }

  public static Vector2 square(double length) {
    return new Vector2(length, length);
  }
}
