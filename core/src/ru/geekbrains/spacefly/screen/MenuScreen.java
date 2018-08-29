package ru.geekbrains.spacefly.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.spacefly.base.ActionListener;
import ru.geekbrains.spacefly.base.Base2DScreen;
import ru.geekbrains.spacefly.math.Rect;
import ru.geekbrains.spacefly.screen.menuscreen.ButtonExit;
import ru.geekbrains.spacefly.screen.menuscreen.ButtonNewGame;
import ru.geekbrains.spacefly.screen.sprites.Background;
import ru.geekbrains.spacefly.screen.sprites.Star;

//экран меню
//так как события кнопки обрабатывают экран, имплементим интерфейс
public class MenuScreen extends Base2DScreen implements ActionListener {

    private static final int STAR_COUNT = 256;
    private static final float BUTTON_PRESS_SCALE = 0.9f;// кнопка уменьшится на 10% при нажатии
    private static final float BUTTON_HEIGHT = 0.15f; // размер кнопки


    private Background background;
    private Texture bgTexture; //текстура фона
    private TextureAtlas atlas;
    private Star[] star;
    private ButtonExit buttonExit;
    private ButtonNewGame buttonNewGame;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        bgTexture = new Texture("textures\\sky.jpg");
        background = new Background(new TextureRegion(bgTexture));
        atlas = new TextureAtlas("textures\\menuAtlas.tpack");//передаем конфиг
        star = new Star[STAR_COUNT];
        for (int i = 0; i < star.length; i++) {
            star[i] = new Star(atlas);
        }
        //передаем наш атлас, ссылку на наш экран обрабатывающий события кнопки,
        buttonExit = new ButtonExit(atlas, this, BUTTON_PRESS_SCALE);
        buttonExit.setHeightProportion(BUTTON_HEIGHT);
        buttonNewGame = new ButtonNewGame(atlas, this, BUTTON_PRESS_SCALE);
        buttonNewGame.setHeightProportion(BUTTON_HEIGHT);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
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
        buttonExit.draw(batch);
        buttonNewGame.draw(batch);
        batch.end();
    }

    public void update(float delta) {
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
        }
    }


    @Override
    public void dispose() {
        super.dispose();
        bgTexture.dispose();
        atlas.dispose();
    }

    //отрисовка в зависимости от размеров экрана
    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds); //сообщаем бэкграунду, что отпработал ресайз

        //если звезда вылетает за экран, то она возвращается
        for (int i = 0; i < star.length; i++) {
            star[i].resize(worldBounds);
        }
        buttonExit.resize(worldBounds);
        buttonNewGame.resize(worldBounds);

    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        buttonExit.touchDown(touch, pointer); //чтобы кнопка знала что произошли эти события
        buttonNewGame.touchDown(touch, pointer);
        return super.touchDown(touch, pointer);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        buttonExit.touchUp(touch, pointer);
        buttonNewGame.touchUp(touch, pointer);
        return super.touchUp(touch, pointer);
    }

    @Override
    public void actionPerformed(Object src) {
        if (src == buttonExit) {
            Gdx.app.exit();
        } else if (src == buttonNewGame) {
            //здесь нужно переключить экраны
            //устанавливаем новый экран
            game.setScreen(new GameScreen(game));
        }
    }
}
