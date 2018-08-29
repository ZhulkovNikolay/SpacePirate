package ru.geekbrains.spacefly.screen.gamescreen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.base.Sprite;
import ru.geekbrains.spacefly.math.Rect;
import ru.geekbrains.spacefly.screen.pool.BulletPool;

public class Ship extends Sprite {
    protected Vector2 v = new Vector2(); //вектор скорости
    //определяем в какой части экрана нажал юзер
    protected Rect worldBounds;

    protected BulletPool bulletPool;
    //зеленая/красная пуля
    protected TextureRegion bulletRegion;

    private Sound sound;

    protected Vector2 bulletV = new Vector2();
    protected float bulletHeight;
    protected int bulletDamage;

    public Ship(TextureRegion region, int rows, int cols, int frames, Sound sound) {
        super(region, rows, cols, frames);
        this.sound = sound;
    }

    //метод для заполнения
    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos,bulletV, bulletHeight, worldBounds, bulletDamage);
        sound.play();


    }
}
