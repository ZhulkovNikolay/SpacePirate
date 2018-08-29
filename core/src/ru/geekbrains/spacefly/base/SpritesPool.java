package ru.geekbrains.spacefly.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

//пул любых графических объектов
public abstract class SpritesPool<T extends Sprite> {
    //список активных объектов
    protected List<T> activeObjects = new ArrayList<T>();
    //список свободных бъектов
    protected List<T> freeObjects = new ArrayList<T>();

    protected abstract T newObject(); //метод создает новый объект

    public T obtain() {
        T object;
        if (freeObjects.isEmpty()) {
            object = newObject();
        } else {
            object = freeObjects.remove(freeObjects.size() - 1);
        }
        activeObjects.add(object);
        //добавим логирование
        debugLog();
        return object;
    }

    public void updateActiveSprites(float delta) {
        for (int i = 0; i < activeObjects.size() ; i++) {
            Sprite sprite = activeObjects.get(i);
            if (!sprite.isDestroyed) {
                sprite.update(delta);
            }
        }
    }

    public void freeAllDestroyedActiveSprites() {
        for (int i = 0; i < activeObjects.size() ; i++) {
            T sprite = activeObjects.get(i);
            if (sprite.isDestroyed) {
                free(sprite);
                i--;
                sprite.flushDestroy();
            }
        }
    }

    //метод для прорисоки всех активных объектов
    public void drawActiveSprites(SpriteBatch batch) {
        for (int i = 0; i < activeObjects.size() ; i++) {
            Sprite sprite = activeObjects.get(i);
            if (!sprite.isDestroyed) {
                sprite.draw(batch);
            }
        }
    }

    private void free(T object) {
        if (!activeObjects.remove(object))
            freeObjects.add(object);
        debugLog();
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    public void dispose() {
        activeObjects.clear();
        freeObjects.clear();
    }

    protected void debugLog() {

    }
}
