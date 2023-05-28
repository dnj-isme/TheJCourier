package app.main.view.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

public class CustomSlider extends HBox {

  private int currentValue;
  private int width;
  private int height;

  private ValueChangedHandler onValueChanged;
  private ObjectProperty<EventHandler<? super MouseEvent>> onMouseReleasedProperty = new SimpleObjectProperty<>();

  public CustomSlider(int initialValue, int width, int height) {
    this.currentValue = initialValue;
    this.width = width;
    this.height = height;
    this.setSpacing(2);
    this.createSlider();

    this.setOnKeyPressed(this::handleKeyPress);
    this.setOnMousePressed(this::handleMousePress);
    this.setOnMouseDragged(this::handleMouseDrag);
    this.setOnMouseReleased(this::handleMouseReleased);
    
    this.setFocusTraversable(true);
  }

  private void createSlider() {
    for (int i = 0; i < 10; i++) {
      Rectangle rect = new Rectangle(width, height);
      rect.setFill(i < currentValue ? Color.WHITE : Color.GRAY);
      this.getChildren().add(rect);
    }
  }
  

  private void updateSlider() {
    for (int i = 0; i < 10; i++) {
      Rectangle rect = (Rectangle) this.getChildren().get(i);
      rect.setFill(i < currentValue ? Color.WHITE : Color.GRAY);
    }
  }

  private void handleKeyPress(KeyEvent event) {
    switch (event.getCode()) {
    case RIGHT:
      if (currentValue < 10) {
        currentValue++;
        updateSlider();
      }
      break;
    case LEFT:
      if (currentValue > 0) {
        currentValue--;
        updateSlider();
      }
      break;
    default:
      break;
    }
  }

  private void handleMousePress(MouseEvent event) {
    handleMouseEvent(event);
  }

  private void handleMouseDrag(MouseEvent event) {
    handleMouseEvent(event);
  }

  private void handleMouseEvent(MouseEvent event) {
    double xPos = event.getX();
    int index = (int) Math.round(xPos / (width + this.getSpacing()));
    if (index >= 0 && index <= 10) {
      setCurrentValue(index);
      updateSlider();
    }
  }
  
  public ValueChangedHandler getOnValueChanged() {
    return onValueChanged;
  }

  public void setOnValueChanged(ValueChangedHandler onValueChanged) {
    this.onValueChanged = onValueChanged;
  }

  public int getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(int currentValue) {
    if (this.currentValue != currentValue && onValueChanged != null) {
      onValueChanged.onValueChanged(this.currentValue, currentValue);
    }
    if (currentValue >= 0 && currentValue <= 10) {
      this.currentValue = currentValue;
      updateSlider();
    }
  }

  private void handleMouseReleased(MouseEvent event) {
      EventHandler<? super MouseEvent> handler = onMouseReleasedProperty.get();
      if (handler != null) {
          handler.handle(event);
      }
  }

  public ObjectProperty<EventHandler<? super MouseEvent>> getOnMouseReleasedProperty() {
      return onMouseReleasedProperty;
  }
}
