package ru.geekbrains.spacefly.screen.menuscreen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.spacefly.base.ActionListener;
import ru.geekbrains.spacefly.base.ScaledTouchUpButton;
import ru.geekbrains.spacefly.math.Rect;

public class ButtonExit extends ScaledTouchUpButton {
    //atlas.findRegion("btExit") - сразу лезем в конфиг атласа и берем нужную текстуру
    public ButtonExit(TextureAtlas atlas, ActionListener actionListener, float pressScale) {
        super(atlas.findRegion("btExit"), actionListener, pressScale);

    }

    //позиционируем кнопку
    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom());
        setRight(worldBounds.getRight());
    }
}
