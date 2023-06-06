package app.utility.canvas;

import java.sql.Time;
import java.util.Collection;
import java.util.Vector;

import app.main.controller.GameController;
import app.main.controller.scene.SceneController;
import app.utility.Utility;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class GameSceneBackup {
  private Vector<GameObject> gameObjects;
  private Canvas canvas;
  private GraphicsContext context;
  private AnimationTimer animationTimer;
  private GameController controller;

  private double border;

  private double lastFrameTime;
  private double startFrameTime;

  private double fps;
  private double fixedDeltaTime;

  private long frameCount;

  private boolean sorted;

  // Pause Properties
  private boolean paused;
  private double pauseStartTime;
  private double totalPauseDuration;
  private double current;

  public static final double WIDTH = 640;
  public static final double HEIGHT = 360;

  public GameSceneBackup() {
    controller = GameController.getInstance();

    gameObjects = new Vector<>();
    canvas = new Canvas(WIDTH, HEIGHT);

    sorted = false;

    double windowHeight = SceneController.getScreenHeight();
    double windowWidth = SceneController.getScreenWidth();

    double scaleX = windowWidth / WIDTH;
    double scaleY = windowHeight / HEIGHT;

    double scale = Math.min(scaleX, scaleY);

    canvas.setScaleX(scale);
    canvas.setScaleY(scale);

    context = canvas.getGraphicsContext2D();
    context.setImageSmoothing(false);
    frameCount = 0;
    paused = false;
    pauseStartTime = 0;
    totalPauseDuration = 0;

    animationTimer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        refreshFrame(now);
      }
    };

    this.border = 0;

    setFps(controller.getFPS());
    initializeGameObjects();
    sortGameObjects();
  }

  public boolean isPaused() {
    return paused;
  }

  public Vector2 getCanvasSize() {
    return new Vector2(canvas.getWidth(), canvas.getHeight());
  }

  public void addGameObject(GameObject object) {
    sorted = false;
    synchronized (gameObjects) {
      gameObjects.add(object);
    }
  }

  public void addGameObjects(Collection<? extends GameObject> objects) {
    sorted = false;
    synchronized (gameObjects) {
      gameObjects.addAll(objects);
    }
  }

  public boolean isSorted() {
    return sorted;
  }

  public boolean isCollidingWall(GameObject obj) {
    double minX = obj.getPosition().getX();
    double minY = obj.getPosition().getY();
    double maxX = obj.getPosition().getX() + obj.getSize().getX();
    double maxY = obj.getPosition().getY() + obj.getSize().getY();
    double canvasWidth = canvas.getWidth();
    double canvasHeight = canvas.getHeight();

    return minX <= border || minY <= border || maxX >= canvasWidth - border || maxY >= canvasHeight - border;
  }

  public double getBorder() {
    return border;
  }

  public void setBorder(double border) {
    this.border = border;
  }

  protected abstract void initializeGameObjects();

  boolean first = true;

  private void refreshFrame(long now) {
    
    if(startFrameTime <= 0) {
      startFrameTime = now / 1_000_000_000.0;
    }
    
//    if(pauseStartTime > 0) {
//      totalPauseDuration = current - pauseStartTime;
//      pauseStartTime = 0;
//    }
    
    current = (now / 1_000_000_000.0) - startFrameTime;
    double deltaTime = current - lastFrameTime;
    
//    Utility.cls();
//    System.out.println("Running");
//    System.out.println("Start = " + startFrameTime);
//    System.out.println("Curr  = " + current);
//    System.out.println("");
//    System.out.println("Pause");
//    System.out.println("Total = " + totalPauseDuration);
//    System.out.println("");
//    System.out.println("Summary");
//    System.out.println("Total Running App : " + current);
//    System.out.println("With paused : " + (current - startFrameTime - totalPauseDuration));

    performGameLogic(new RenderProperties(context, deltaTime, fixedDeltaTime, frameCount));

    double timeSinceLastFixedUpdate = current - lastFrameTime;
    while (timeSinceLastFixedUpdate >= fixedDeltaTime) {
      notifyFixedUpdate(deltaTime);
      timeSinceLastFixedUpdate -= fixedDeltaTime;
    }

    notifyUpdate(deltaTime);

    drawGameObjects(deltaTime);

    lastFrameTime = current;
    frameCount++;
  }

  public long getTimeSpent() {
    return (long) ((current - totalPauseDuration) * 1000);
  }

  public String getTimeSpentFormat() {
    long time = (long) ((current - totalPauseDuration) * 1000);
    long min = time / (60000);
    long sec = (time / 1000) % 60;
    long ms = time % 1000;
    return String.format("%02d:%02d.%03d", min, sec, ms);
  }

  public String getTimePausedFormat() {
    long time = (long) (totalPauseDuration * 1000);
    long min = time / (60000);
    long sec = (time / 1000) % 60;
    long ms = time % 1000;
    return String.format("%02d:%02d.%03d", min, sec, ms);
  }

  private void notifyUpdate(double deltaTime) {
    RenderProperties prop = new RenderProperties(context, deltaTime, fixedDeltaTime, frameCount);
    for (GameObject obj : gameObjects) {
      if (obj instanceof Updatable) {
        ((Updatable) obj).update(prop);
      }
    }
  }

  private void notifyFixedUpdate(double deltaTime) {
    RenderProperties prop = new RenderProperties(context, deltaTime, fixedDeltaTime, frameCount);
    for (GameObject obj : gameObjects) {
      if (obj instanceof Updatable) {
        ((Updatable) obj).fixedUpdate(prop);
      }
    }
  }

  public abstract void performGameLogic(RenderProperties properties);

  private void drawGameObjects(double deltaTime) {
    RenderProperties prop = new RenderProperties(context, deltaTime, fixedDeltaTime, frameCount);

    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    for (GameObject gameObject : gameObjects) {
      gameObject.render(prop);
    }

    if (border > 0) {
      context.setFill(Color.BLACK);
      context.fillRect(0, 0, canvas.getWidth(), border);
      context.fillRect(0, 0, border, canvas.getHeight());
      context.fillRect(canvas.getWidth() - border, 0, border, canvas.getHeight());
      context.fillRect(0, canvas.getHeight() - border, canvas.getWidth(), border);
    }
  }

  public void setFps(double fps) {
    this.fps = fps;
    this.fixedDeltaTime = 1.0 / fps;
  }

  public double getFps() {
    return fps;
  }

  public void start() {
    animationTimer.start();
  }

  public void pause() {
    if (!paused) {
      pauseStartTime = current;
      paused = true;
      animationTimer.stop();
    }
  }

  public void resume() {
    if (paused) {
      paused = false;
      animationTimer.start();
    }
  }

  public void stop() {
    animationTimer.stop();
  }

  public GameObject[] getGameObjects() {
    if (!sorted) {
      sortGameObjects();
    }
    return gameObjects.toArray(new GameObject[0]);
  }

  public void sortGameObjects() {
    sorted = true;
    gameObjects.sort((a, b) -> a.compareTo(b));
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public GraphicsContext getContext() {
    return context;
  }
}
