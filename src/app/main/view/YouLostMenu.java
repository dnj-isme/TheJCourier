package app.main.view;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.view.game.DeveloperScene;
import app.main.view.game.SpawnScene;
import app.utility.SceneTemplate;
import app.utility.Utility;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class YouLostMenu extends SceneTemplate {
  
  private static int deathCount = 0;

  private static String[] motivation = { 
      "If all else fails, you could try jumping.",
      "Zigged when you should have zagged?", 
      "I totally called that one! I just won another bet.",
      "Or you could have watched your step.", 
      "Admit it, you did that just because you wanted to see me!",
      "The first paycheck always feels good,\nbut at this point I mostly see the work.",
      "Living outside of time, I tend to lose track.\nHave we met before from your perspective?",
      "Perfect timing! It was my turn to buy a round.",
      "Just so you can't say I'm always negative:\nGo Player! You can do it!",
      "No, I don't want to take a selfie with you.", 
      "Why do I feel like you meant to do that?",
      "If anyone asks, I'm taken.", 
      "Are you playing with your feet or something?",
  };
  
  private static String[] roast = {
      "I could do this all day, which is good\nbecause it looks like I will.",
      "Not that I'd ever give up on my best client,\n but I could use a break.",
      "Looks like I'll be canceling that summer vacation.",
      "The money's good, but I fear I might actually burn out.",
      "At this point you could consider thanking me.",
      "Can someone contact the director and explain to me\nhow my actions don't justify a main role yet?",
      "I think that's the highest tab I have ever seen.",
      "Can I interest you in a payment plan?",
      "Thanks for making me so productive, I got employee of the month!",
      "I used to be the poorest of all greed demons.\nYou really turned my life around!",
      "Seriously, you're a gold mine! I'm not even declaring\nhalf of this revenue.",
      "Are you really that bad, or are you testing my patience?",
      "Ow! My magic ring is overheating!",
      "Maybe I should rent my ring to you directly for a fixed price instead.",
      "Since it looks like I'll be doing most of the work,\ndo you want me to carry the scroll for you?",
      "Are you sure about this mission? You don't look like\nyou're in your element.",
      "Someone give me a tips: \"Jangan keseringan main,\nkerjain BP nya\". Don't know what that means",
      "\"Main aja Ampas, apa lagi BP?\", from someone wise",
      "Another quote from the wise man:\n\"Skill boleh ampas, tapi BP jangan!!\" - MC22-2",
  };
  
  private AssetManager assets;
  
  private Image quarbleIdle;
  private Image quarbleIdleWing;

  private VBox base;
    private Label label;
    private Canvas quarble;
    private VBox buttons;
      private Button retryButton;
      private Button mainMenuButton;

  @Override
  public Node initComponents() {
    assets = AssetManager.getInstance();
    quarbleIdle = assets.findImage("quarble_idle");
    quarbleIdleWing = assets.findImage("quarble_wing");
    
    deathCount++;
    
    String message = "";
    if(Utility.chance(deathCount * 2)) {
      message = roast[Utility.random(0, roast.length-1)];
    }
    else {
      message = motivation[Utility.random(0, motivation.length-1)];
    }
    deathCount++;
    
    base = new VBox(
      quarble = new Canvas(),
      label = new Label(message),
      buttons = new VBox(
        retryButton = new Button("Replay"), 
        mainMenuButton = new Button("Main Menu")
      )
    );
    
    new AnimationTimer() {
      
      @Override
      public void handle(long now) {
        handleAnimation(now);
      }
    }.start();
    
    label.setPadding(new Insets(50, 0, 20, 0));

    quarble.setHeight(90);
    quarble.setWidth(90);
    quarble.getGraphicsContext2D().setImageSmoothing(false);
    
    fps = 20;
    timeEachFrame = 1.0 / fps;
    frameCount = 0;
    
    return base;
  }

  private long fps;
  private double timeEachFrame;

  private double startFrameTime;
  private double current;

  private double lastFrameTime;

  private int frameCount; 
  
  private void handleAnimation(long now) {
    if (startFrameTime <= 0) {
      startFrameTime = now / 1_000_000_000.0;
    }

    current = (now / 1_000_000_000.0) - startFrameTime;
    
    double deltaTime = current - lastFrameTime;

    if (deltaTime < timeEachFrame) {
      return;
    }
    
    int index = frameCount % 8;
    GraphicsContext context = quarble.getGraphicsContext2D();
    context.clearRect(0, 0, quarble.getWidth(), quarble.getHeight());
    context.drawImage(quarbleIdleWing, index * 30, 0, 30, 30, 0, 0, quarble.getWidth(), quarble.getHeight());
    context.drawImage(quarbleIdle, index * 30, 0, 30, 30, 0, 0, quarble.getWidth(), quarble.getHeight());
    
    frameCount++;
    lastFrameTime = current;
  }

  @Override
  public void initStyle() {
    Font font24 = FontManager.loadFont(24);
    Font font16 = FontManager.loadFont(16);
    
    base.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    base.setAlignment(Pos.CENTER);
    base.setSpacing(5);
    
    applyFont(label, font24);
    
    buttons.setAlignment(Pos.CENTER);
    buttons.setSpacing(5);
    
    applyButtonStyle(retryButton, font16);
    applyButtonStyle(mainMenuButton, font16);
  }

  @Override
  public void eventHandling() {
    retryButton.setOnMouseClicked((e) -> {
      AudioFactory.createSfxHandler(AssetManager.getInstance().findAudio("sfx_menu_select_16")).playThenDestroy();
      SceneController.getInstance().switchScene(GameController.getInstance().isDebug() ? new DeveloperScene() : new SpawnScene());
    });

    mainMenuButton.setOnMouseClicked((e) -> {
      AudioFactory.createSfxHandler(AssetManager.getInstance().findAudio("sfx_menu_select_16")).playThenDestroy();
      SceneController.getInstance().switchScene(new MainMenu());
    });
  }

  @Override
  public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {
    
  }

  private void applyFont(Labeled node, Font font) {
      node.setFont(font);
      node.setTextFill(Color.WHITE);
      node.setTextAlignment(TextAlignment.CENTER);
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
