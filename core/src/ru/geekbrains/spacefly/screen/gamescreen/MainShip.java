package ru.geekbrains.spacefly.screen.gamescreen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.math.Rect;
import ru.geekbrains.spacefly.screen.pool.BulletPool;

public class MainShip extends Ship {

    private static final float SHIP_HEIGHT = 0.15f;
    // отступ от экрана при позиционировании корабля
    private static final float BOTTOM_MARGIN = 0.05f;
    //для исправления бага с мультитачем. Никакой палец:
    private static final int INVALID_POINTER = -1;

    private Vector2 v0 = new Vector2(0.5f, 0.0f);//начальная скорость по х и у

    //чтобы корабль не тормозил когда юзер отпускает кнопки
    private boolean pressedLeft;
    private boolean pressedRight;

    //храним отдельно левый/правый палец
    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    //2 состояния корабля
    public MainShip(TextureAtlas atlas, BulletPool bulletPool, Sound sound) {
        super(atlas.findRegion("main_ship"), 1, 2, 2,sound);
        setHeightProportion(SHIP_HEIGHT);
        //смотрим в атласе нужную пулю
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletHeight = 0.02f;
        this.bulletV.set(0, 0.5f);
        this.bulletDamage = 1;
        this.bulletPool = bulletPool;
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
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
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
            case Input.Keys.UP:
                shoot();
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
    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (touch.x < worldBounds.pos.x) {
            //перед тем как двигаться проверяем нажимал или отпустил
            if (leftPointer != INVALID_POINTER) return false;
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) return false;
            rightPointer = pointer; //если юзер до этого палец не держал, а только-только нажал
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            //проверяем вдруг юзер держит второй палец
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                //если юзер отпустил палец
                stop();
            }
        }
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
