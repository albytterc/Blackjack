package com.mygdx.blackjack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.screens.GameScreen;

public class Blackjack extends Game {
    public static Skin gameSkin;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;

    @Override
    public void create() {
        gameSkin = new Skin(Gdx.files.absolute("C:\\Users\\albyt\\Downloads\\gdx-skins-master\\gdx-skins-master\\cloud-form\\skin\\cloud-form-ui.json"));
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }
}
