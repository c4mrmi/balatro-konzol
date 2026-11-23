# Konzol Balatro

Konzolos, Java nyelven készült Balatro-szimulátor, amelyben a játékos pókerkéz-kombinációk kijátszásával, eldobásával és különleges Joker kártyák vásárlásával igyekszik minél magasabb pontszámot elérni az egyre nehezedő körökben.

A projekt az objektumorientált szoftverfejlesztési elvek és tervezési minták bemutatására készült Java nyelven.

## A program célja

A felhasználó egy interaktív konzolos kártyajátékot játszhat, ahol a szerencse és a stratégiai döntések (melyik kártyát játssza ki, mit dobjon el, milyen Jokert vásároljon) határozzák meg a végeredményt.

A szimuláció a népszerű Balatro kártyajáték simplified, konzolos átirata. A rekord pontszám automatikusan mentésre kerül a helyi háttértárra.

## Funkciók

- Standard 52 lapos francia kártya pakli kezelése keveréssel és húzással.
- Pókerkéz-értékelő rendszer (Magas lap, Egy pár, Két pár, Drill, Flöss, Póker).
- Polimorfikus Joker kártyák (+Zseton, +Szorzó, Színalapú szorzó hatásokkal).
- Dinamikus bolt rendszer minden sikeres kör végén.
- Egyedi kivételkezelés az érvénytelen játékoslépések kiszűrésére.
- Rekord pontszám (High Score) automatikus fájlba mentése és betöltése.
- Lapok rendezése érték és szín szerint.

## Követelmények

- JDK 17 vagy újabb
- Maven 3.9 vagy újabb a parancssoros fordításhoz
- Git a repository klónozásához
- Opcionálisan IntelliJ IDEA

## Letöltés

```bash
git clone https://github.com/c4mrmi/balatro-konzol.git
cd balatro-konzol
```

## Fordítás és futtatás

A projekt gyökérkönyvtárából Maven segítségével:

```bash
mvn clean compile exec:java -Dexec.mainClass="hu.balatro.Main"
```

Vagy a lefordított osztályfájlok közvetlen futtatásával:

```bash
mvn clean package
java -cp target/classes hu.balatro.Main
```

### Futtatás IntelliJ IDEA-ban

1. Nyisd meg a projekt gyökérkönyvtárát.
2. Importáld a `pom.xml` fájlt Maven-projektként.
3. Állítsd a Project SDK értékét JDK 17-re vagy újabbra.
4. Nyisd meg a `Main.java` fájlt (`src/main/java/hu/balatro/Main.java`).
5. Indítsd el a `Main.main()` metódust.

## A játékmenet és parancsok

Minden körben a játékos 8 lapot tart a kezében, és a cél a meghatározott célpontszám (Target Score) elérése a rendelkezésre álló kijátszási és eldobási lehetőségeken belül.

### Használható parancsok

| Parancs | Példa | Leírás |
|---|---|---|
| `P [indexek]` | `P 0 1 2` | Kijátssza a kiválasztott indexű lapokat (1–5 lap választható). |
| `D [indexek]` | `D 0 3` | Eldobja a kiválasztott lapokat és újakat húz a pakliból. |
| `S` | `S` | Újrarendezi a kézben lévő lapokat szín és érték szerint. |

## A szimuláció működése

Minden kör elején a pakli újrakeveredik, a játékos pedig 8 kártyát húz. A kör addig tart, amíg a játékos el nem éri a célpontszámot, vagy el nem fogynak a kijátszási lehetőségei.

A pontszámítás összetevői:

- A kijátszott pókerkéz alapzsetonja és alap-szorzója (pl. Flöss = 35 zseton, 4x szorzó).
- A kijátszott kártyák egyéni chipértékeinek összege.
- Az aktív Jokerek által hozzáadott bónusz zsetonok és szorzók.

Kiszámítási képlet:

Összpontszám = (Alap zseton + Kártya zsetonok + Joker zsetonok) * (Alap szorzó + Joker szorzók)

### Jokerek és a Bolt

Sikeres kör után a játékos jutalompénzt kap, és megnyílik a bolt, ahol véletlenszerűen generált Jokereket vásárolhat:

| Joker Típus | Példa | Hatás |
|---|---|---|
| `ChipJoker` | Kék Joker | +50 Zseton minden kijátszáskor. |
| `MultJoker` | Jimbo | +4 Szorzó minden kijátszáskor. |
| `SuitMultJoker` | Kéjenc Joker / Mohó Joker | +3 Szorzó minden meghatározott színű (pl. Kőr vagy Káró) kijátszott lap után. |

### Rekordok mentése

A játék befejezésekor (vereség esetén), ha az elért pontszám magasabb a korábbi rekordnál, a `GameSaveManager` automatikusan elmenti az új rekordot a `balatro_highscore.txt` fájlba.

## Objektumorientált felépítés

### Főbb típusok

| Típus | Felelősség |
|---|---|
| `Card` | Egy francia kártyát képvisel (szín és érték), megvalósítja a `Comparable` interfészt. |
| `Suit` | A kártyaszínek enumja (Kőr, Káró, Treff, Pikk) és szimbólumaik. |
| `Rank` | A kártyaértékek enumja (2-10, J, Q, K, A) és zsetonértékeik. |
| `Joker` | Absztrakt ősosztály a Jokerek számára, meghatározza az `applyEffect` polimorf metódust. |
| `ChipJoker` | Zsetonbónuszt adó Joker megvalósítás. |
| `MultJoker` | Szorzóbónuszt adó Joker megvalósítás. |
| `SuitMultJoker` | Adott kártyaszín alapján szorzót adó Joker megvalósítás. |
| `Deck` | A 52 lapos pakli keverése, nyilvántartása és kártyák húzása. |
| `HandType` | A pókertípusok enumja alapzsetonokkal és alapszorzókkal. |
| `HandScoringStrategy` | A pókerkéz kiértékelésének közös interfésze (Strategy minta). |
| `StandardPokerEvaluator` | Hagyományos pókerkéz-kiértékelő logikát megvalósító osztály. |
| `JokerFactory` | Véletlenszerű Jokerek példányosításáért felelős gyárosztály (Factory minta). |
| `GameSaveManager` | A legmagasabb pontszám fájlba mentése és betöltése (Persistence). |
| `GameEngine` | A játékmenet, körök, parancsfeldolgozás és bolt vezérlése. |
| `InvalidGameActionException` | Egyedi ellenőrzött kivétel érvénytelen játékoslépésekre. |
| `Main` | A program belépési pontja. |

### Egységbezárás és validáció

Az osztályok privát mezőket használatosak. A konstruktorok és metódusok szigorúan validálják a bemeneti adatokat:

- A kártya színe és értéke nem lehet `null` (`Objects.requireNonNull`).
- Üres pakliból történő húzáskor a rendszer `InvalidGameActionException` kivételt dob.
- Érvénytelen index, nem létező lap vagy hibás parancsformátum esetén a játék nem omlik össze, hanem hibaüzenetet jelenít meg.

### Objektumkapcsolatok

A `GameEngine` tartalmazza a `Deck`, a kézben lévő `Card` objektumok listáját, a vásárolt `Joker` példányokat, valamint a `HandScoringStrategy` és `GameSaveManager` referenciákat.

A `Deck` a `Card` objektumok kollekcióját aggregálja.

### Kollekciók és rendezés

A program generikus `List<T>` kollekciókat és `ArrayList<T>` megvalósításokat használ.

A lapok alapértelmezett rendezése a `Card` osztály `Comparable<Card>` megvalósítását és a `Collections.sort()` metódust használja. A szín szerinti rendezéshez lambda kifejezést és `Comparator.comparing()`-et alkalmaz.

## Tervezési minták

### Strategy minta

A pókerkéz értékelése Strategy mintát használ.

Résztvevők:

- `HandScoringStrategy`: a kiértékelés közös interfésze.
- `StandardPokerEvaluator`: a hagyományos pókerkéz-értékelés megvalósítása.
- `GameEngine`: az interfészt használó kontextus osztály.

A játékos által kiválasztott kártyákat a `GameEngine` az interfészen keresztül adja át a kiértékelőnek. Ez lehetővé teszi új kiértékelési szabályok (pl. egyedi pontozási módok) bevezetését a játékmenet módosítása nélkül.

### Factory minta

A Jokerek előállítása Factory mintával történik.

- `JokerFactory`: centralizáltan felelős a véletlenszerű `Joker` példányok létrehozásáért a bolt számára.

## Csomagok

| Csomag | Tartalom |
|---|---|
| `hu.balatro` | Belépési pont (`Main`). |
| `hu.balatro.domain` | A játék adatmodellje (`Card`, `Deck`, `Joker` és származékai, `HandType`, `Rank`, `Suit`). |
| `hu.balatro.exception` | Egyedi kivételkezelés (`InvalidGameActionException`). |
| `hu.balatro.pattern` | Tervezési minták (`HandScoringStrategy`, `StandardPokerEvaluator`, `JokerFactory`). |
| `hu.balatro.persistence` | Rekordok mentése és betöltése (`GameSaveManager`). |
| `hu.balatro.service` | A játékmenet és az üzleti logika (`GameEngine`). |

## Jelenlegi korlátok

- Grafikus felhasználói felület (GUI) hiánya, a játék teljes egészében parancssoros.
- Korlátozott számú Joker típus (jelenleg 3 különböző típus érhető el).
- A pókerkéz kiértékelő jelenleg 5 lapos kombinációk alapértelmezett felismerését támogatja.
- Nincsenek tarot kártyák és bolygókártyák, amelyek az eredeti Balatro játékban szerepelnek.

## Készítő

c4mrmi — [c4mrmi](https://github.com/c4mrmi)

## Licenc

A projekt MIT licenc alatt érhető el.