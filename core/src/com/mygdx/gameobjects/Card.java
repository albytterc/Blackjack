package com.mygdx.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.blackjack.Blackjack;
import com.mygdx.enums.Pip;
import com.mygdx.enums.Suit;

public class Card {
    public final Suit suit;
    public final Pip pip;
    public Vector2 position = new Vector2();
    public boolean touchable = false;

    private final Sprite front;
    private final Sprite back;

    private boolean turned;


    public Card(Suit suit, Pip pip, Sprite front, Sprite back) {
        front.setSize(Blackjack.CARD_WIDTH, Blackjack.CARD_HEIGHT);
        back.setSize(Blackjack.CARD_WIDTH, Blackjack.CARD_HEIGHT);

        this.suit = suit;
        this.pip = pip;
        this.front = front;
        this.back = back;
    }

    public void setPosition(float x, float y) {
        front.setPosition(x - front.getWidth() / 2f, y - front.getHeight() / 2f);
        back.setPosition(x - back.getWidth() / 2f, y - back.getHeight() / 2f);
    }

    public void turn() {
        turned = !turned;
    }

    public boolean handleTouch(float x, float y) {
        boolean inBoundsX = x >= front.getX() && x <= front.getX() + front.getWidth();
        boolean inBoundsY = y >= front.getY() && y <= front.getY() + front.getHeight();

        // System.out.println("X: " + x + ", y: " + y);
        // System.out.println("card " + suit.name + ", " + pip.name() + " x: (" + front.getX() + ", " + (front.getWidth() + front.getX()) + ") y: (" + front.getY() + ", " + (front.getHeight() + front.getY()) + ")");

        if (inBoundsX && inBoundsY) {
            System.out.println("card " + suit.name + ", " + pip.name() + " touched");
            setPosition(x, y);
            return true;
        }
        return false;
    }


    public void draw(Batch batch) {
        if (turned) {
            back.draw(batch);
        } else {
            front.draw(batch);
        }
    }
}
