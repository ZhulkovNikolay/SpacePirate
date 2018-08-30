package ru.geekbrains.spacefly.base;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.math.Rect;
import ru.geekbrains.spacefly.utils.Regions;

public class Sprite extends Rect {

    protected float angle; //на сколько можем повернуть картинку
    protected float scale = 1f; // на сколько можем скаллировать картинку
    protected TextureRegion[] regions;
    protected int frame;
    protected boolean isDestroyed = false;

    public Sprite(TextureRegion region) {
        if (region == null) {
            throw new RuntimeException("region == null");
        }
        regions = new TextureRegion[1];
        regions[0] = region;
    }

    //конструктор обозначает помечен ли объект на удаление с экрана
    public Sprite() {

    }

    //дополнительный конструктор для корабля и взрывов (для нарезания)
    //нам нужно разрезать текстуру корабля на 2 части = 2 его состояния
    public Sprite(TextureRegion region, int rows, int cols, int frames) {
        this.regions = Regions.split(region, rows, cols, frames);


    }


    //текущий регион, точка отрисовки, точка вращения, ширина и высота, масштаб по х у, угол вращения
    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                halfWidth, halfHeight,
                getWidth(), getHeight(),
                scale, scale,
                angle);
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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    //убираем в массив, где будет хранится до следующего использования
    public void destroy(){
        this.isDestroyed = true;
    }
    public void flushDestroy() {
        this.isDestroyed = false;
    }

    //возвращает состояние пули
    public boolean isDestroyed() {
        return isDestroyed;
    }
}
