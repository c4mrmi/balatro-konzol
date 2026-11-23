# Konzol Balatro (Java)

Egy konzolos Java alkalmazás, amelyet a népszerű **Balatro** nevű roguelike deck-builder játék ihletett. A játékos célja, hogy pókertípusú lapkombinációk kijátszásával és különleges kártyák (Jokerek) használatával minél magasabb pontszámot érjen el az egyre nehezedő körökben.

---

## 📋 Jellemzők

* **Klasszikus póker kézértékelés**: Magas lap, Egy pár, Két pár, Drill, Flöss, Póker támogatása.
* **Polimorfikus Joker kártyák**: Különböző hatású Jokerek (+Zsetonok, +Szorzók, Színalapú bónuszok).
* **Interaktív bolt rendszere**: Minden sikeres kör után új Jokerek vásárolhatók az összegyűjtött pénzből.
* **Lokális Rekordok (High Score)**: A legmagasabb elért pontszám automatikus mentése és betöltése szöveges fájlból.
* **Objektumorientált és tiszta architektúra**: Stratégia (Strategy) és Gyár (Factory) tervezési minták alkalmazása.

---

## 🛠️ Projekt Struktúra

Az alkalmazás a jól elkülönített rétegek és csomagstruktúra szerint épül fel:

```
src/main/java/hu/balatro/
├── domain/                  # A játék alapegységei (Card, Deck, Joker, HandType, Rank, Suit)
├── pattern/                 # Tervezési minták (HandScoringStrategy, JokerFactory, StandardPokerEvaluator)
├── service/                 # Játékmenet és üzleti logika (GameEngine)
├── persistence/             # Mentés és betöltés kezelése (GameSaveManager)
├── exception/               # Egyedi kivételkezelés (InvalidGameActionException)
└── Main.java                # Belépési pont
```

---

## 🚀 Telepítés és Futtatás

### Előkövetelmények
* **Java JDK 17** vagy újabb
* **Apache Maven 3.x**

### Futtatás parancssorból (Maven)

1. **Klónozd a tárolót:**
   ```bash
   git clone https://github.com/c4mrmi/balatro-konzol.git
   cd balatro-konzol
   ```

2. **Fordítsd le a projektet és futtasd:**
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="hu.balatro.Main"
   ```

### Futtatás IntelliJ IDEA-ból
1. Nyisd meg a projektet az IntelliJ IDEA-ban (Open -> `pom.xml`).
2. Várd meg, amíg az IDE letölti a Maven függőségeket.
3. Keresd meg a `src/main/java/hu/balatro/Main.java` fájlt, majd kattints a zöld **Run** gombra.

---

## 🎮 Játékmenet és Parancsok

Minden körben meghatározott számú lapot tarthatsz a kezedben (alapértelmezetten 8 lapot). A cél a kijelölt célpontszám (Target Score) elérése.

### Használható parancsok:

| Parancs | PÉLDA | Leírás |
| :--- | :--- | :--- |
| `P [indexek]` | `P 0 1 2` | Kijátssza a megadott indexű lapokat (1-5 lap választható). |
| `D [indexek]` | `D 0 3` | Eldobja a kiválasztott lapokat és újakat húz a pakliból. |
| `S` | `S` | Újrarendezi a kezedben lévő lapokat szín és érték szerint. |

### Pontszámítás működése:
$$\text{Összpontszám} = (\text{Alap zsetonok} + \text{Kártyák értéke} + \text{Joker zsetonok}) \times (\text{Alap szorzó} + \text{Joker szorzók})$$

---

## 📐 UML és Dokumentáció

A részletes UML osztálydiagram és a tervezési minták leírása a [`docs/UML.md`](docs/UML.md) fájlban található meg.