package ru.geekbrains.spacefly.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
//логика нажатия кнопки

public class ScaledTouchUpButton extends Sprite {
    private int pointer;
    private boolean pressed;
    private float pressScale;
    private ActionListener actionListener;

    public ScaledTouchUpButton(TextureRegion region, ActionListener actionListener, float pressScale) {
        super(region);
        this.pressScale = pressScale;
        this.actionListener = actionListener;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        // проверяем нажата ли уже и попал ли юзер по кнопке
        if (pressed || !isMe(touch)) {
            return false;
        }
        this.pointer = pointer;//запоминаем палец
        this.scale = pressScale; //присваиваем новое значение pressScale, типа новое состояние нажатия
        this.pressed = true;
        return true;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        //проверяем тот ли палец был убран или кнопка не та что была нажата
        if (this.pointer != pointer || !pressed) {
            return false;
        }
        //убрал ли пользователь палец с кнопки - тогда сработает. Если отвел, то нет.
        if (isMe(touch)) {
            //класс релизующий данный интерфес сам решит что с этим делать
            actionListener.actionPerformed(this);
            return true;
        }
        pressed = false;
        scale = 1f;
        return true;
    }
}
