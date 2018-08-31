package ru.geekbrains.spacefly.screen.gamescreen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.spacefly.base.ActionListener;
import ru.geekbrains.spacefly.base.ScaledTouchUpButton;
import ru.geekbrains.spacefly.math.Rect;

public class ButtonNewGameReset extends ScaledTouchUpButton {
    private static final float TOP_MARGIN = 0.008f;

    public ButtonNewGameReset(TextureAtlas atlas, ActionListener actionListener, float pressScale) {
        super(atlas.findRegion("button_new_game"), actionListener, pressScale);
    }
//  //позиционируем кнопку
    @Override
    public void resize(Rect worldBounds) {
        setTop(TOP_MARGIN);
    }
}
