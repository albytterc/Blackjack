package com.mygdx.managers;

import com.badlogic.gdx.utils.OrderedSet;
import com.mygdx.enums.Pip;
import com.mygdx.gameobjects.Card;
import com.mygdx.gameobjects.Deck;
import com.mygdx.screens.GameScreen;

public class GameManager {
    private Deck deck;
    public OrderedSet<Card> currentHand;
    public int currentScore;

    public GameManager(Deck deck) {
        this.deck = deck;
        currentHand = new OrderedSet<>();
    }

    public void spawnCards() {
        while (currentHand.size != 2) {
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


}
