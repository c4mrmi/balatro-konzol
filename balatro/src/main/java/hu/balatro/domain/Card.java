package hu.balatro.domain;

import java.io.Serializable;
import java.util.Objects;

public class Card implements Comparable<Card>, Serializable {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = Objects.requireNonNull(suit, "A szín nem lehet null!");
        this.rank = Objects.requireNonNull(rank, "Az érték nem lehet null!");
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public int compareTo(Card other) {
        int rankCompare = Integer.compare(this.rank.ordinal(), other.rank.ordinal());
        return rankCompare != 0 ? rankCompare : this.suit.compareTo(other.suit);
    }

    @Override
    public String toString() {
        return "[" + rank.getLabel() + suit.getSymbol() + "]";
    }
}