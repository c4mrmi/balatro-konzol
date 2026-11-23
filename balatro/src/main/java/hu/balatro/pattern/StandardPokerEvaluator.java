package hu.balatro.pattern;

import hu.balatro.domain.Card;
import hu.balatro.domain.HandType;
import hu.balatro.domain.Rank;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StandardPokerEvaluator implements HandScoringStrategy {
    @Override
    public HandType evaluateHand(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return HandType.HIGH_CARD;
        }

        boolean isFlush = cards.size() == 5 &&
                cards.stream().map(Card::getSuit).distinct().count() == 1;

        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        long maxSameRank = rankCounts.values().stream().mapToLong(Long::longValue).max().orElse(1);
        long pairCount = rankCounts.values().stream().filter(count -> count == 2).count();

        if (maxSameRank == 4) return HandType.FOUR_OF_A_KIND;
        if (isFlush) return HandType.FLUSH;
        if (maxSameRank == 3) return HandType.THREE_OF_A_KIND;
        if (pairCount == 2) return HandType.TWO_PAIR;
        if (maxSameRank == 2) return HandType.ONE_PAIR;

        return HandType.HIGH_CARD;
    }
}