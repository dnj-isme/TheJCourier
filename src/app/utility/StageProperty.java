package app.utility;

import javafx.stage.Stage;

@FunctionalInterface
public interface StageProperty {
  public void apply(Stage stage);
}
