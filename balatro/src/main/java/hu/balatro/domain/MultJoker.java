package hu.balatro.domain;

import java.util.List;

public class MultJoker extends Joker {
    private final int bonusMult;

    public MultJoker(String name, String description, int cost, int bonusMult) {
        super(name, description, cost);
        this.bonusMult = bonusMult;
    }

    @Override
    public void applyEffect(List<Card> playedCards, int[] scoreState) {
        scoreState[1] += bonusMult;
    }
}