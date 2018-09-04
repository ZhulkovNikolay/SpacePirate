package ru.geekbrains.spacefly.screen.gamescreen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.screen.sprites.Star;

//отслеживает движение звезд
public class TrackingStar extends Star {

    private Vector2 trackingV;
    private Vector2 sumV = new Vector2();

    public TrackingStar(TextureAtlas atlas, Vector2 trackingV) {
        super(atlas);
        this.trackingV = trackingV;
    }

    @Override
    public void update(float delta) {
        sumV.setZero().mulAdd(trackingV, 0.2f).rotate(180).add(v);//движение звезд 20% от скорсоти корабля
        pos.mulAdd(sumV, delta);
        checkAndHandleBounds();
    }
}
