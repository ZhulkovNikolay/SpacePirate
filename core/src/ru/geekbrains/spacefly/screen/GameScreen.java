package ru.geekbrains.spacefly.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.base.Base2DScreen;
import ru.geekbrains.spacefly.math.Rect;
import ru.geekbrains.spacefly.screen.gamescreen.MainShip;
import ru.geekbrains.spacefly.screen.pool.BulletPool;
import ru.geekbrains.spacefly.screen.sprites.Background;
import ru.geekbrains.spacefly.screen.sprites.Star;

//скрин для новой игры
public class GameScreen extends Base2DScreen {

    private static final int STAR_COUNT = 64;
    private TextureAtlas atlas;
    private Background background;
    private Texture bgTexture; //текстура фона
    private Star star[];
    private MainShip mainShip;
    private BulletPool bulletPool = new BulletPool();
    private Music music;
    private Sound bulletSound;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        //добавляем то же самое что и в menuscreen
        //чтобы не было красного экрана
        bgTexture = new Texture("textures\\sky.jpg");
        background = new Background(new TextureRegion(bgTexture));
        atlas = new TextureAtlas("textures\\mainAtlas.tpack");//передаем конфиг
        star = new Star[STAR_COUNT];
        for (int i = 0; i < star.length; i++) {
            star[i] = new Star(atlas);
        }
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\bullet.wav"));
        mainShip = new MainShip(atlas, bulletPool, bulletSound);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds\\music.mp3"));
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        deleteAllDestroyed();
        draw();
    }

    public void draw() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < star.length; i++) {
            star[i].draw(batch);
        }
        mainShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        batch.end();
    }

    public void update(float delta) {
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
        }
        //чтобы корабль летел после нажатия
        mainShip.update(delta);
        //заставляем пульки двигаться
        bulletPool.updateActiveSprites(delta);
    }

    //попала ли пуля в корабль
    public void checkCollisions() {

    }

    //если пуля попала, то корабль нужно убрать с экрана
    public void deleteAllDestroyed() {
        //пульки чистим
        bulletPool.freeAllDestroyedActiveSprites();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds); //сообщаем бэкграунду, что отпработал ресайз
        //если звезда вылетает за экран, то она возвращается
        for (int i = 0; i < star.length; i++) {
            star[i].resize(worldBounds);
        }
        mainShip.resize(worldBounds); // важно знать границы за которые вылетает корабль
    }

    @Override
    public void dispose() {
        super.dispose();
        bgTexture.dispose();
        atlas.dispose();
        bulletPool.dispose();
        bulletSound.dispose();
        music.dispose();
    }

    //Чтобы использовать кнопки, Корабль должен их принимать
    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);// чтобы корабль узнал о пользовательских событиях
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);
        return super.touchUp(touch, pointer);
    }
}
