package ru.geekbrains.spacefly.screen.gamescreen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.base.Sprite;
import ru.geekbrains.spacefly.math.Rect;
import ru.geekbrains.spacefly.screen.pool.BulletPool;
import ru.geekbrains.spacefly.screen.pool.ExplosionPool;

public class Ship extends Sprite {

    //имитация нанесения урона с помощью таймера. Переключаемся между 2 фреймами
    //таймер тикает в бесконечность до тех пор пока не заходим переключить фрейм.
    //потом по достижении значения переключим обратно
    private static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;
    private float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;

    protected Vector2 v = new Vector2(); //вектор скорости
    //определяем в какой части экрана нажал юзер
    protected Rect worldBounds;

    protected BulletPool bulletPool;
    //зеленая/красная пуля
    protected TextureRegion bulletRegion;
    protected ExplosionPool explosionPool;

    private Sound sound;

    protected Vector2 bulletV = new Vector2();
    protected float bulletHeight;
    protected int bulletDamage;
    protected int hp;
    protected float reloadInterval; //для автоматической стрельбы
    protected float reloadTimer;

    public Ship(BulletPool bulletPool, ExplosionPool explosionPool, Sound sound, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.sound = sound;
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        damageAnimateTimer +=delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0; //нулевой фрейм, состояние когда в корабль ни кто не попал
        }
    }

    public Ship(TextureRegion region, int rows, int cols, int frames, Sound sound, ExplosionPool explosionPool) {
        super(region, rows, cols, frames);
        this.sound = sound;
        this.explosionPool = explosionPool;
    }

    //метод для заполнения
    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    protected void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, bulletHeight, worldBounds, bulletDamage);
        sound.play();


    }
    //метод взрывает корабль
    public void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }

    //состояние корабля. Передаем велечину урона
    public void damage(int damage) {
        frame = 1;// переключаем фрейм
        damageAnimateTimer = 0f; //обнуляем таймер, чтобы 1 фрейм подержался чуток
        hp -=damage; //наносим урон
        if (hp <= 0) {
            destroy();
        }
    }
    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

}
