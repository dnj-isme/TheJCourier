package app.utility.canvas;

public interface Updatable {
  /**
   * This function is used to handle update per frame.
   * Recommended use cases: handling game logic and user input (if necessary).
   * @param properties
   */
  public void update(RenderProperties properties);
  
  /**
   * This function is used to handle something that specifically requires fixed delta time.
   * Recommended use cases: handling physics and movement.
   * @param fixedDeltaTime
   */
  public void fixedUpdate(RenderProperties properties);
}
