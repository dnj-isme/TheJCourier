package app.main.game.object.boss.state.levitate;

import app.main.controller.asset.AssetManager;
import app.main.game.object.boss.Boss;
import app.main.game.object.boss.BossState;
import app.main.game.object.boss.state.BossIdleState;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BossLevitateCooldown extends BossState {

    private static BossLevitateCooldown instance;
    private final Boss boss;
    private final Vector2 imageSize;
    private long animStart;
    private boolean animFinished;
    private final Image sprite;

    public static BossLevitateCooldown load(Boss boss) {
        if (instance == null) {
            instance = new BossLevitateCooldown(boss);
        }
        return instance;
    }

    private BossLevitateCooldown(Boss boss) {
        super(boss);

        this.boss = boss;
        AssetManager assets = AssetManager.getInstance();
        sprite = assets.findImage("boss_spawn");
        imageSize= Vector2.square(120);
        reset();
    }

    public void reset() {
        animStart = -1;
        animFinished = false;
    }

    @Override
    protected void render(RenderProperties properties) {
        if(animStart == -1) return;
        GraphicsContext context = properties.getContext();

        long index = (long) ((properties.getFrameCount() - animStart) / 2.5);
        if(index > 5) {
            index = 5;
            animFinished = true;
        }
        double xPos = 120 * index;
        Vector2 renderPos = Vector2.renderBottomCenter(boss.getPosition(), boss.getSize(), imageSize);
        context.drawImage(sprite, xPos, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(), renderPos.getY(), imageSize.getX(), imageSize.getY());
    }

    @Override
    public void update(RenderProperties properties) {
        if(animStart == -1) {
            animStart = properties.getFrameCount();
            boss.setPosition(
                    (GameScene.WIDTH - boss.getSize().getX())/2,
                    (GameScene.HEIGHT - boss.getSize().getY() - 20)
            );
        }
        if(animFinished) {
            BossIdleState state = BossIdleState.load(boss);
            state.reset();
            boss.setState(state);
        }
    }

    @Override
    public void fixedUpdate(RenderProperties properties) {

    }
}
