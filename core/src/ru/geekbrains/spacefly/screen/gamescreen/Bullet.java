package ru.geekbrains.spacefly.screen.gamescreen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.base.Sprite;
import ru.geekbrains.spacefly.math.Rect;

public class Bullet extends Sprite {

    //пуля должна знать границы экрана
    private Rect worldBounds;
    private final Vector2 v = new Vector2();
    private int damage;
    private Object owner; // траектория полета

    public Bullet() {
        regions = new TextureRegion[1];
    }

    //владелец, красная/зеленая, откуда пуля летит, скорость нач,
    // размер, границы мира, урон

    public void set(Object owner,
                    TextureRegion region,
                    Vector2 pos0,
                    Vector2 v0,
                    float height,
                    Rect worldBounds,
                    int damage
    ) {
        this.owner = owner;
        this.regions[0] = region;
        this.pos.set(pos0);
        this.v.set(v0);
        setHeightProportion(height);
        this.worldBounds = worldBounds;
        this.damage = damage;
    }

    @Override
    public void update(float delta) {
        this.pos.mulAdd(v,delta);
        //если пуля за пределами экрана, помечаем на удаление
        if(isOutside(worldBounds)) {
        destroy();
        }

    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }
}
