package app.main.view.game;

import java.util.TimerTask;

import app.main.controller.GameController;
import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.audio.AudioHandler;
import app.main.controller.scene.SceneController;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.main.game.scene.BossRoom;
import app.main.view.YouWinMenu;
import app.utility.Utility;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class BossScene extends GamePageTemplate {
  private static BossRoom room;
  private boolean win;
  private boolean easterEgg;
  private AudioHandler introMusic;
  private AudioHandler loopMusic;
  private boolean loopMode = false;
  private boolean paused = false;
  private boolean end = false;
  private GameController controller;

  public BossScene() {
    super(room = new BossRoom());
    controller = GameController.getInstance();
    getBase().setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    AssetManager asset = AssetManager.getInstance();
    introMusic = AudioFactory.createMusicHandler(asset.findAudio("bgm_boss_intro"), false);
    loopMusic = AudioFactory.createMusicHandler(asset.findAudio("bgm_boss"), true);
    introMusic.getPlayer().setOnEndOfMedia(() -> {
      loopMusic.play();
      introMusic.stop();
      loopMode = true;
    });

    AudioFactory.createSfxHandler(asset.findAudio("sfx_boss_laff")).playThenDestroy();
    Player.getInstance(getGameScene()).setOnDead(() -> {
      loopMusic.stop();
	  introMusic.stop();
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
  }

  @Override
  public void handleResume() {
    loopMusic.setVolume(controller.getMusic());
    introMusic.setVolume(controller.getMusic());
    if(loopMode) {
      loopMusic.play();
    }
    else {
      introMusic.play();
    }
  }

  @Override
  protected void handleStop() {
    loopMusic.stop();
    introMusic.stop();
  }

  @Override
  public void handleSceneKeyChanges(SceneEventObserver sceneEventObserver) {
    super.handleSceneKeyChanges(sceneEventObserver);
//    if(sceneEventObserver.isPressing(KeyCode.P) {
//      paused = true;
//      end = true;
//      Player.getInstance(getGameScene()).setDead();
//      Player.getInstance(getGameScene()).getOnDead().run();
//    }
    if(sceneEventObserver.isPressing(KeyBinding.getIntance().getBinding(KeyBinding.PAUSE)) && win) {
      room.stop();
      SceneController.getInstance().switchScene(new YouWinMenu());
      GameController.getInstance().setWinTime(room.getTimeSpent());
    }
  }
}
