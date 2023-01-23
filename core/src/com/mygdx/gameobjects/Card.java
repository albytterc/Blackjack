package com.mygdx.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.mygdx.blackjack.Blackjack;
import com.mygdx.enums.Pip;
import com.mygdx.enums.Suit;
import com.mygdx.screens.GameScreen;

import static com.mygdx.screens.GameScreen.*;

public class Card extends Actor {
    public final Suit suit;
    public final Pip pip;

    public final Sprite front;
    public final Sprite back;

    private boolean turned;


    // public static final float CARD_WIDTH = 1f;
    // public static final float CARD_HEIGHT = CARD_WIDTH * 277 / 200f;


    public Card(Suit suit, Pip pip, final Sprite front, final Sprite back) {
        front.setSize(front.getWidth(), front.getHeight());
        back.setSize(back.getWidth(), back.getHeight());

        this.suit = suit;
        this.pip = pip;
        this.front = front;
        this.back = back;

        setBounds(front.getX(), front.getY(), front.getWidth(), front.getHeight());
        setTouchable(Touchable.enabled);
    }


    @Override
    protected void positionChanged() {
        front.setPosition(getX(), getY());
        back.setPosition(getX(), getY());

        super.positionChanged();
    }

    @Override
    public void setPosition(float x, float y) {
        front.setPosition(x - front.getWidth() / 2f, y - front.getHeight() / 2f);
        back.setPosition(x - back.getWidth() / 2f, y - back.getHeight() / 2f);
        super.setPosition(x - front.getWidth() / 2f, y - front.getHeight() / 2f);
    }

    public void turn() {
        turned = !turned;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (turned) {
            back.draw(batch);
        } else {
            front.draw(batch);
            // batch.draw(front.getTexture(), front.getX(), front.getY());
        }
    }

    @Override
    public String toString() {
        return pip + " of " + suit + "s";
    }
}
