package com.mygdx.managers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.mygdx.blackjack.Blackjack;
import com.mygdx.enums.Pip;
import com.mygdx.enums.Suit;
import com.mygdx.gameobjects.Card;
import com.mygdx.gameobjects.Deck;
import com.mygdx.screens.GameScreen;

public class GameManager {
    private Blackjack game;
    private Deck deck;
    public OrderedSet<Card> currentHand;
    public int currentScore;

    public GameManager(Blackjack game, Deck deck) {
        this.game = game;
        this.deck = deck;
        currentHand = new OrderedSet<>();
    }

    public void spawnCards() {
        while (currentHand.size != 1) {
            Card toAdd = deck.getRandomCard();
            if (toAdd != null) {
                currentHand.add(toAdd);
            }
        }
        updateScore();
    }

    public void restart() {
        currentHand.clear();
        deck = new Deck(GameScreen.atlas, 0);
        spawnCards();
    }

    public void hit() {
        int oldSize = currentHand.size;
        while (currentHand.size - oldSize != 1) {
            Card toAdd = deck.getRandomCard();
            if (toAdd != null) {
                currentHand.add(toAdd);
            }
        }
        updateScore();
    }

    public void updateScore() {
        currentScore = 0;
        int numAces = 0;
        for (Card card : currentHand) {
            currentScore += Math.min(card.pip.value, 10);

            if (card.pip == Pip.Ace) {
                if (numAces == 0) {
                    currentScore += 10;
                }
                numAces++;
            }

            if (numAces >= 1 && currentScore > 21) {
                currentScore -= 10;
                numAces = -1;
            }
        }
    }

    private boolean containsAce(OrderedSet<Card> hand) {
        for (Suit suit : Suit.values()) {
            if (hand.contains(deck.getCard(suit, Pip.Ace))) {
                return true;
            }
        }
        return false;
    }



}
