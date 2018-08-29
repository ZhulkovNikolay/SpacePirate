package ru.geekbrains.spacefly.screen.gamescreen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.base.Sprite;
import ru.geekbrains.spacefly.math.Rect;

public class MainShip extends Sprite {

    private static final float SHIP_HEIGHT = 0.15f;
    // отступ от экрана при позиционировании корабля
    private static final float BOTTOM_MARGIN = 0.05f;
    private Vector2 v = new Vector2(); //вектор скорости
    private Vector2 v0 = new Vector2(0.5f, 0.0f);//начальная скорость по х и у

    //чтобы корабль не тормозил когда юзер отпускает кнопки
    private boolean pressedLeft;
    private boolean pressedRight;

    //определяем в какой части экрана нажал юзер
    private Rect worldBounds;

    //2 состояния корабля
    public MainShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        setHeightProportion(SHIP_HEIGHT);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        //если крайняя правая точка корабля правее границ
        //останавливаем корабль
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
        this.worldBounds = worldBounds;
    }

    //корабль сам знает как собой управлять
    public void keyDown(int keycode) {
        //взависимости от кейкода который пришел, выполняем действиек
        switch (keycode) {
            //будет выполнен и в случае влево и в случае А
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
        }
    }

    public void keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
    }

    //в какой части экрана пользователь нажал пальцем
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x < worldBounds.pos.x) {
            moveLeft();
        } else {
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        //если юзер отпустил палец
        stop();
        return false;
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180); //разворачивает вектор
    }

    private void stop() {
        v.setZero(); //обнуляет вектор
    }
}
