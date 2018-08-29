package ru.geekbrains.spacefly.screen.menuscreen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import ru.geekbrains.spacefly.base.ActionListener;
import ru.geekbrains.spacefly.base.ScaledTouchUpButton;
import ru.geekbrains.spacefly.math.Rect;

public class ButtonNewGame extends ScaledTouchUpButton {

    public ButtonNewGame(TextureAtlas atlas, ActionListener actionListener, float pressScale) {
        super(atlas.findRegion("btPlay"), actionListener, pressScale);

    }
    //позиционируем кнопку
    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom());
        setLeft(worldBounds.getLeft());
    }
}
