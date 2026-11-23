# Balatro Projekt - UML Osztálydiagram és Architektúra

Ez a dokumentum tartalmazza a **Balatro** projekt osztálydiagramját Mermaid formátumban, valamint az egyes csomagok és tervezési minták rövid leírását.

---

## 1. UML Osztálydiagram

```mermaid
classDiagram
    direction LR

    class Card {
        - Suit suit
        - Rank rank
        + Card(Suit suit, Rank rank)
        + getSuit() Suit
        + getRank() Rank
        + compareTo(Card other) int
        + toString() String
    }

    class Suit {
        <<enumeration>>
        HEARTS
        DIAMONDS
        CLUBS
        SPADES
        - String symbol
        + getSymbol() String
    }

    class Rank {
        <<enumeration>>
        TWO
        THREE
        FOUR
        FIVE
        SIX
        SEVEN
        EIGHT
        NINE
        TEN
        JACK
        QUEEN
        KING
        ACE
        - int chipValue
        - String label
        + getChipValue() int
        + getLabel() String
    }

    class Joker {
        <<abstract>>
        - String name
        - String description
        - int cost
        # Joker(String name, String description, int cost)
        + getName() String
        + getDescription() String
        + getCost() int
        + applyEffect(List~Card~ playedCards, int[] scoreState)*
        + toString() String
    }

    class ChipJoker {
        - int bonusChips
        + ChipJoker(String name, String description, int cost, int bonusChips)
        + applyEffect(List~Card~ playedCards, int[] scoreState)
    }

    class MultJoker {
        - int bonusMult
        + MultJoker(String name, String description, int cost, int bonusMult)
        + applyEffect(List~Card~ playedCards, int[] scoreState)
    }

    class SuitMultJoker {
        - Suit targetSuit
        - int multPerCard
        + SuitMultJoker(String name, String description, int cost, Suit targetSuit, int multPerCard)
        + applyEffect(List~Card~ playedCards, int[] scoreState)
    }

    Joker <|-- ChipJoker
    Joker <|-- MultJoker
    Joker <|-- SuitMultJoker

    class Deck {
        - List~Card~ cards
        + Deck()
        + reset()
        + draw() Card
        + remainingCards() int
    }

    class HandType {
        <<enumeration>>
        HIGH_CARD
        ONE_PAIR
        TWO_PAIR
        THREE_OF_A_KIND
        FLUSH
        FOUR_OF_A_KIND
        - String hungarianName
        - int baseChips
        - int baseMult
        + getHungarianName() String
        + getBaseChips() int
        + getBaseMult() int
    }

    class HandScoringStrategy {
        <<interface>>
        + evaluateHand(List~Card~ cards) HandType
    }

    class StandardPokerEvaluator {
        + evaluateHand(List~Card~ cards) HandType
    }

    HandScoringStrategy <|.. StandardPokerEvaluator

    class JokerFactory {
        - static Random RANDOM
        + static createRandomJoker() Joker
    }

    class GameSaveManager {
        - static String SAVE_FILE
        + saveHighScore(int score)
        + loadHighScore() int
    }

    class GameEngine {
        - Deck deck
        - List~Card~ hand
        - List~Joker~ activeJokers
        - HandScoringStrategy scoringStrategy
        - GameSaveManager saveManager
        - int round
        - int targetScore
        - int currentScore
        - int handsLeft
        - int discardsLeft
        - int money
        + GameEngine(HandScoringStrategy scoringStrategy)
        + start()
        - setupRound()
        - drawUpTo(int targetSize)
        - playRound(Scanner scanner) boolean
        - parseIndices(String indexPart) List~Card~
        - playCards(List~Card~ selected)
        - discardCards(List~Card~ selected)
        - openShop(Scanner scanner)
        - printStatus()
    }

    class InvalidGameActionException {
        + InvalidGameActionException(String message)
    }

    Card --> Suit
    Card --> Rank
    Deck o-- Card
    GameEngine --> Deck
    GameEngine o-- Card
    GameEngine o-- Joker
    GameEngine --> HandScoringStrategy
    GameEngine --> GameSaveManager
    JokerFactory ..> Joker
    JokerFactory ..> ChipJoker
    JokerFactory ..> MultJoker
    JokerFactory ..> SuitMultJoker
    StandardPokerEvaluator ..> HandType
    StandardPokerEvaluator ..> Card
```

---

## 2. Csomagok Struktúrája

* **`hu.balatro.domain`**: A játék alapvető domain modelljei (`Card`, `Deck`, `Joker` és származékai, `HandType`, `Rank`, `Suit`).
* **`hu.balatro.pattern`**: Tervezési minták implementációi (Stratégia minta a pókerkéz értékelésére, Gyár minta a Jokerek előállítására).
* **`hu.balatro.service`**: A játékmenetért felelős üzleti logika (`GameEngine`).
* **`hu.balatro.persistence`**: A rekordok és mentések kezelése (`GameSaveManager`).
* **`hu.balatro.exception`**: Egyedi kivétel osztály (`InvalidGameActionException`).

---

## 3. Alkalmazott Tervezési Minták és OOP Elvek

1. **Stratégia (Strategy) minta**: 
   * `HandScoringStrategy` interfész és `StandardPokerEvaluator` megvalósítás. Lehetővé teszi, hogy a kéz kiértékelési algoritmusát függetlenítsük a játékmenettől.
2. **Gyár (Factory) minta**: 
   * `JokerFactory` osztály, amely centralizáltan felelős a véletlenszerű `Joker` objektumok példányosításáért.
3. **Polimorfizmus és Absztrakció**: 
   * Az absztrakt `Joker` osztályból származó `ChipJoker`, `MultJoker` és `SuitMultJoker` osztályok mind felüldefiniálják az `applyEffect` metódust, sajátos pontszámítási hatást biztosítva.