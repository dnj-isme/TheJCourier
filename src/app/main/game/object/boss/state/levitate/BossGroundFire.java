package app.main.game.object.boss.state.levitate;

import app.utility.canvas.*;

public class BossGroundFire extends GameObject implements Collidable, Updatable {
    private static BossGroundFire instance;
    public static BossGroundFire getInstance(GameScene owner) {
        if(instance == null) {
            instance = new BossGroundFire(owner);
        }
        return instance;
    }

    private BossGroundFire(GameScene owner) {
        super(owner);
        setLayer(ObjectLayer.Block);
        setSize(GameScene.WIDTH, 80);
    }

    @Override
    public void receiveCollision(GameObject object) {

    }

    @Override
    public void render(RenderProperties properties) {

    }

    @Override
    public void update(RenderProperties properties) {

    }

    @Override
    public void fixedUpdate(RenderProperties properties) {

    }
}
