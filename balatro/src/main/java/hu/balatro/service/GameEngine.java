package hu.balatro.service;

import hu.balatro.domain.*;
import hu.balatro.exception.InvalidGameActionException;
import hu.balatro.pattern.HandScoringStrategy;
import hu.balatro.pattern.JokerFactory;
import hu.balatro.persistence.GameSaveManager;

import java.util.*;

public class GameEngine {
    private final Deck deck = new Deck();
    private final List<Card> hand = new ArrayList<>();
    private final List<Joker> activeJokers = new ArrayList<>();
    private final HandScoringStrategy scoringStrategy;
    private final GameSaveManager saveManager = new GameSaveManager();

    private int round = 1;
    private int targetScore = 150;
    private int currentScore = 0;
    private int handsLeft = 4;
    private int discardsLeft = 3;
    private int money = 5;

    public GameEngine(HandScoringStrategy scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int highScore = saveManager.loadHighScore();
        System.out.println("=== KONZOL BALATRO ===");
        System.out.println("Eddigi rekord pontszám: " + highScore + "\n");

        while (true) {
            setupRound();
            boolean wonRound = playRound(scanner);

            if (!wonRound) {
                System.out.println("\nVESZTETTÉL! Nem érted el a(z) " + targetScore + " pontot.");
                if (currentScore > highScore) {
                    System.out.println("ÚJ REKORD! Mentés...");
                    saveManager.saveHighScore(currentScore);
                }
                break;
            }

            System.out.println("\nGRATULÁLOK! Teljesítetted a(z) " + round + ". kört!");
            money += 4;
            openShop(scanner);
            round++;
            targetScore = (int) (targetScore * 1.6);
        }
    }

    private void setupRound() {
        deck.reset();
        hand.clear();
        currentScore = 0;
        handsLeft = 4;
        discardsLeft = 3;
        drawUpTo(8);
    }

    private void drawUpTo(int targetSize) {
        while (hand.size() < targetSize && deck.remainingCards() > 0) {
            try {
                hand.add(deck.draw());
            } catch (InvalidGameActionException e) {
                break;
            }
        }
        Collections.sort(hand); // Comparable használata
    }

    private boolean playRound(Scanner scanner) {
        while (currentScore < targetScore && handsLeft > 0) {
            printStatus();
            System.out.println("Parancsok: [P 0 1 2] Kijátszás | [D 0 1] Eldobás | [S] Rendezés szín szerint");
            System.out.print("> ");
            String input = scanner.nextLine().trim().toUpperCase();

            try {
                if (input.equals("S")) {
                    // Lambda Comparator használata
                    hand.sort(Comparator.comparing(Card::getSuit).thenComparing(Card::getRank));
                } else if (input.startsWith("P ")) {
                    List<Card> selected = parseIndices(input.substring(2));
                    playCards(selected);
                } else if (input.startsWith("D ")) {
                    List<Card> selected = parseIndices(input.substring(2));
                    discardCards(selected);
                } else {
                    throw new InvalidGameActionException("Ismeretlen parancs!");
                }
            } catch (InvalidGameActionException e) {
                System.out.println("HIBA: " + e.getMessage());
            }
        }
        return currentScore >= targetScore;
    }

    private List<Card> parseIndices(String indexPart) throws InvalidGameActionException {
        String[] parts = indexPart.trim().split("\\s+");
        if (parts.length == 0 || parts.length > 5) {
            throw new InvalidGameActionException("1 és 5 közötti számú lapot választhatsz!");
        }
        Set<Integer> uniqueIndices = new HashSet<>();
        List<Card> selected = new ArrayList<>();
        for (String p : parts) {
            try {
                int idx = Integer.parseInt(p);
                if (idx < 0 || idx >= hand.size() || !uniqueIndices.add(idx)) {
                    throw new InvalidGameActionException("Érvénytelen vagy ismétlődő lapindex: " + idx);
                }
                selected.add(hand.get(idx));
            } catch (NumberFormatException e) {
                throw new InvalidGameActionException("Csak számokat adj meg indexként!");
            }
        }
        return selected;
    }

    private void playCards(List<Card> selected) {
        HandType handType = scoringStrategy.evaluateHand(selected);
        int cardChips = selected.stream().mapToInt(c -> c.getRank().getChipValue()).sum();

        int[] scoreState = new int[]{handType.getBaseChips() + cardChips, handType.getBaseMult()};

        for (Joker joker : activeJokers) {
            joker.applyEffect(selected, scoreState);
        }

        int roundPoints = scoreState[0] * scoreState[1];
        currentScore += roundPoints;
        handsLeft--;

        System.out.printf("Kijátszva: %s -> %d Zseton x %d Szorzó = +%d pont!\n",
                handType.getHungarianName(), scoreState[0], scoreState[1], roundPoints);

        hand.removeAll(selected);
        drawUpTo(8);
    }

    private void discardCards(List<Card> selected) throws InvalidGameActionException {
        if (discardsLeft <= 0) {
            throw new InvalidGameActionException("Nincs több eldobási lehetőséged ebben a körben!");
        }
        discardsLeft--;
        hand.removeAll(selected);
        drawUpTo(8);
    }

    private void openShop(Scanner scanner) {
        Joker shopJoker = JokerFactory.createRandomJoker();
        System.out.println("\n--- BOLT (Pénzed: $" + money + ") ---");
        System.out.println("Elérhető Joker: " + shopJoker);
        System.out.print("Megveszed? (I/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("I")) {
            if (money >= shopJoker.getCost()) {
                money -= shopJoker.getCost();
                activeJokers.add(shopJoker);
                System.out.println("Megvásároltad: " + shopJoker.getName());
            } else {
                System.out.println("Nincs elég pénzed!");
            }
        }
    }

    private void printStatus() {
        System.out.println("\n------------------------------------------------");
        System.out.printf("Kör: %d | Pont: %d / %d | Pénz: $%d | Kezek: %d | Eldobás: %d\n",
                round, currentScore, targetScore, money, handsLeft, discardsLeft);
        if (!activeJokers.isEmpty()) {
            System.out.println("Aktív Jokerek: " + activeJokers);
        }
        System.out.print("Kezében lévő lapok: ");
        for (int i = 0; i < hand.size(); i++) {
            System.out.print(i + ":" + hand.get(i) + " ");
        }
        System.out.println();
    }
}