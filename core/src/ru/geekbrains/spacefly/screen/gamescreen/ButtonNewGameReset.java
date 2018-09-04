package ru.geekbrains.spacefly.screen.gamescreen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.spacefly.base.ActionListener;
import ru.geekbrains.spacefly.base.ScaledTouchUpButton;

public class ButtonNewGameReset extends ScaledTouchUpButton {
    private static final float HEIGHT = 0.05f;
    private static final float TOP = -0.012f;
    private static final float PRESS_SCALE = 0.9f; // на сколько кнопка уменьшится при нажатии на 10%

    public ButtonNewGameReset(TextureAtlas atlas, ActionListener actionListener) {
        super(atlas.findRegion("button_new_game"), actionListener, PRESS_SCALE);
        setHeightProportion(HEIGHT); //размер кнопки
        setTop(TOP);  //позиционируем кнопку
    }
}
