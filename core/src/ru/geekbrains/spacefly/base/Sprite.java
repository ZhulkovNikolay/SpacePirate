package ru.geekbrains.spacefly.base;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.math.Rect;
import ru.geekbrains.spacefly.utils.Regions;

public class Sprite extends Rect {

    protected float angel; //на сколько можем повернуть картинку
    protected float scale = 1f; // на сколько можем скаллировать картинку
    protected TextureRegion[] regions;
    protected int frame;

    public Sprite(TextureRegion region) {
        if (region == null) {
            throw new RuntimeException("region == null");
        }
        regions = new TextureRegion[1];
        regions[0] = region;
    }

    //дополнительный конструктор для корабля и взрывов (для нарезания)
    //нам нужно разрезать текстуру корабля на 2 части = 2 его состояния
    public Sprite(TextureRegion region, int rows, int cols, int frames) {
        this.regions = Regions.split(region, rows, cols, frames);


    }


    //текущий регион, точка отрисовки, точка вращения, ширина и высота, масштаб по х у, угол вращения
    public void draw(SpriteBatch batch) {
        batch.draw(regions[frame], getLeft(), getBottom(), halfWidth, halfHeight, getWidth(), getHeight(), scale, scale, angel);
    }

    //вычисляем размер объекта относительно размеров экрана
    public void setHeightProportion(float height) {
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);

    }

    public void resize(Rect worldBounds) {

    }

    public void update(float delta) {

    }

    public boolean touchDown(Vector2 touch, int pointer) {

        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer) {

        return false;
    }

    public float getAngel() {
        return angel;
    }

    public void setAngel(float angel) {
        this.angel = angel;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
