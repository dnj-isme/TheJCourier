package app.utility.canvas;

import java.util.*;

import app.main.controller.GameController;
import app.main.controller.scene.SceneController;
import app.utility.Utility;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class GameScene {
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
  private double firstPauseTime;
  private double accumulatedPauseTime;
  private boolean pauseFlag;
  private boolean dead;
  private long startDead;

  public static final double WIDTH = 640;
  public static final double HEIGHT = 360;
  private long startDeadTime = -1;

  public GameScene() {
    controller = GameController.getInstance();
    firstPauseTime = 0;
    accumulatedPauseTime = 0;
    pauseFlag = false;

    gameObjects = new Vector<>();
    canvas = new Canvas(WIDTH, HEIGHT);

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
    stopped = false;
    dead = false;
    startDead = -1;

    animationTimer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        refreshFrame(now);
      }
    };

    this.border = 0;

    setFps(controller.getFPS());
    initializeGameObjects();
  }

  public boolean isDead() {
    return dead;
  }

  public void setDead(boolean dead) {
    if(dead) {
      startDeadTime = getTimeSpent();
    }
    this.dead = dead;
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
  private boolean paused;
  private double current;
  private boolean stopped;

  private GameObject exception;

  public void setException(GameObject object) {
    this.exception = object;
  }

  private void refreshFrame(long now) {
    if (stopped)
      return;

    if (startFrameTime <= 0) {
      startFrameTime = now / 1_000_000_000.0;
    }

    current = (now / 1_000_000_000.0) - startFrameTime;

    if (dead && startDead == -1) {
      startDead = frameCount;
    }

    if (pauseFlag) {
      lastFrameTime = current;
      pauseFlag = false;
      accumulatedPauseTime += (current - firstPauseTime);
      return;
    }

    double deltaTime = current - lastFrameTime;

    if (deltaTime < fixedDeltaTime) {
      return;
    }

    performGameLogic(new RenderProperties(context, deltaTime, fixedDeltaTime, frameCount));

    double timeSinceLastUpdateMovement = current - lastFrameTime;
    while (timeSinceLastUpdateMovement >= fixedDeltaTime) {
      notifyUpdateMovement(deltaTime);
      timeSinceLastUpdateMovement -= fixedDeltaTime;
    }

    notifyUpdate(deltaTime);

    drawGameObjects(deltaTime);

    lastFrameTime = current;
    frameCount++;
  }

  public long getTimeSpent() {
    return dead ? startDeadTime : (long) ((current - accumulatedPauseTime) * 1000);
  }

  public boolean isPaused() {
    return paused;
  }

  public String getTimeSpentFormat() {
    long time = dead ? startDeadTime : (long) ((current - accumulatedPauseTime) * 1000);
    long min = time / 60000;
    long sec = (time / 1000) % 60;
    long ms = time % 1000;
    return String.format("%02d:%02d.%03d", min, sec, ms);
  }

  private void notifyUpdate(double deltaTime) {
    RenderProperties prop = new RenderProperties(context, deltaTime, fixedDeltaTime, frameCount);
    RenderProperties deadProp = new RenderProperties(context, 0, 0, startDead);

    for (GameObject obj : gameObjects) {
      if (obj instanceof Updatable) {
        RenderProperties currentProp = dead ? (obj.equals(exception) ? prop : deadProp) : prop;
        ((Updatable) obj).update(currentProp);
      }
    }
  }

  private void notifyUpdateMovement(double deltaTime) {
    RenderProperties prop = new RenderProperties(context, deltaTime, fixedDeltaTime, frameCount);
    RenderProperties deadProp = new RenderProperties(context, 0, 0, startDead);

    for (GameObject obj : gameObjects) {
      if (obj instanceof Updatable) {
        RenderProperties currentProp = dead ? (obj.equals(exception) ? prop : deadProp) : prop;
        ((Updatable) obj).fixedUpdate(currentProp);
      }
    }
  }

  public abstract void performGameLogic(RenderProperties properties);

  private void drawGameObjects(double deltaTime) {
    RenderProperties prop = new RenderProperties(context, deltaTime, fixedDeltaTime, frameCount);
    RenderProperties deadProp = new RenderProperties(context, 0, 0, startDead);

    context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    for (GameObject gameObject : gameObjects) {
      RenderProperties currentProp = dead ? (gameObject.equals(exception) ? prop : deadProp) : prop;
        gameObject.render(currentProp);
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
//    gameObjects.sort(Comparator.comparingInt(a -> a.getLayer().getIndex()));
    gameObjects.sort(Comparator.comparingInt(a -> a.getLayer().getIndex()));
    Set<Object> uniqueObjects = new HashSet<>();

    Iterator<GameObject> iterator = gameObjects.iterator();

    while (iterator.hasNext()) {
      Object obj = iterator.next();
      if (uniqueObjects.contains(obj)) {
        iterator.remove();
      } else {
        uniqueObjects.add(obj);
      }
    }

    Utility.debug("Class = " + getClass().toGenericString());
    for(int i =0; i < gameObjects.size(); i++) {
      Utility.debug(String.format("%d. %s, %s",i + 1, gameObjects.get(i).getLayer().toString(), gameObjects.get(i).getClass().toGenericString()));
    }
    animationTimer.start();
  }

  public void stop() {
    animationTimer.stop();
    stopped = true;
  }

  public void pause() {
    if (!paused) {
      animationTimer.stop();
      firstPauseTime = current;
      paused = true;
    }
  }

  public void resume() {
    if (paused) {
      animationTimer.start();
      paused = false;
      pauseFlag = true;
    }
  }

  public Vector<GameObject> getGameObjects() {
    return gameObjects;
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public GraphicsContext getContext() {
    return context;
  }

  public void addGameObject(GameObject object) {
    gameObjects.add(object);
  }

  public void addGameObjects(Collection<? extends GameObject> objects) {
    gameObjects.addAll(objects);
  }
}