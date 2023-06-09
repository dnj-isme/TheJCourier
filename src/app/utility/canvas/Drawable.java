package app.utility.canvas;

public interface Drawable {
	/**
	 * Method to render sprite to the canvas. Friendly reminder: Player/Enemy will
	 * more refer to the center part of the sprite.
	 * 
	 * @param properties
	 */
	public void render(RenderProperties properties);
}
