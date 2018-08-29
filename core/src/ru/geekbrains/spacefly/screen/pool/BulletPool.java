package ru.geekbrains.spacefly.screen.pool;

import ru.geekbrains.spacefly.base.SpritesPool;
import ru.geekbrains.spacefly.screen.gamescreen.Bullet;

public class BulletPool extends SpritesPool<Bullet> {
    //если пуль будет не хваттать, мы их создадим
    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    @Override
    protected void debugLog() {
        System.out.println("active/free: " + activeObjects.size() + "/" + freeObjects.size());
    }
}
