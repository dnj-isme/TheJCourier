package app.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public final class Message {
  private Message() {
  }

  public static ButtonType show(AlertType type, String title, String content, ButtonType... buttons) {
    Alert a = new Alert(type, content, buttons);
    a.setTitle(title);
    a.show();
    return a.getResult();
  }
}
