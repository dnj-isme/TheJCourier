package app.utility.canvas;

import javafx.scene.canvas.GraphicsContext;

public class RenderProperties {
  private GraphicsContext context;
  private double deltaTime;
  private double fixedDeltaTime;
  private long frameCount;
  
  public RenderProperties(GraphicsContext context, double deltaTime, double fixedDeltaTime, long frameCount) {
    super();
    this.context = context;
    this.deltaTime = deltaTime;
    this.fixedDeltaTime = fixedDeltaTime;
    this.frameCount = frameCount;
  }

  public long getFrameCount() {
    return frameCount;
  }

  public GraphicsContext getContext() {
    return context;
  }

  public double getDeltaTime() {
    return deltaTime;
  }

  public double getFixedDeltaTime() {
    return fixedDeltaTime;
  }
}
