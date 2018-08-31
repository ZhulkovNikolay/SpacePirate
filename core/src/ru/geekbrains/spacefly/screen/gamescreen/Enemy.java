package ru.geekbrains.spacefly.screen.gamescreen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.math.Rect;
import ru.geekbrains.spacefly.screen.pool.BulletPool;
import ru.geekbrains.spacefly.screen.pool.ExplosionPool;
import ru.geekbrains.spacefly.utils.EnemyEmitter;

public class Enemy extends Ship {
    private enum State {DESCENT, FIGHT} //состояние выползающего корабля

    private State state;
    private Vector2 descentV = new Vector2(0, -0.15f);

    //3 текстуры для врагов.
    //с разной вероятностью генерируем
    private MainShip mainShip;
    private Vector2 v0 = new Vector2();

    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Sound sound, MainShip mainShip, Rect worldBounds) {
        super(bulletPool, explosionPool, sound, worldBounds);
        this.mainShip = mainShip;
        this.v.set(v0);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta); //вылетает корабль
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }

        if (getBottom() < worldBounds.getBottom()) {
            //если корабль вылетает за низ
            boom();
            destroy();
        }

        switch (state) {
            //если корабль выехал на экран устанавливпаем ему боевую скорость
            case DESCENT:
                if (getTop() <= worldBounds.getTop()) {
                    v.set(v0);
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                reloadTimer += delta;
                if (reloadTimer >= reloadInterval) {
                    reloadTimer = 0f;
                    shoot();
                }
                if (getBottom() < worldBounds.getBottom()) {
                    //если корабль вылетает за низ
                    mainShip.damage(bulletDamage); //наносится урон игроку
                    destroy();
                }
                break;
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int bulletDamage,
            float reloadInterval, //для автоматической стрельбы
            float height,
            int hp
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0f, bulletVY);
        this.bulletDamage = bulletDamage;
        this.reloadInterval = reloadInterval;
        this.hp = hp;
        setHeightProportion(height);
        v.set(descentV);
        reloadTimer = reloadInterval; //корабль вылетает и сразу стреляет
        state = State.DESCENT; // сперва корабль не в режиме боя
    }

    //у врагов прямоугольники. Пуля попадает не долетая корабля. Теперь хит бокс в центр корабля
    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y
        );
    }

}
