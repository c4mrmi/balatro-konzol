package hu.balatro.domain;

import java.util.List;

public class ChipJoker extends Joker {
    private final int bonusChips;

    public ChipJoker(String name, String description, int cost, int bonusChips) {
        super(name, description, cost);
        this.bonusChips = bonusChips;
    }

    @Override
    public void applyEffect(List<Card> playedCards, int[] scoreState) {
        scoreState[0] += bonusChips;
    }
}