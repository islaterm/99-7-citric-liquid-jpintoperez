package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.unit.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:ignacio.slater@ug.uchile.cl">Ignacio Slater M.</a>.
 * @version 1.0.6-rc.1
 * @since 1.0
 */
class PanelTest {
  private final static String PLAYER_NAME = "Suguri";
  private final static int BASE_HP = 4;
  private final static int BASE_ATK = 1;
  private final static int BASE_DEF = -1;
  private final static int BASE_EVD = 2;
  private HomePanel testHomePanel;
  private NeutralPanel testNeutralPanel;
  private BonusPanel testBonusPanel;
  private DropPanel testDropPanel;
  private EncounterPanel testEncounterPanel;
  private BossPanel testBossPanel;
  private Player suguri;
  private long testSeed;

  @BeforeEach
  public void setUp() {
    testBonusPanel = new BonusPanel(0);
    testBossPanel = new BossPanel(1);
    testDropPanel = new DropPanel(2);
    testEncounterPanel = new EncounterPanel(3);
    testHomePanel = new HomePanel(4);
    testNeutralPanel = new NeutralPanel(5);
    testSeed = new Random().nextLong();
    suguri = new Player(PLAYER_NAME, BASE_HP, BASE_ATK, BASE_DEF, BASE_EVD);
  }

  @Test
  public void constructorTest() {

    BonusPanel sameIDBonusPanel = new BonusPanel(0);

    assertNotEquals(testBonusPanel, testDropPanel);
    assertEquals(sameIDBonusPanel, testBonusPanel);
    assertEquals(new BossPanel(1), testBossPanel);
    assertEquals(new DropPanel(2), testDropPanel);
    assertEquals(new EncounterPanel(3), testEncounterPanel);
    assertEquals(new HomePanel(4), testHomePanel);
    assertEquals(new NeutralPanel(5), testNeutralPanel);

    assertNotEquals(testBonusPanel.hashCode(), testDropPanel.hashCode());
    assertNotEquals(testBonusPanel, new Object());
    assertNotEquals(testNeutralPanel, new Player("Peat", 3, 1, 1, 1));
  }

  @Test
  public void nullPanelTest() {
    IPanel panel1 = NullPanel.getNullPanel();
    IPanel panel2 = NullPanel.getNullPanel();

    // Should be a unique instance
    assertEquals(panel1,panel2);
    // Adding panels shouldn't work, will throw warnings
    panel1.addNextPanel(panel2);
    panel1.addPlayer(suguri);
    panel1.removePlayer(suguri);


    // Shouldn't have next panels
    List<IPanel> expectedNextPanels = List.of();
    assertEquals(expectedNextPanels,panel1.getNextPanels());

    // Shouldn't have players
    List<Player> expectedPlayers = List.of();
    assertEquals(expectedPlayers, panel1.getPlayers());

    assertEquals(panel1.getPanelID(), -1);

    panel1.setPanelDescription("Haha funny!!!");
    assertEquals("", panel1.getPanelDescription());

    // Nothing should happen to suguri
    Player backup = suguri.copy();
    panel1.activatedBy(suguri);
    assertEquals(backup, suguri);


  }
  @Test
  public void nextPanelTest() {
    assertTrue(testNeutralPanel.getNextPanels().isEmpty());
    final var expectedPanel1 = new NeutralPanel(6);
    final var expectedPanel2 = new NeutralPanel(7);
    final var sameIDPanel = new NeutralPanel(5);
    final var sameIDasAddedPanel = new BonusPanel(6);

    testNeutralPanel.addNextPanel(expectedPanel1);
    assertEquals(1, testNeutralPanel.getNextPanels().size());

    testNeutralPanel.addNextPanel(expectedPanel2);
    assertEquals(2, testNeutralPanel.getNextPanels().size());

    testNeutralPanel.addNextPanel(expectedPanel2);
    assertEquals(2, testNeutralPanel.getNextPanels().size());

    // Shouldn't add if it has the same ID or is the same object.
    testNeutralPanel.addNextPanel(sameIDPanel);
    testNeutralPanel.addNextPanel(testNeutralPanel);
    assertEquals(2, testNeutralPanel.getNextPanels().size());

    // Shouldn't add because there's already a ID: 6 panel
    testNeutralPanel.addNextPanel(sameIDasAddedPanel);
    assertEquals(2, testNeutralPanel.getNextPanels().size());

    assertEquals(List.of(expectedPanel1, expectedPanel2),
                 testNeutralPanel.getNextPanels());
  }

  @Test
  public void homePanelTest() {
    // this test verifies that the player cannot exceed maximum HP through healing
    // with the Home panel.
    assertEquals(suguri.getMaxHP(), suguri.getCurrentHP());
    testHomePanel.activatedBy(suguri);
    assertEquals(suguri.getMaxHP(), suguri.getCurrentHP());

    // this one verifies that the play can effectively heal using the home panel.
    suguri.setCurrentHP(1);
    testHomePanel.activatedBy(suguri);
    assertEquals(2, suguri.getCurrentHP());
  }

  @Test
  public void neutralPanelTest() {

    // this one verifies that the neutralPanel doesn't affect the player
    final var expectedSuguri = suguri.copy();
    testNeutralPanel.activatedBy(suguri);
    assertEquals(expectedSuguri, suguri);

  }

  @Test
  public void controllerDependantPanelTests() {
    final var expectedSuguri = suguri.copy();
    testBossPanel.activatedBy(suguri);
    assertEquals(expectedSuguri, suguri);
    testEncounterPanel.activatedBy(suguri);
    assertEquals(expectedSuguri, suguri);
  }

  // region : Consistency tests
  @RepeatedTest(100)
  public void bonusPanelConsistencyTest() {
    int expectedStars = 0;
    assertEquals(expectedStars, suguri.getStars());
    final var testRandom = new Random(testSeed);
    suguri.setSeed(testSeed);
    for (int normaLvl = 1; normaLvl <= 6; normaLvl++) {
      final int roll = testRandom.nextInt(6) + 1;
      testBonusPanel.activatedBy(suguri);
      expectedStars += roll * Math.min(3, normaLvl);
      assertEquals(expectedStars, suguri.getStars(),
                   "Test failed with seed: " + testSeed);
      suguri.normaClear();
    }
  }

  @RepeatedTest(100)
  public void dropPanelConsistencyTest() {
    int expectedStars = 30;
    suguri.increaseStarsBy(30);
    assertEquals(expectedStars, suguri.getStars());
    final var testRandom = new Random(testSeed);
    suguri.setSeed(testSeed);
    for (int normaLvl = 1; normaLvl <= 6; normaLvl++) {
      final int roll = testRandom.nextInt(6) + 1;
      testDropPanel.activatedBy(suguri);
      expectedStars = Math.max(expectedStars - roll * normaLvl, 0);
      assertEquals(expectedStars, suguri.getStars(),
                   "Test failed with seed: " + testSeed);
      suguri.normaClear();
    }
  }
  // endregion

  @Test
  public void matrixPosTest() {
    List<IPanel> list = List.of(new NeutralPanel(0), new BonusPanel(1), new BossPanel(2), new DropPanel(3), new EncounterPanel(4), new HomePanel(5), new NeutralPanel(6));
    IPanel nullPanel = NullPanel.getNullPanel();

    int counter = 0;
    // Place them diagonally and then check they're effectively set on diagonal panels.
    for (IPanel panel : list) {
      panel.setMatrixPos(counter,counter);
      counter+=1;
    }

    // Check again if they're on the expected positions
    counter = 0;
    for (IPanel panel : list) {
      assertEquals(counter,panel.getX());
      assertEquals(counter,panel.getY());
      counter++;
    }

    // Check also for the nullPanel, it should return (0,0)
    // regardless of the matrixPos we can assign to it.
    nullPanel.setMatrixPos(69,420);
    assertEquals(0,nullPanel.getX());
    assertEquals(0,nullPanel.getY());


  }

  @Test
  public void getSpritePathTest() {

    IPanel panel = new NeutralPanel(0);
    assertEquals("NEUTRAL",panel.getSpriteString());

    panel = new BonusPanel(0);
    assertEquals("BONUS",panel.getSpriteString());

    panel = new BossPanel(0);
    assertEquals("BOSS",panel.getSpriteString());

    panel = new DropPanel(0);
    assertEquals("DROP",panel.getSpriteString());

    panel = new EncounterPanel(0);
    assertEquals("ENCOUNTER",panel.getSpriteString());

    panel = new HomePanel(0);
    assertEquals("HOME",panel.getSpriteString());

    panel = NullPanel.getNullPanel();
    assertNull(panel.getSpriteString());

  }

}