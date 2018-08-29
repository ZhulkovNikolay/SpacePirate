package ru.geekbrains.spacefly;

import com.badlogic.gdx.Game;

import ru.geekbrains.spacefly.screen.MenuScreen;


public class SpaceFly2DGame extends Game {
    //класс умеет переключать экраны
    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}
