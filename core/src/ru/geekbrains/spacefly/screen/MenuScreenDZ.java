package ru.geekbrains.spacefly.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.base.Base2DScreen;

//экран меню
public class MenuScreenDZ extends Base2DScreen {
    private static final float SPEED = 1f;
    SpriteBatch batch;
    Texture background;
    Texture ship;

    Vector2 pos;
    Vector2 v;

    Vector2 touch;
    Vector2 buf; // буферный вектор для обхода утечки памяти в рендере
    public MenuScreenDZ(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        background = new Texture("sky.jpg");
        ship = new Texture("ship.png");
        pos = new Vector2(0, 0);
        v = new Vector2(0, 0);
        touch = new Vector2();
        buf = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        buf.set(touch);
        //вызываем cpy(), чтобы touch никак не менялся
        if (buf.cpy().sub(pos).len() > SPEED) {
            //если лететь далеко, выполняем сложение векторов
            pos.add(v);
        } else {
            // здесь полагаем что нам остался 1 шаг. Мы помещаем корабль в конечную точку
            pos.set(touch);
            v.setZero();
        }
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(ship, pos.x, pos.y);
        batch.end();
        pos.add(v); //с каждым срабатыванием рендер картинка отрисовывается в новом месте
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        background.dispose();
        ship.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);// инвентируем экран
        // вычитание векторов. Указывает на метсо из точки отрисовывания корабля в точку нажатия
        //движение реализуется с помощью сложения векторов
        //уменьшаем вектор на значение равной скорости, чтобы картинка не прыгала
        //получается вектор скорости
        //чтобы не передавать ссылеку на объект, а сделать отдельный объект скорости
        //иначе v и touch будут указывать на одно и то же
        //вызовем cpy() - скопирует результат
        v.set(touch.cpy().sub(pos).setLength(SPEED));
        System.out.println("touchPos.x = " + touch.x + " touchPos.y = " + touch);
        return false; // нужно ли дальше обрабатывать это событие
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        super.touchUp(screenX, screenY, pointer, button);
        return false;
    }
}
