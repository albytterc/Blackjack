package com.mygdx.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.OrderedSet;
import com.mygdx.enums.Pip;
import com.mygdx.enums.Suit;


public class Deck {
    private final Card[][] cards;
    private final OrderedSet<Card> deck;

    public Deck(TextureAtlas atlas, int backIndex) {
        cards = new Card[Suit.values().length][];
        deck = new OrderedSet<>();
        for (Suit suit : Suit.values()) {
            cards[suit.index] = new Card[Pip.values().length];
            for (Pip pip : Pip.values()) {
                Sprite front = atlas.createSprite(suit.name, pip.value);
                Sprite back = atlas.createSprite("back", backIndex);
                cards[suit.index][pip.index] = new Card(suit, pip, front, back);
                deck.add(cards[suit.index][pip.index]);
            }
        }

    }


    public Card getRandomCard() {
        int cardId = MathUtils.random(0, deck.size - 1);
        Card toRemove = deck.orderedItems().get(cardId);
        return removeCard(toRemove);
    }


    public Card removeCard(Card card) {
        boolean removed = deck.remove(card);
        if (removed) {
            return card;
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }
}
