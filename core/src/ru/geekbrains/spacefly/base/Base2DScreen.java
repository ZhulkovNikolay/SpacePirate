package ru.geekbrains.spacefly.base;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.math.MatrixUtils;
import ru.geekbrains.spacefly.math.Rect;


public class Base2DScreen implements Screen, InputProcessor {

    protected SpriteBatch batch;

    protected Game game;
    private Rect screenBounds; //границы экрана (области рисования) в пикселах
    protected Rect worldBounds; //границы проекции мировых координат
    private Rect glBounds; //дефолтные границы OpenGL

    private Matrix4 worldToGl; //будет утсанавливать батчер
    private Matrix3 screenToWorld; //с ней будем работать сами
    private Vector2 touch = new Vector2();

    public Base2DScreen(Game game) {
        this.game = game;
        Gdx.input.setInputProcessor(this); //все экраны стали inputprocessor'ами
        //все экраны могут принимать события пользователя
        this.screenBounds = new Rect();
        this.worldBounds = new Rect();
        this.glBounds = new Rect(0, 0, 1f, 1f);
        this.worldToGl = new Matrix4();
        this.screenToWorld = new Matrix3();

    }

    @Override
    public void show() {
        System.out.println("show");
        if (batch != null) {
            throw new RuntimeException("Повторная установка screen без dispose");
        }
        this.batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {

    }

    //границы мира приходят в метод ресайз
    @Override
    public void resize(int width, int height) {
        System.out.println("resize w = " + width + " h = " + height);
        screenBounds.setSize(width, height);
        screenBounds.setLeft(0); //смещаем в ноль
        screenBounds.setBottom(0);

        float aspect = width / (float) height; //считаем соотношение сторон
        worldBounds.setHeight(1f);
        worldBounds.setWidth(1f * aspect); // устанавливаем размер в зависимости от пропорций экрана
        // какую матрицу считаем, откуда куда проецируемся
        MatrixUtils.calcTransitionMatrix(worldToGl, worldBounds, glBounds);
        //при каждом ресайзе матрица преобразований будет пересчитываться, устанавливаться как матрица проекций
        batch.setProjectionMatrix(worldToGl);
        MatrixUtils.calcTransitionMatrix(screenToWorld, screenBounds, worldBounds); // посчитали матрицу преобразования
        resize(worldBounds);

    }


    public void  resize(Rect worldBounds) {

    }

    @Override
    public void pause() {
        System.out.println("pause");

    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
        System.out.println("hide");
        dispose();
    }

    @Override
    public void dispose() {
        System.out.println("dispose");
        batch.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown keycode = " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("keyUp keycode = " + keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        System.out.println("keyTyped keycode = " + character);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchDown screenX = " + screenX + " screenY = " + screenY);
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDown(touch,pointer);
        return false;
    }

    public boolean touchDown(Vector2 touch, int pointer) {
        System.out.println("touchDown touchX = " + touch.x + " touchY = " + touch.y);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchUp screenX = " + screenX + " screenY = " + screenY);
        //умножить на матрицу
        //после этого вектор touch будет не в пикселах, а вмировой системе координат
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchUp(touch, pointer);
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer) {
        System.out.println("touchUp touchX = " + touch.x + " touchY = " + touch.y);
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.println("touchDragged screenX = " + screenX + " screenY = " + screenY);
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDragged(touch, pointer);
        return false;
    }

    public boolean touchDragged(Vector2 touch, int pointer) {

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
