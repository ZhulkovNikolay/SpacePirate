package ru.geekbrains.spacefly.screen.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.base.Sprite;
import ru.geekbrains.spacefly.math.Rect;
import ru.geekbrains.spacefly.math.Rnd;

public class Star extends Sprite {
    protected Vector2 v = new Vector2(); // вектор скорости
    private Rect worldBounds;

    public Star(TextureAtlas atlas) {
        super(atlas.findRegion("star")); //смотрим название региона в конфиге атласа
        v.set(Rnd.nextFloat(-0.105f, 0.205f), Rnd.nextFloat(-0.05f, -0.7f)); //задаем скорость по оси х и у
        setHeightProportion(0.01f); //1% от экрана занимает звезда
    }

    //движение звезды. сложение векторов
    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);//складывает вектора и умножает на скаляр
        checkAndHandleBounds();
    }

    protected void checkAndHandleBounds() {
        if (getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        // если звезда улетела через правую границу мира, то возвращаем ее через левую
        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (getTop() < worldBounds.getBottom()) setBottom(worldBounds.getTop());

    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        // super.resize(worldBounds);
        //расчитываем новую позицию звезды после ухода за экран
        //звезда по оси Х не вылезет за пределы экрана
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        //укстанавливааем позицию нашей звезды
        pos.set(posX, posY);

    }
}
