package app.main.view.component;

import app.main.controller.GameController;
import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.audio.AudioHandler;
import app.utility.Utility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class KeyBindingMenu extends BorderPane implements Component {
  private EventHandler<ActionEvent> onBackEvent;
  private Button leftBtn;
  private Button rightBtn;
  private Button duckBtn;
  private Button jumpBtn;
  private Button basicBtn;
  private Button shurBtn;
  private Button tpBtn;
  private Button pauseBtn;
  private KeyBinding binding;

  AssetManager manager;

  public KeyBindingMenu() {
    manager = AssetManager.getInstance();
    binding = KeyBinding.getIntance();

    Font fontTitle = FontManager.loadFont(28);
    Font fontText = FontManager.loadFont(17);
    Font fontKey = FontManager.loadFont(16);

    Image bigFrame = manager.findImage("frame");

    this.setMaxSize(800, 500);
    this.setPadding(new Insets(10));
    this.setBackground(
        new Background(new BackgroundImage(bigFrame, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, new BackgroundSize(800, 500, false, false, false, false))));

    Label top = new Label("Controls");
    BorderPane center = new BorderPane();
    GridPane options = new GridPane();

    // Column 1
    // Left
    Label leftLbl = new Label("Left");
    leftBtn = new Button(binding.getBinding(KeyBinding.LEFT).toString());
    // Right
    Label rightLbl = new Label("Right");
    rightBtn = new Button(binding.getBinding(KeyBinding.RIGHT).toString());
    // Duck
    Label duckLbl = new Label("Duck");
    duckBtn = new Button(binding.getBinding(KeyBinding.DUCK).toString());
    // Jump / Select
    Label jumpLbl = new Label("Jump/Confirm");
    jumpBtn = new Button(binding.getBinding(KeyBinding.JUMP).toString());
    // Column 2
    // Basic Attack
    Label basicLbl = new Label("Attack");
    basicBtn = new Button(binding.getBinding(KeyBinding.ATTACK).toString());
    // Shuriken
    Label shurLbl = new Label("Shuriken");
    shurBtn = new Button(binding.getBinding(KeyBinding.SHURIKEN).toString());
    // Teleport
    Label tpLbl = new Label("Teleport");
    tpBtn = new Button(binding.getBinding(KeyBinding.TELEPORT).toString());
    // Pause
    Label pauseLbl = new Label("Pause");
    pauseBtn = new Button(binding.getBinding(KeyBinding.PAUSE).toString());
    HBox buttons = new HBox();
    Button defaultBtn = new Button("[ Default ]");
    Button backBtn = new Button("[  Back  ]");

    center.setMaxSize(700, 400);

    options.setHgap(10);

    options.add(leftLbl, 1, 1);
    options.add(leftBtn, 2, 1);
    options.add(rightLbl, 1, 2);
    options.add(rightBtn, 2, 2);
    options.add(duckLbl, 1, 3);
    options.add(duckBtn, 2, 3);
    options.add(jumpLbl, 1, 4);
    options.add(jumpBtn, 2, 4);
    options.add(basicLbl, 3, 1);
    options.add(basicBtn, 4, 1);
    options.add(shurLbl, 3, 2);
    options.add(shurBtn, 4, 2);
    options.add(tpLbl, 3, 3);
    options.add(tpBtn, 4, 3);
    options.add(pauseLbl, 3, 4);
    options.add(pauseBtn, 4, 4);

    center.setBackground(
        new Background(new BackgroundImage(bigFrame, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, new BackgroundSize(700, 400, false, false, false, false))));

    this.setTop(top);
    this.setCenter(center);

    center.setTop(options);
    center.setBottom(buttons);
    buttons.getChildren().addAll(defaultBtn, backBtn);
    buttons.setAlignment(Pos.CENTER);
    buttons.setSpacing(100);
    buttons.setPadding(new Insets(20));

    applyFont(top, fontTitle);
    applyKeyFont(leftLbl, fontKey);
    applyKeyFont(rightLbl, fontKey);
    applyKeyFont(duckLbl, fontKey);
    applyKeyFont(jumpLbl, fontKey);
    applyKeyFont(basicLbl, fontKey);
    applyKeyFont(shurLbl, fontKey);
    applyKeyFont(tpLbl, fontKey);
    applyKeyFont(pauseLbl, fontKey);

    applyButtonStyle(backBtn, fontText);
    applyButtonStyle(defaultBtn, fontText);
    applyKeyButton(leftBtn, fontKey);
    applyKeyButton(rightBtn, fontKey);
    applyKeyButton(duckBtn, fontKey);
    applyKeyButton(jumpBtn, fontKey);
    applyKeyButton(basicBtn, fontKey);
    applyKeyButton(shurBtn, fontKey);
    applyKeyButton(tpBtn, fontKey);
    applyKeyButton(pauseBtn, fontKey);

    BorderPane.setAlignment(center, Pos.CENTER);
    BorderPane.setAlignment(top, Pos.CENTER);
    top.setPadding(new Insets(10));

    options.setPadding(new Insets(30, 20, 0, 20));
    options.setVgap(25);

    backBtn.setOnAction((e) -> {
      if (onBackEvent != null) {
        onBackEvent.handle(e);
      }
    });

    defaultBtn.setOnAction((e) -> {
      playSelect();
      binding.reset();
      refreshText();
    });

    leftBtn.setOnKeyPressed(this::handleKeyPressed);
    rightBtn.setOnKeyPressed(this::handleKeyPressed);
    duckBtn.setOnKeyPressed(this::handleKeyPressed);
    jumpBtn.setOnKeyPressed(this::handleKeyPressed);
    basicBtn.setOnKeyPressed(this::handleKeyPressed);
    shurBtn.setOnKeyPressed(this::handleKeyPressed);
    tpBtn.setOnKeyPressed(this::handleKeyPressed);
    pauseBtn.setOnKeyPressed(this::handleKeyPressed);
    
    leftBtn.setOnAction(this::handleAction);
    rightBtn.setOnAction(this::handleAction);
    duckBtn.setOnAction(this::handleAction);
    jumpBtn.setOnAction(this::handleAction);
    basicBtn.setOnAction(this::handleAction);
    shurBtn.setOnAction(this::handleAction);
    tpBtn.setOnAction(this::handleAction);
    pauseBtn.setOnAction(this::handleAction);
  }
  
  private boolean onBinding = false;
  
  private void handleAction(ActionEvent e) {
    playMenuCursor();
    Button target = (Button) e.getSource();
    onBinding = !target.getText().equals("?");
    if(onBinding) {
      refreshText();
      target.setText("?");
    }
  }

  private void handleKeyPressed(KeyEvent e) {
    KeyCode changed = e.getCode();
    playSelect();

    if (e.getSource().equals(leftBtn)) {
      binding.setBinding(KeyBinding.LEFT, changed);
    } else if (e.getSource().equals(rightBtn)) {
      binding.setBinding(KeyBinding.RIGHT, changed);
    } else if (e.getSource().equals(duckBtn)) {
      binding.setBinding(KeyBinding.DUCK, changed);
    } else if (e.getSource().equals(jumpBtn)) {
      binding.setBinding(KeyBinding.JUMP, changed);
    } else if (e.getSource().equals(basicBtn)) {
      binding.setBinding(KeyBinding.ATTACK, changed);
    } else if (e.getSource().equals(shurBtn)) {
      binding.setBinding(KeyBinding.SHURIKEN, changed);
    } else if (e.getSource().equals(tpBtn)) {
      binding.setBinding(KeyBinding.TELEPORT, changed);
    } else if (e.getSource().equals(pauseBtn)) {
      binding.setBinding(KeyBinding.PAUSE, changed);
    }
    
    onBinding = false;
    this.requestFocus();
    
    refreshText();
  }

  private void refreshText() {
    binding.debug();
    
    leftBtn.setText(binding.getBinding(KeyBinding.LEFT).toString());
    rightBtn.setText(binding.getBinding(KeyBinding.RIGHT).toString());
    duckBtn.setText(binding.getBinding(KeyBinding.DUCK).toString());
    jumpBtn.setText(binding.getBinding(KeyBinding.JUMP).toString());
    basicBtn.setText(binding.getBinding(KeyBinding.ATTACK).toString());
    shurBtn.setText(binding.getBinding(KeyBinding.SHURIKEN).toString());
    tpBtn.setText(binding.getBinding(KeyBinding.TELEPORT).toString());
    pauseBtn.setText(binding.getBinding(KeyBinding.PAUSE).toString());
  }

  public void setOnBackEvent(EventHandler<ActionEvent> eventHandler) {
    this.onBackEvent = eventHandler;
  }

  private void applyFont(Labeled node, Font font) {
    node.setFont(font);
    node.setTextFill(Color.WHITE);
  }

  private void applyButtonStyle(Button button, Font font) {
    String defaultStyle = "-fx-background-color: transparent; " + "-fx-border-width: 3px; " + "-fx-border-radius: 5px; "
        + "-fx-text-fill: white; ";
    button.setFont(font);
    button.setStyle(defaultStyle + "-fx-border-color: transparent;");
    button.focusedProperty().addListener((obs, oldVal, newVal) -> {
      button.setStyle(defaultStyle + (newVal ? "-fx-border-color: #fcdc80;" : "-fx-border-color: transparent;"));
    });
    button.hoverProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        button.requestFocus();
        playMenuCursor();
      } else {
        button.getParent().requestFocus();
      }
    });
  }
  
  void playMenuCursor() {
    AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_cursor_8")).playThenDestroy();
  }
  
  void playSelect() {
    AudioFactory.createSfxHandler(manager.findAudio("sfx_menu_select_8")).playThenDestroy();
  }

  private void applyKeyButton(Button button, Font font) {
    button.setFont(font);
    button.setMinWidth(100);
  }

  private void applyKeyFont(Label label, Font font) {
    label.setFont(font);
    label.setMinWidth(200);
    label.setTextFill(Color.WHITE);
  }
}
