package hu.balatro.domain;

import java.io.Serializable;
import java.util.List;

public abstract class Joker implements Serializable {
    private final String name;
    private final String description;
    private final int cost;

    protected Joker(String name, String description, int cost) {
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getCost() { return cost; }
a
    // Polimorfikus metódus: minden Joker máshogy módosítja a pontszámot [chips, mult]
    public abstract void applyEffect(List<Card> playedCards, int[] scoreState);

    @Override
    public String toString() {
        return name + " (" + description + ") - Ár: $" + cost;
    }
}