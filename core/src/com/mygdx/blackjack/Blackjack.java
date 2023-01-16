package com.mygdx.blackjack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.enums.Pip;
import com.mygdx.enums.Suit;
import com.mygdx.gameobjects.Card;
import com.mygdx.gameobjects.Deck;

import java.util.Vector;


public class Blackjack extends Game {
    SpriteBatch batch;
    TextureAtlas atlas;
    public static final float CARD_WIDTH = 1f;
    public static final float CARD_HEIGHT = CARD_WIDTH * 277 / 200f;
    public static final float MINIMUM_VIEWPORT = 13f;
    public OrthographicCamera camera;
    Deck deck;
    ObjectSet<Card> cards;

    @Override
    public void create() {
        batch = new SpriteBatch();
        atlas = new TextureAtlas("Decks/card-deck.atlas");
        cards = new ObjectSet<>();
        deck = new Deck(atlas, 0);

        float aspectRatio = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        // camera.setToOrtho(false);

        for (Card card : deck.getCards()[Suit.Spade.index]) {
            card.setPosition(card.pip.index + CARD_WIDTH / 2f, MINIMUM_VIEWPORT*aspectRatio / 2f + CARD_HEIGHT / 2f);
            cards.add(card);
        }
        // Card diamondQueen = deck.getCard(Suit.Diamond, Pip.Queen);
        // diamondQueen.setPosition(0, 0);
        // cards.add(diamondQueen);
        //
        // Card diamondKing = deck.getCard(Suit.Diamond, Pip.King);
        // diamondKing.setPosition(-1, 0);
        // cards.add(diamondKing);


        camera = new OrthographicCamera(MINIMUM_VIEWPORT, MINIMUM_VIEWPORT * aspectRatio);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        // camera.setToOrtho(true);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resizing " + width + ", " + height);
        // if (width > height) {
        //     camera.viewportHeight = MINIMUM_VIEWPORT;
        //     camera.viewportWidth = camera.viewportHeight * (float) width / (float) height;
        // } else {
        //     camera.viewportWidth = MINIMUM_VIEWPORT;
        //     camera.viewportHeight = camera.viewportWidth * (float) height / (float) width;
        // }
        camera.update();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0.6f, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Card card : cards) {
            if (Gdx.input.isTouched() && !Gdx.input.justTouched()) {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                // System.out.println("x: " + touchPos.x + ", y: " + touchPos.y);
                card.handleTouch(touchPos.x, touchPos.y);
            }
            card.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        atlas.dispose();
    }
}
