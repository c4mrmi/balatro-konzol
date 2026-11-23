package hu.balatro.pattern;

import hu.balatro.domain.*;
import java.util.List;
import java.util.Random;

public class JokerFactory {
    private static final Random RANDOM = new Random();

    public static Joker createRandomJoker() {
        List<Joker> pool = List.of(
                new MultJoker("Jimbo", "+4 Szorzó minden kijátszáskor", 4, 4),
                new ChipJoker("Kék Joker", "+50 Zseton minden kijátszáskor", 5, 50),
                new SuitMultJoker("Kéjenc Joker", "+3 Szorzó minden Kőr (♥) lap után", 6, Suit.HEARTS, 3),
                new SuitMultJoker("Mohó Joker", "+3 Szorzó minden Káró (♦) lap után", 6, Suit.DIAMONDS, 3)
        );
        return pool.get(RANDOM.nextInt(pool.size()));
    }
}