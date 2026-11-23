package hu.balatro.domain;

import hu.balatro.exception.InvalidGameActionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        reset();
    }

    public void reset() {
        cards.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }

    public Card draw() throws InvalidGameActionException {
        if (cards.isEmpty()) {
            throw new InvalidGameActionException("A pakli elfogyott, nem lehet több lapot húzni!");
        }
        return cards.remove(cards.size() - 1);
    }

    public int remainingCards() {
        return cards.size();
    }
}