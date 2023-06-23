package app.main.view.game;

import java.util.TimerTask;

import app.main.controller.GameController;
import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.audio.AudioHandler;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.boss.Boss;
import app.main.game.object.player.Player;
import app.main.game.scene.BossRoom;
import app.main.game.scene.SpawnRoom;
import app.main.view.YouWinMenu;
import app.utility.SceneTemplate;
import app.utility.Utility;
import app.utility.canvas.GameScene;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class BossScene extends GamePageTemplate {
  private static BossRoom room;
  private boolean win;
  private boolean easterEgg;
  private AudioHandler introMusic;
  private AudioHandler loopMusic;
  private boolean loopMode = false;
  private boolean paused = false;
  private boolean end = false;

  public BossScene() {
    super(room = new BossRoom());
    getBase().setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    AssetManager asset = AssetManager.getInstance();
    introMusic = AudioFactory.createMusicHandler(asset.findAudio("bgm_boss_intro"), false);
    loopMusic = AudioFactory.createMusicHandler(asset.findAudio("bgm_boss"), true);
    introMusic.getPlayer().setOnEndOfMedia(() -> {
      loopMusic.play();
      loopMode = true;
    });

    AudioFactory.createSfxHandler(asset.findAudio("sfx_boss_laff")).playThenDestroy();
    Player.getInstance(getGameScene()).setOnDead(() -> {
      loopMusic.getPlayer().stop();
      introMusic.getPlayer().stop();
    });

    Platform.runLater(() -> {
      Utility.delayAction(2000, new TimerTask() {
        @Override
        public void run() {
          if(!paused && !end) {
            introMusic.play();
          }
        }
      });
    });
    loopMode = false;
    easterEgg = Utility.chance(GameController.getInstance().isDebug() ? 50 : 5);
    win = false;
    room.setOnWin(() -> {
      win = true;
    });
    room.setOnStop(() -> {
      handlePause();
    });

    getBase().setAlignment(Pos.CENTER);
  }

  @Override
  public void handlePause() {
    paused = true;
    if (loopMode) {
      loopMusic.pause();
    } else {
      introMusic.pause();
    }
    System.out.println(room.getTimeSpentFormat());
  }

  @Override
  public void handleResume() {
    if (loopMode) {
      loopMusic.play();
    } else {
      introMusic.play();
    }
  }

  @Override
  public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {
    super.handleSceneKeyChanges(sceneEventObserver);
    if(sceneEventObserver.isPressing(KeyCode.P)) {
      paused = true;
      end = true;
      Player.getInstance(getGameScene()).setDead();
      Player.getInstance(getGameScene()).getOnDead().run();
    }
    if(sceneEventObserver.isPressing(KeyBinding.getIntance().getBinding(KeyBinding.PAUSE)) && win) {
      room.stop();
      SceneController.getInstance().switchScene(new YouWinMenu());
      GameController.getInstance().setWinTime(room.getTimeSpent());
    }
  }
}
