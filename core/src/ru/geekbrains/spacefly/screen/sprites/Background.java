package ru.geekbrains.spacefly.screen.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.spacefly.base.Sprite;
import ru.geekbrains.spacefly.math.Rect;

public class Background extends Sprite {
    public Background(TextureRegion region) {
        super(region);

    }
    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight()); // растягиваем по высоте экрана
        pos.set(worldBounds.pos); //фон отрисован из центра
    }



}
