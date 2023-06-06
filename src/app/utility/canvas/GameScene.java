package app.utility.canvas;

import java.util.Collection;
import java.util.Vector;

import app.main.controller.GameController;
import app.main.controller.scene.SceneController;
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
  
  public static final double WIDTH = 640;
  public static final double HEIGHT = 360;

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
    stopped =false;

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
  
  public void reset() {

  }

  public Vector2 getCanvasSize() {
    return new Vector2(canvas.getWidth(), canvas.getHeight());
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

  private void refreshFrame(long now) {
    if(stopped) return;
    
    if(startFrameTime <= 0) {
      startFrameTime = now / 1_000_000_000.0;
    }

    current = (now / 1_000_000_000.0) - startFrameTime;
    
    if(pauseFlag) {
      lastFrameTime = current;
      pauseFlag = false;
      accumulatedPauseTime += (current - firstPauseTime);
      System.out.println(current);
      System.out.println(firstPauseTime);
      System.out.println(current - firstPauseTime);
      return;
    }
    
    double deltaTime = current - lastFrameTime;
    
    if (deltaTime < fixedDeltaTime) {
      return;
    }
    
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
    return (long) (current - accumulatedPauseTime) * 1000;
  }
  
  public boolean isPaused() {
    return paused;
  }

  public String getTimeSpentFormat() {
    long time = (long) ((current - accumulatedPauseTime) * 1000);
    long min = time / 60000;
    long sec = time / 1000;
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

  public void stop() {
    animationTimer.stop();
    stopped = true;
  }
  
  public void pause() {
    if(!paused) {
      animationTimer.stop();
      firstPauseTime = current;
      paused = true;      
    }
  }
  
  public void resume() {
    if(paused) {
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