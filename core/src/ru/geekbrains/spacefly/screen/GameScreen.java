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
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.spacefly.base.ActionListener;
import ru.geekbrains.spacefly.base.Base2DScreen;
import ru.geekbrains.spacefly.base.Font;
import ru.geekbrains.spacefly.math.Rect;
import ru.geekbrains.spacefly.screen.gamescreen.Bullet;
import ru.geekbrains.spacefly.screen.gamescreen.ButtonNewGameReset;
import ru.geekbrains.spacefly.screen.gamescreen.Enemy;
import ru.geekbrains.spacefly.screen.gamescreen.MainShip;
import ru.geekbrains.spacefly.screen.gamescreen.MessageGameOver;
import ru.geekbrains.spacefly.screen.gamescreen.TrackingStar;
import ru.geekbrains.spacefly.screen.pool.BulletPool;
import ru.geekbrains.spacefly.screen.pool.EnemyPool;
import ru.geekbrains.spacefly.screen.pool.ExplosionPool;
import ru.geekbrains.spacefly.screen.sprites.Background;
import ru.geekbrains.spacefly.screen.sprites.Star;
import ru.geekbrains.spacefly.utils.EnemyEmitter;

//скрин для новой игры
public class GameScreen extends Base2DScreen implements ActionListener {

    private enum State {
        PLAYING, GAME_OVER
    } //2 состояния игры

    private static final int STAR_COUNT = 64;
    private static final float FONT_SIZE = 0.03f;
    private  static  final String FRAGS = "Frags: ";
    private  static  final String HP = "HP: ";
    private  static  final String LEVEL = "Level: ";
    private Background background;
    private Texture bgTexture; //текстура фона
    private TextureAtlas atlas;

    //private Star star[];
    private TrackingStar star[];
    private MainShip mainShip;
    private BulletPool bulletPool = new BulletPool();
    private Music music;
    private Sound bulletSound;
    private Sound laserSound;
    private Sound explosionSound;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;

    private EnemyEmitter enemyEmitter;
    private State state;

    private MessageGameOver messageGameOver;
    private ButtonNewGameReset buttonNewGameReset;
    private Font font;
    private StringBuilder sbFrags = new StringBuilder();
    private StringBuilder sbHP = new StringBuilder();
    private StringBuilder sbLevel = new StringBuilder();
    //подсчет фрагов. Чем больше, тем сложнее
    int frags;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds\\music.mp3"));
        music.setLooping(true);
        music.play();
        //добавляем то же самое что и в menuscreen
        //чтобы не было красного экрана
        bgTexture = new Texture("textures\\sky.jpg");
        background = new Background(new TextureRegion(bgTexture));
        atlas = new TextureAtlas("textures\\mainAtlas.tpack");//передаем конфиг
       // star = new Star[STAR_COUNT];
        star = new TrackingStar[STAR_COUNT];

        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\bullet.wav"));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\laser.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\explosion.wav"));
        explosionPool = new ExplosionPool(atlas, explosionSound);
        mainShip = new MainShip(atlas, bulletPool, laserSound, explosionPool);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, mainShip, bulletSound);
        enemyEmitter = new EnemyEmitter(atlas, worldBounds, enemyPool);
        messageGameOver = new MessageGameOver(atlas);
        buttonNewGameReset = new ButtonNewGameReset(atlas, this);

        for (int i = 0; i < star.length; i++) {
            //    star[i] = new Star(atlas);
            star[i] = new TrackingStar(atlas, mainShip.getV());
        }

        font = new Font("font/font.fnt", "font/font.png");
        font.setWorldSize(FONT_SIZE);
        startNewGame(); //как только все проинициализировали, вызываем старт
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
        explosionPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        if (state == State.GAME_OVER) {
            messageGameOver.draw(batch);
            buttonNewGameReset.draw(batch);
        }
        printInfo();
        batch.end();
    }

    //выводит статистику
    public void printInfo() {
        sbFrags.setLength(0); //вовзвращаемся в начало для переписки
        sbHP.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft(), worldBounds.getTop());
        font.draw(batch, sbHP.append(HP).append(mainShip.getHp()), worldBounds.pos.x, worldBounds.getTop(), Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight(), worldBounds.getTop(), Align.right);

    }


    public void update(float delta) {
        if (mainShip.isDestroyed()) {
            state = State.GAME_OVER;
        }
        //взрывы и звезды проигрываются до конца вне зависимости от статуса игры
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        switch (state) {
            case PLAYING:
                //чтобы корабль летел после нажатия
                mainShip.update(delta);
                //заставляем пульки двигаться
                bulletPool.updateActiveSprites(delta);
                enemyPool.updateActiveSprites(delta);
                enemyEmitter.generateEnemies(delta, frags);
                break;
            case GAME_OVER:
                break; //все кроме звезд и взрывов перестает апдейтится
        }
    }

    //попала ли пуля в корабль, столкнулся ли корабль
    public void checkCollisions() {
        //столкновение
        List<Enemy> enemyList = enemyPool.getActiveObjects();//вытаскиваем из пула
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            //считаем расстояния от центра кораблей
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if (enemy.pos.dst2(mainShip.pos) < minDist * minDist) { //сравниваем квадраты расстояния
                enemy.destroy();
                mainShip.destroy(); //столкновение кораблей
                state = State.GAME_OVER;
                return;
            }
        }
//урон врагам
        List<Bullet> bulletList = bulletPool.getActiveObjects(); //список пуль на экране
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            //не столкнулись ли пули
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != mainShip || bullet.isDestroyed()) {
                    continue; //это либо вражеская пуля по врагу либо не используется уже. Ни чего не делаем
                }
                if (enemy.isBulletCollision(bullet)) { //если пуля наша
                    enemy.damage(bullet.getDamage());//находится ли прямоугольничек пули за пределами прямогугольника врага
                    bullet.destroy(); //чтобы не пробило несколько кораблей
                    if (enemy.isDestroyed()) {
                        frags++;
                        break;
                    }
                }
            }
        }
        //урон игроку
        for (Bullet bullet : bulletList) {
            if (bullet.isDestroyed() || bullet.getOwner() == mainShip) { //своей пулей себя убить не можем
                continue;//переходим в след итерацию
            }
            if (mainShip.isBulletCollision(bullet)) {
                mainShip.damage(bullet.getDamage());
                bullet.destroy(); //убираем пульку
                if (mainShip.isDestroyed()) {
                    state = State.GAME_OVER;
                }
            }
        }

    }

    //если пуля попала, то корабль нужно убрать с экрана
    public void deleteAllDestroyed() {
        //пульки чистим
        bulletPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
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
        explosionPool.dispose();
        enemyPool.dispose();
        bulletSound.dispose();
        music.dispose();
        font.dispose();
    }

    //Чтобы использовать кнопки, Корабль должен их принимать
    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyDown(keycode);// чтобы корабль узнал о пользовательских событиях
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            mainShip.touchDown(touch, pointer);
        } else {
            buttonNewGameReset.touchDown(touch, pointer);
        }
        //  Explosion explosion = explosionPool.obtain(); // ВЗРЫВЫ по тычку пальца
        //  explosion.set(0.20f, touch);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            mainShip.touchUp(touch, pointer);
        } else {
            buttonNewGameReset.touchUp(touch, pointer);
        }
        return super.touchUp(touch, pointer);
    }

    //метод сбрасывает игру на дефолт
    private void startNewGame() {
        state = State.PLAYING;
        frags = 0;
        mainShip.startNewGame();
        enemyEmitter.startNewGame();

        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
    }


    @Override
    public void actionPerformed(Object src) {
        if (src == buttonNewGameReset) {
            startNewGame();
        }
    }



}
