package ru.geekbrains.spacefly.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.geekbrains.spacefly.SpaceFly2DGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        float aspect = 3f / 4f; //соотношение экрана телефона
        config.width = 350; //в переменной конфиг задаются параметры экрана
        config.height = (int)(config.width / aspect);
        config.resizable = false; //для десктопной версии

        new LwjglApplication(new SpaceFly2DGame(), config);
    }
}
