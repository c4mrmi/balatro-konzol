package hu.balatro.domain;

public enum HandType {
    HIGH_CARD("Magas lap", 5, 1),
    ONE_PAIR("Egy pár", 10, 2),
    TWO_PAIR("Két pár", 20, 2),
    THREE_OF_A_KIND("Drill", 30, 3),
    FLUSH("Flöss", 35, 4),
    FOUR_OF_A_KIND("Póker", 60, 7);

    private final String hungarianName;
    private final int baseChips;
    private final int baseMult;

    HandType(String hungarianName, int baseChips, int baseMult) {
        this.hungarianName = hungarianName;
        this.baseChips = baseChips;
        this.baseMult = baseMult;
    }

    public String getHungarianName() { return hungarianName; }
    public int getBaseChips() { return baseChips; }
    public int getBaseMult() { return baseMult; }
}