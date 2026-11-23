package hu.balatro.pattern;

import hu.balatro.domain.Card;
import hu.balatro.domain.HandType;
import java.util.List;

public interface HandScoringStrategy {
    HandType evaluateHand(List<Card> cards);
}