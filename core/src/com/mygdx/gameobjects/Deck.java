package com.mygdx.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.enums.Pip;
import com.mygdx.enums.Suit;

public class Deck {
    private final Card[][] cards;

    public Deck(TextureAtlas atlas, int backIndex) {
        cards = new Card[Suit.values().length][];
        for (Suit suit : Suit.values()) {
            cards[suit.index] = new Card[Pip.values().length];
            for (Pip pip : Pip.values()) {
                Sprite front = atlas.createSprite(suit.name, pip.value);
                Sprite back = atlas.createSprite("back", backIndex);
                cards[suit.index][pip.index] = new Card(suit, pip, front, back);
            }
        }
    }

    public Card getCard(Suit suit, Pip pip) {
        return cards[suit.index][pip.index];
    }

    public Card[][] getCards() {
        return cards;
    }
}
