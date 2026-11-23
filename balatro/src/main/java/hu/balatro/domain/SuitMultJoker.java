package hu.balatro.domain;

import java.util.List;

public class SuitMultJoker extends Joker {
    private final Suit targetSuit;
    private final int multPerCard;

    public SuitMultJoker(String name, String description, int cost, Suit targetSuit, int multPerCard) {
        super(name, description, cost);
        this.targetSuit = targetSuit;
        this.multPerCard = multPerCard;
    }

    @Override
    public void applyEffect(List<Card> playedCards, int[] scoreState) {
        long matchingCount = playedCards.stream()
                .filter(c -> c.getSuit() == targetSuit)
                .count();
        scoreState[1] += (int) (matchingCount * multPerCard);
    }
}