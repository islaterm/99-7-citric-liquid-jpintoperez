package com.github.cc3002.citricjuice.model;

import com.github.cc3002.citricjuice.model.unit.BossUnit;
import com.github.cc3002.citricjuice.model.unit.Player;
import com.github.cc3002.citricjuice.model.unit.WildUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the players of the game.
 *
 * @author <a href="mailto:ignacio.slater@ug.uchile.cl">Ignacio Slater M.</a>.
 * @version 1.0.6-rc.1
 * @since 1.0
 */
public class UnitTest {
  private final static String PLAYER_NAME = "Suguri";
  private Player suguri;
  private Player kai;
  private WildUnit chicken;
  private BossUnit manager;

  @BeforeEach
  public void setUp() {
    suguri = new Player(PLAYER_NAME, 4, 1, -1, 2);
    kai = new Player("Kai", 5, 1, 0, 0);
    chicken = new WildUnit("Chicken", 3, -1, -1, 1);
    manager = new BossUnit("Store Manager", 8, 3, 2, -1);
  }

  @Test
  public void constructorTest() {
    final var expectedSuguri = new Player(PLAYER_NAME, 4, 1, -1, 2);
    assertEquals(expectedSuguri, suguri);

    final var expectedChicken = new WildUnit("Chicken", 3, -1, -1, 1);
    assertEquals(expectedChicken, chicken);

    final var expectedManager = new BossUnit("Store Manager", 8, 3, 2, -1);
    assertEquals(expectedManager, manager);
  }

  @Test
  public void testEquals() {
    final var o = new Object();
    assertNotEquals(suguri, o);
    assertEquals(suguri, suguri);
    final var expectedSuguri = new Player(PLAYER_NAME, 4, 1, -1, 2);
    assertEquals(expectedSuguri, suguri);
    // also check if suguri is not equal to a different character (kai)
    assertNotEquals(suguri,kai);

    assertNotEquals(manager, o);
    assertEquals(manager, manager);

    assertNotEquals(chicken, o);
    assertNotEquals(chicken, manager);
    assertEquals(chicken, chicken);

  }

  @Test
  public void hitPointsTest() {
    assertEquals(suguri.getMaxHP(), suguri.getCurrentHP());
    suguri.setCurrentHP(2);
    assertEquals(2, suguri.getCurrentHP());
    suguri.setCurrentHP(-1);
    assertEquals(0, suguri.getCurrentHP());
    suguri.setCurrentHP(5);
    assertEquals(4, suguri.getCurrentHP());
  }

  @Test
  public void normaClearTest() {
    suguri.normaClear();
    assertEquals(2, suguri.getNormaLevel());
  }

  @Test
  public void copyTest() {
    final var expectedSuguri = new Player(PLAYER_NAME, 4, 1, -1, 2);
    final var actualSuguri = suguri.copy();
    final var expectedManager = manager;
    final var actualManager = manager.copy();
    final var expectedChicken = chicken;
    final var actualChicken = chicken.copy();
    // Checks that the copied player have the same parameters as the original
    assertEquals(expectedSuguri, actualSuguri);
    assertEquals(expectedManager, actualManager);
    assertEquals(expectedChicken, actualChicken);
    // Checks that the copied player doesn't reference the same object
    assertNotSame(expectedSuguri, actualSuguri);
    assertNotSame(expectedManager, actualManager);
    assertNotSame(expectedChicken, actualChicken);

  }

  // region : consistency tests
  @RepeatedTest(100)
  public void hitPointsConsistencyTest() {
    final long testSeed = new Random().nextLong();
    // We're gonna try and set random hit points in [-maxHP * 2, maxHP * 2]
    final int testHP = new Random(testSeed).nextInt(4 * suguri.getMaxHP() + 1)
                       - 2 * suguri.getMaxHP();
    suguri.setCurrentHP(testHP);
    assertTrue(0 <= suguri.getCurrentHP()
               && suguri.getCurrentHP() <= suguri.getMaxHP(),
               suguri.getCurrentHP() + "is not a valid HP value"
               + System.lineSeparator() + "Test failed with random seed: "
               + testSeed);
  }

  @RepeatedTest(100)
  public void normaClearConsistencyTest() {
    final long testSeed = new Random().nextLong();
    // We're gonna test for 0 to 5 norma clears
    final int iterations = Math.abs(new Random(testSeed).nextInt(6));
    final int expectedNorma = suguri.getNormaLevel() + iterations;
    for (int it = 0; it < iterations; it++) {
      suguri.normaClear();
    }
    assertEquals(expectedNorma, suguri.getNormaLevel(),
                 "Test failed with random seed: " + testSeed);
  }

  @RepeatedTest(100)
  public void rollConsistencyTest() {
    final long testSeed = new Random().nextLong();
    suguri.setSeed(testSeed);
    final int roll = suguri.roll();
    assertTrue(roll >= 1 && roll <= 6,
               roll + "is not in [1, 6]" + System.lineSeparator()
               + "Test failed with random seed: " + testSeed);
  }
  // endregion

  @RepeatedTest(500)
  public void winsAndStarsConsistencyTest() {
    final long testSeed1 = new Random().nextLong();
    final long testSeed2 = new Random().nextLong();
    final long testSeed3 = new Random().nextLong();
    final long testSeed4 = new Random().nextLong();
    Random suguriRandom = new Random();
    Random kaiRandom = new Random();
    Random chickenRandom = new Random();
    Random managerRandom = new Random();
    suguriRandom.setSeed(testSeed1);
    kaiRandom.setSeed(testSeed2);
    chickenRandom.setSeed(testSeed3);
    managerRandom.setSeed(testSeed4);
    suguri.setSeed(testSeed1);
    kai.setSeed(testSeed2);
    chicken.setSeed(testSeed3);
    manager.setSeed(testSeed4);

    kai.setCurrentHP(3);
    kai.increaseStarsBy(30);

    int kaiDefense = kaiRandom.nextInt(6) + 1 + kai.getDef();
    int managerAttack = managerRandom.nextInt(6) + 1 + manager.getAtk();
    int expectedAttack = Math.max(1, managerAttack - kaiDefense);
    int expectedBossStars = 0;
    int expectedBossWins = 0;
    if (expectedAttack >= kai.getCurrentHP()) {
      expectedBossStars = Math.floorDiv(kai.getStars(),2);
      expectedBossWins = 2; }
    kai.defendAttack(manager, manager.getAttackRoll());

    assertEquals(manager.getStars(), expectedBossStars);
    assertEquals(manager.getWins(), expectedBossWins);

    manager.setCurrentHP(2);
    int suguriAttack = suguriRandom.nextInt(6) + 1 + suguri.getAtk();
    int managerEvasion = managerRandom.nextInt(6) + 1 + manager.getEvd();
    int expectedManagerHP = Math.max(0, manager.getCurrentHP() - suguriAttack );
    int expectedSuguriStars = 0;
    int expectedSuguriWins = 0;
    if (managerEvasion > suguriAttack) {
      expectedManagerHP = manager.getCurrentHP();
    }
    if (expectedManagerHP == 0) {
      expectedSuguriStars = manager.getStars();
      expectedSuguriWins = 3;
    }
    manager.evadeAttack(suguri, suguri.getAttackRoll());

    assertEquals(expectedSuguriStars,suguri.getStars());
    assertEquals(expectedSuguriWins,suguri.getWins());

    chicken.increaseStarsBy(13);
    chicken.setCurrentHP(2);
    int chickenDefense = chickenRandom.nextInt(6) + 1 + chicken.getDef();
    int kaiAttack = kaiRandom.nextInt(6) + 1 + kai.getAtk();
    int expectedChickenHP = Math.max(0, chicken.getCurrentHP() - Math.max(1 , kaiAttack - chickenDefense ));
    int expectedKaiStars = kai.getStars();
    int expectedKaiWins = kai.getWins();
    if (expectedChickenHP == 0) {
      expectedKaiStars += 13;
      expectedKaiWins += 1;
    }
    chicken.evadeAttack(kai, kai.getAttackRoll());

    assertEquals(expectedKaiStars,kai.getStars());
    assertEquals(expectedKaiWins,kai.getWins());







  }

  @Test
  public void bossWinsTest() {
    BossUnit shifurobot = new BossUnit("Shifu Robot", 7, 2, 3, -2);

    kai.increaseStarsBy(50);
    chicken.increaseStarsBy(50);
    shifurobot.increaseStarsBy(50);

    kai.setCurrentHP(1);
    shifurobot.setCurrentHP(1);
    chicken.setCurrentHP(1);

    chicken.defendAttack(manager, manager.getAttackRoll());
    assertEquals(manager.getStars(), 25);
    assertEquals(manager.getWins(), 1);

    kai.defendAttack(manager, manager.getAttackRoll());
    assertEquals(manager.getStars(), 50);
    assertEquals(manager.getWins(), 3);

    shifurobot.defendAttack(manager, manager.getAttackRoll());
    assertEquals(manager.getStars(),75);
    assertEquals(manager.getWins(), 6);
  }

  @RepeatedTest(300)
  public void wildWinsTest() {
    WildUnit roboball = new WildUnit("Robo Ball", 3, -1, 1, -1);
    final long testSeed = new Random().nextLong();
    Random chickenRandom = new Random();
    chickenRandom.setSeed(testSeed);
    chicken.setSeed(testSeed);

    kai.increaseStarsBy(50);
    roboball.increaseStarsBy(50);
    manager.increaseStarsBy(50);

    kai.setCurrentHP(1);
    manager.setCurrentHP(1);
    roboball.setCurrentHP(1);

    int expectedChickenWins = 0;
    int expectedChickenStars = 0;
    int expectedChickenATK =  chicken.getAtk() + chickenRandom.nextInt(6) + 1;
    if (expectedChickenATK > 0) {
      expectedChickenWins += 1;
      expectedChickenStars += 25;
    }
    roboball.defendAttack(chicken, chicken.getAttackRoll());
    assertEquals(expectedChickenStars, chicken.getStars());
    assertEquals(expectedChickenWins, chicken.getWins());

    expectedChickenATK =  chicken.getAtk() + chickenRandom.nextInt(6) + 1;
    if (expectedChickenATK > 0) {
      expectedChickenWins += 2;
      expectedChickenStars += 25;
    }
    kai.defendAttack(chicken, chicken.getAttackRoll());
    assertEquals(expectedChickenStars, chicken.getStars());
    assertEquals(expectedChickenWins, chicken.getWins());

    expectedChickenATK =  chicken.getAtk() + chickenRandom.nextInt(6) + 1;
    if (expectedChickenATK > 0) {
      expectedChickenWins += 3;
      expectedChickenStars += 25;
    }
    manager.defendAttack(chicken, chicken.getAttackRoll());
    assertEquals(expectedChickenStars, chicken.getStars());
    assertEquals(expectedChickenWins, chicken.getWins());
  }

  @RepeatedTest(100)
  public void defendFromAttackConsistencyTest() {
    final long testSeed1 = new Random().nextLong();
    final long testSeed2 = new Random().nextLong();
    final Random suguriRandom = new Random();
    final Random kaiRandom = new Random();
    suguriRandom.setSeed(testSeed1);
    suguri.setSeed(testSeed1);
    kaiRandom.setSeed(testSeed2);
    kai.setSeed(testSeed2);

    int expectedAttack = suguriRandom.nextInt(6) + 1 + suguri.getAtk();
    int expectedDefense = kaiRandom.nextInt(6) + 1 + kai.getDef();

    int expectedKaiHP = Math.max(0,kai.getCurrentHP() - Math.max(1, expectedAttack - expectedDefense));
    kai.defendAttack(suguri, suguri.getAttackRoll() );
    assertEquals(kai.getCurrentHP(),expectedKaiHP);

  }

  @RepeatedTest(100)
  public void evadeAttackConsistencyTest() {
    final long testSeed1 = new Random().nextLong();
    final long testSeed2 = new Random().nextLong();
    final Random suguriRandom = new Random();
    final Random kaiRandom = new Random();
    suguriRandom.setSeed(testSeed1);
    suguri.setSeed(testSeed1);
    kaiRandom.setSeed(testSeed2);
    kai.setSeed(testSeed2);

    int expectedAttack = suguriRandom.nextInt(6) + 1 + suguri.getAtk();
    int expectedEvade = kaiRandom.nextInt(6) + 1 + kai.getEvd();

    int expectedKaiHP = Math.max(0,kai.getCurrentHP() - expectedAttack);
    if (expectedEvade > expectedAttack) {
      expectedKaiHP = kai.getCurrentHP(); }

    kai.evadeAttack(suguri, suguri.getAttackRoll() );
    assertEquals(kai.getCurrentHP(),expectedKaiHP);

  }

  @Test
  public void statChangingTest() {
    suguri.setAtk(5);
    assertEquals(suguri.getAtk(),5);
    suguri.setDef(2);
    assertEquals(suguri.getDef(),2);
    suguri.setEvd(-3);

  }

}
