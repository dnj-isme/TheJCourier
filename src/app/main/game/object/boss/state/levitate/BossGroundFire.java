package app.main.game.object.boss.state.levitate;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.utility.canvas.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class BossGroundFire extends GameObject implements Collidable, Updatable {
    private static BossGroundFire instance;
    private final Image leftSwordBurnLoop;
    private final Image leftSwordBurnStart;
    private final Image rightSwordBurnLoop;
    private final Image rightSwordBurnStart;
    private GameController gameController;

    public static BossGroundFire getInstance(GameScene owner) {
        if(instance == null) {
            instance = new BossGroundFire(owner);
        }
        return instance;
    }

    private final Image fireCenter;
    private final Image fireCorner;
    private boolean active = false;
    private double startAnim = -1;
    private boolean finished = false;

    public boolean isActive() {
        return active;
    }

    public void activate() {
        active = true;
    }

    private BossGroundFire(GameScene owner) {
        super(owner);
        setLayer(ObjectLayer.Block);
        setTag(ObjectTag.Enemy);
        setSize(GameScene.WIDTH, 40);
        setPosition(0, GameScene.HEIGHT - 70);

        AssetManager assets = AssetManager.getInstance();
        fireCenter = assets.findImage("fire_center");
        fireCorner = assets.findImage("fire_corner");
        leftSwordBurnLoop = assets.findImage("left_sword_burn_loop");
        leftSwordBurnStart = assets.findImage("left_sword_burn_start");        
        rightSwordBurnLoop = assets.findImage("right_sword_burn_loop");
        rightSwordBurnStart = assets.findImage("right_sword_burn_start");
        gameController = GameController.getInstance();
        reset(owner);
    }

    public void reset(GameScene owner) {
        setOwner(owner);
        active = false;
        startAnim = -1;
        finished = false;
    }

    @Override
    public void receiveCollision(GameObject object) {

    }

    @Override
    public void render(RenderProperties properties) {
        if(!active) return;
        GraphicsContext context = properties.getContext();
        if(gameController.isHitbox()) {
            context.setFill(Color.RED);
            Vector2 position = getPosition();
            Vector2 size = getSize();
            context.fillRect(position.getX(), position.getY(), size.getX(), size.getY());
        }

        int index = (int) ((properties.getFrameCount() - startAnim) / 2.5);
        if(!finished) {
            if(index >= 4) {
                index = index % 4 + 4;
            }
        }
        else {
            index = 3 - index;
            if(index <= -1) {
                active = false;
                return;
            }
        }

        double yPos = index > 3 ? 40 : 0;
        double xPos = (index % 4) * 30;

        Vector2 basePosition = getPosition().copy();
        int len = (int) (GameScene.WIDTH / 40 - 1);
        for(int i = 0; i < len; i++) {
            if(i == 0) {
                context.drawImage(fireCorner, xPos, yPos, 30, 40, basePosition.getX(), basePosition.getY(), 40, 40);
            }
            if(i == len - 1) {
                context.drawImage(fireCorner, xPos, yPos, 30, 40, GameScene.WIDTH, basePosition.getY(), -40, 40);
            }
            else {
                context.drawImage(fireCenter, xPos, yPos, 40, 40, basePosition.getX() + (1 + i) * 40, basePosition.getY(), 40, 40);
            }
        }

        xPos = (index % 4) * 50;
        Image leftSword = index < 4 ? leftSwordBurnStart : leftSwordBurnLoop;
        Image rightSword = index < 4 ? rightSwordBurnStart : rightSwordBurnLoop;
        Vector2 size40 = Vector2.square(40);
        Vector2 size50 = Vector2.square(50);
        Vector2 swordLeftPos = Vector2.renderLeftCenter(basePosition, size40, size50);
        Vector2 swordRightPos = Vector2.renderRightCenter(new Vector2(GameScene.WIDTH - 40, getPosition().getY()), size40, size50);

        context.drawImage(leftSword, 0, xPos, 50, 50, swordLeftPos.getX(), swordLeftPos.getY(), 50, 50 );
        context.drawImage(rightSword, 0, xPos, 50, 50, swordRightPos.getX(), swordRightPos.getY(), 50, 50 );
    }

    @Override
    public void update(RenderProperties properties) {
        if(!active) return;
        if (startAnim == -1) {
            startAnim = properties.getFrameCount();
        }
    }

    @Override
    public void fixedUpdate(RenderProperties properties) {

    }

    public void deactivate() {
        finished = true;
    }
}
