package hu.balatro;

import hu.balatro.pattern.StandardPokerEvaluator;
import hu.balatro.service.GameEngine;

public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(new StandardPokerEvaluator());
        engine.start();
    }
}