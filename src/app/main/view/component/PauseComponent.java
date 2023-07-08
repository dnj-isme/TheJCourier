package app.main.view.component;

import java.util.TimerTask;

import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.main.controller.audio.AudioFactory;
import app.utility.Utility;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PauseComponent extends BorderPane implements Component {
  private EventHandler<ActionEvent> onBackEvent;
  private EventHandler<ActionEvent> onOptionsEvent;
  private EventHandler<ActionEvent> onTitleEvent;

  private AssetManager asset;
  Font fontTitle;
  Font fontText;

  private Image frame;

  private Label top;
  private VBox center;
  private Button optionsButton;
  private Button backBtn;
  private Button titleBtn;
  private Button desktopBtn;

  public PauseComponent() {
    asset = AssetManager.getInstance();

    fontTitle = FontManager.loadFont(28);
    fontText = FontManager.loadFont(18);

    top = new Label("Pause");
    center = new VBox(
        optionsButton = new Button("Options"), 
        backBtn = new Button("Back to Game"),
        titleBtn = new Button("Title Screen"), 
        desktopBtn = new Button("Quit to Desktop"));

    setTop(top);
    setCenter(center);

    setStyle();
    handleEvents();
  }

  public void setOnBackEvent(EventHandler<ActionEvent> onBackEvent) {
    this.onBackEvent = onBackEvent;
  }

  public void setOnOptionsEvent(EventHandler<ActionEvent> onOptionsEvent) {
    this.onOptionsEvent = onOptionsEvent;
  }

  public void setOnTitleEvent(EventHandler<ActionEvent> onTitleEvent) {
    this.onTitleEvent = onTitleEvent;
  }

  private void setStyle() {
    frame = asset.findImage("frame");
    this.setPadding(new Insets(10));
    this.setBackground(new Background(new BackgroundImage(frame, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER, new BackgroundSize(600, 350, false, false, false, false))));
    this.setMaxSize(600, 350);

    center
        .setBackground(new Background(new BackgroundImage(frame, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, new BackgroundSize(500, 250, false, false, false, false))));
    center.setMaxSize(500, 250);

    applyFont(top, fontTitle);
    applyButtonStyle(optionsButton, fontText);
    applyButtonStyle(backBtn, fontText);
    applyButtonStyle(titleBtn, fontText);
    applyButtonStyle(desktopBtn, fontText);

    BorderPane.setAlignment(center, Pos.CENTER);
    BorderPane.setAlignment(top, Pos.CENTER);
    center.setAlignment(Pos.CENTER);
    center.setSpacing(10);
    top.setPadding(new Insets(10));
  }

  private void handleEvents() {
    backBtn.setOnAction((e) -> {
      if (onBackEvent != null) {
        onBackEvent.handle(e);
      }
    });

    optionsButton.setOnAction((e) -> {
      if (onBackEvent != null) {
        onOptionsEvent.handle(e);
      }
    });

    titleBtn.setOnAction((e) -> {
      if (onBackEvent != null) {
        onTitleEvent.handle(e);
      }
    });
    
    desktopBtn.setOnAction((e) -> {
      AudioFactory.createSfxHandler(asset.findAudio("sfx_menu_select_8")).playThenDestroy();
      Utility.delayAction(500, new TimerTask() {
        @Override
        public void run() {
          // TODO Auto-generated method stub
          Platform.runLater(() -> {
            System.exit(0);
          });
        }
      });
    });
  }

  private void applyFont(Labeled node, Font font) {
    node.setFont(font);
    node.setTextFill(Color.WHITE);
  }

  private void applyButtonStyle(Button button, Font font) {
    AssetManager manager = AssetManager.getInstance();
    String defaultStyle = "-fx-background-color: transparent; " + "-fx-border-width: 3px; "
            + "-fx-border-radius: 5px; " + "-fx-text-fill: white; ";
    button.setFont(font);
    button.setStyle(defaultStyle + "-fx-border-color: transparent;");
    button.hoverProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal && !oldVal) {
        AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_cursor_8")).playThenDestroy();
        button.setStyle(defaultStyle + "-fx-border-color: #fcdc80;");
      } else {
        button.setStyle(defaultStyle + "-fx-border-color: transparent;");
      }
    });
  }
}
