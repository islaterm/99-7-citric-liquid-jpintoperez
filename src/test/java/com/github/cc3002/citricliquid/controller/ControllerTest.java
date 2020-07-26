package com.github.cc3002.citricliquid.controller;

import com.github.cc3002.citricjuice.model.board.*;
import com.github.cc3002.citricjuice.model.norma.INormaGoal;
import com.github.cc3002.citricjuice.model.norma.NormaFactory;
import com.github.cc3002.citricjuice.model.norma.StarsNorma;
import com.github.cc3002.citricjuice.model.norma.WinsNorma;
import com.github.cc3002.citricjuice.model.unit.BossUnit;
import com.github.cc3002.citricjuice.model.unit.Player;
import com.github.cc3002.citricjuice.model.unit.WildUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
  GameController controller = new GameController();
  private Player suguri;
  private Player kai;

  @BeforeEach
  void ControllerSetUp() {
    controller = new GameController();
    suguri = new Player("Suguri", 4, 1, -1, 2);
    kai = new Player("Kai", 5, 1, 0, 0);
  }

  @Test
  public void playerCreationTest() {
    List<Player> expectedListOfPlayers = List.of(suguri,kai);
    controller.createPlayer("Suguri", 4, 1, -1, 2, NullPanel.getNullPanel());
    controller.createPlayer("Kai", 5, 1, 0, 0, NullPanel.getNullPanel());
    List<Player> actualListOfPlayers = controller.getPlayers();
    assertEquals(expectedListOfPlayers, actualListOfPlayers, "Expected list of players is not the actual one!");
  }

  @Test
  public void unitCreationTest() {
    WildUnit expectedChicken = new WildUnit("Chicken", 3, -1, -1, 1);
    BossUnit expectedManager = new BossUnit("Store Manager", 8, 3, 2, -1);

    WildUnit actualChicken = controller.createWildUnit("Chicken", 3, -1, -1, 1);
    BossUnit actualManager = controller.createBossUnit("Store Manager", 8, 3, 2, -1);
    assertEquals(expectedChicken, actualChicken, "Expected Wild Unit is not the actual one!");
    assertEquals(expectedManager, actualManager, "Expected Boss Unit is not the actual one!");
  }

  @Test void normaCheckTest() {
    Player player1 = controller.createPlayer("Suguri", 4, 1, -1, 2, NullPanel.getNullPanel());
    controller.setCurrPlayerNormaGoal(NormaFactory.getStarsNorma(1));

    controller.beginTurn();
    controller.normaCheck(player1);
    int expectedNormaLevel = 1;
    int actualNormaLevel = player1.getNormaLevel();
    assertEquals(expectedNormaLevel, actualNormaLevel, "Norma Clear shouldn't have passed!");
    player1.increaseStarsBy(60);
    controller.normaCheck(player1);
    expectedNormaLevel = 2;
    actualNormaLevel = player1.getNormaLevel();
    assertEquals(expectedNormaLevel, actualNormaLevel, "Norma Clear should have passed!!!!!");


  }

  @Test
  public void gameWinnerTest() {

    HomePanel panel1 = controller.createHomePanel(0);
    NeutralPanel panel2 = controller.createNeutralPanel(1);
    DropPanel panel3 = controller.createDropPanel(2);
    NeutralPanel panel4 = controller.createNeutralPanel(3);
    NeutralPanel panel5 = controller.createNeutralPanel(4);

    controller.setNextPanel(panel1, panel2);
    controller.setNextPanel(panel2, panel3);
    controller.setNextPanel(panel3, panel4);
    controller.setNextPanel(panel4, panel5);
    controller.setNextPanel(panel5, panel1);

    Player player1 = controller.createPlayer("Suguri", 4, 1, -1, 2, panel2);
    controller.setPlayerHome(player1, panel1);
    assertNull(controller.getWinner());
    controller.beginTurn();
    player1.setNormaLevel(4);
    player1.normaClear();
    controller.selectStarsNorma();
    controller.finishTurn();
    controller.beginTurn();
    controller.movePlayer(3);
    // We force this player to win the game by passing them the final amount
    // of stars before reaching norma check.
    player1.increaseStarsBy(200);
    int move = controller.movePlayer(1);
    assertEquals(move,0);

    assertTrue(controller.getGameEnded(), "Game hasn't ended and it should have ended!");
    assertEquals(player1, controller.getWinner(), "The winner is not the player that should have won the game!");

  }

  @Test
  public void panelCreationTest() {
    HomePanel panel1 = new HomePanel(0);
    NeutralPanel panel2 = new NeutralPanel(1);
    DropPanel panel3 = new DropPanel(2);
    BossPanel panel4 = new BossPanel(3);
    NeutralPanel panel5 = new NeutralPanel(4);
    EncounterPanel panel6 = new EncounterPanel(5);
    BonusPanel panel7 = new BonusPanel(6);
    // List with a panel of each type, except it's not.
    // actually it has two neutral panels, but it does have at least one of each kind.
    List<IPanel> expectedListOfPanels = List.of(panel1,panel2,panel3,panel4,panel5,panel6,panel7);

    controller.createHomePanel(0);
    controller.createNeutralPanel(1);
    controller.createDropPanel(2);
    controller.createBossPanel(3);
    controller.createNeutralPanel(4);
    controller.createEncounterPanel(5);
    controller.createBonusPanel(6);

    assertEquals(expectedListOfPanels, controller.getPanels());


  }

  @Test
  public void setNormaTest() {
    suguri.normaClear();
    INormaGoal expected = NormaFactory.getStarsNorma(2);
    controller.setStarsNorma(suguri);
    assertEquals(expected, suguri.getNormaGoal());
    assertEquals(NormaFactory.getStarsNorma(2).getRequirement(), suguri.getNormaGoal().getRequirement());

    expected = NormaFactory.getWinsNorma(3);
    suguri.normaClear();
    controller.setWinsNorma(suguri);
    assertEquals(expected, suguri.getNormaGoal());
    assertEquals(NormaFactory.getWinsNorma(3).getRequirement(), suguri.getNormaGoal().getRequirement());

    assertNotEquals(NormaFactory.getWinsNorma(3), NormaFactory.getStarsNorma(3));
    assertNotEquals(NormaFactory.getWinsNorma(3), new Object());

  }

  @Test
  public void winsNormaTest() {
    suguri.normaClear();
    suguri.normaClear();
    suguri.normaClear();
    controller.setWinsNorma(suguri);
    assertFalse(suguri.normaCheck());
    int req = suguri.getNormaGoal().getRequirement();
    suguri.increaseWinsBy(req);
    assertTrue(suguri.normaCheck());

    assertEquals(new WinsNorma(1).getRequirementNoun(), "wins");
    assertEquals(new StarsNorma(1).getRequirementNoun(), "stars");


  }


  @Test
  public void boardCircuitTest() {
    HomePanel panel1 = controller.createHomePanel(0);
    NeutralPanel panel2 = controller.createNeutralPanel(1);
    DropPanel panel3 = controller.createDropPanel(2);
    NeutralPanel panel4 = controller.createNeutralPanel(3);
    NeutralPanel panel5 = controller.createNeutralPanel(4);

    controller.setNextPanel(panel1, panel2);
    controller.setNextPanel(panel2, panel3);
    controller.setNextPanel(panel3, panel4);
    controller.setNextPanel(panel4, panel5);
    controller.setNextPanel(panel5, panel1);

    Player testSuguri = controller.createPlayer("Suguri", 4, 1, -1, 2, panel1 );

    controller.movePlayer(1);
    IPanel expectedPanel = panel2;
    assertEquals(expectedPanel, controller.getPlayerPanel(testSuguri));

    controller.movePlayer(1);
    expectedPanel = panel3;
    assertEquals(expectedPanel, controller.getPlayerPanel(testSuguri));

    controller.movePlayer(1);
    expectedPanel = panel4;
    assertEquals(expectedPanel, controller.getPlayerPanel(testSuguri));

    controller.movePlayer(1);
    expectedPanel = panel5;
    assertEquals(expectedPanel, controller.getPlayerPanel(testSuguri));

    controller.movePlayer(1);

    expectedPanel = panel1;
    assertEquals(expectedPanel, controller.getPlayerPanel(testSuguri));

  }

  @RepeatedTest(300)
  void stopAtHomeAndSelectStarsNormaTest() {
    HomePanel panel1 = controller.createHomePanel(0);
    NeutralPanel panel2 = controller.createNeutralPanel(1);
    DropPanel panel3 = controller.createDropPanel(2);
    BossPanel panel4 = controller.createBossPanel(3);
    NeutralPanel panel5 = controller.createNeutralPanel(4);

    controller.setNextPanel(panel1, panel2);
    controller.setNextPanel(panel2, panel3);
    controller.setNextPanel(panel3, panel4);
    controller.setNextPanel(panel4, panel5);
    controller.setNextPanel(panel5, panel1);

    Player player1 = controller.createPlayer("Suguri", 4, 1, -1, 2, panel2);
    player1.increaseStarsBy(200);
    controller.setPlayerHome(player1, panel1);
    controller.placePlayer(panel5);

    controller.beginTurn();
    int remSteps = controller.doMove();
    if (remSteps != 0) {
      assertEquals(player1.getCurrentPanel(), panel1);
      assertTrue(controller.getTurnState().isHomeStopChoosePhase());
      controller.stopAtHome();
      controller.selectStarsNorma();
      assertTrue(player1.getNormaGoal() instanceof StarsNorma);
    }
    if (controller.getTurnState().isNormaPickPhase()) {
      controller.selectWinsNorma();
      assertTrue(player1.getNormaGoal() instanceof WinsNorma);
    }
    assertTrue(controller.getTurnState().isEndPhase());
    controller.finishTurn();
    assertTrue(controller.getTurnState().isStartPhase());




  }

  @RepeatedTest(300)
  void stopAtHomeAndSelectWinsNormaTest() {
    HomePanel panel1 = controller.createHomePanel(0);
    NeutralPanel panel2 = controller.createNeutralPanel(1);
    DropPanel panel3 = controller.createDropPanel(2);
    BossPanel panel4 = controller.createBossPanel(3);
    NeutralPanel panel5 = controller.createNeutralPanel(4);

    controller.setNextPanel(panel1, panel2);
    controller.setNextPanel(panel2, panel3);
    controller.setNextPanel(panel3, panel4);
    controller.setNextPanel(panel4, panel5);
    controller.setNextPanel(panel5, panel1);

    Player player1 = controller.createPlayer("Suguri", 4, 1, -1, 2, panel2);
    player1.increaseStarsBy(200);
    controller.setPlayerHome(player1, panel1);
    controller.placePlayer(panel5);

    controller.beginTurn();
    int remSteps = controller.doMove();
    if (remSteps != 0) {
      assertEquals(player1.getCurrentPanel(), panel1);
      assertTrue(controller.getTurnState().isHomeStopChoosePhase());
      controller.stopAtHome();
      controller.selectWinsNorma();
      assertTrue(player1.getNormaGoal() instanceof WinsNorma);
    }
    if (controller.getTurnState().isNormaPickPhase()) {
      controller.selectWinsNorma();
      assertTrue(player1.getNormaGoal() instanceof WinsNorma);
    }
    assertTrue(controller.getTurnState().isEndPhase());
    controller.finishTurn();
    assertTrue(controller.getTurnState().isStartPhase());




  }

  @RepeatedTest(100)
  void continueMovingTest() {
    HomePanel panel1 = controller.createHomePanel(0);
    NeutralPanel panel2 = controller.createNeutralPanel(1);
    DropPanel panel3 = controller.createDropPanel(2);
    NeutralPanel panel4 = controller.createNeutralPanel(3);
    NeutralPanel panel5 = controller.createNeutralPanel(4);

    controller.setNextPanel(panel1, panel2);
    controller.setNextPanel(panel2, panel5);
    controller.setNextPanel(panel1, panel3);
    controller.setNextPanel(panel3, panel4);
    controller.setNextPanel(panel4, panel5);
    controller.setNextPanel(panel5, panel1);

    Player player1 = controller.createPlayer("Suguri", 4, 1, -1, 2, panel2);
    controller.setPlayerHome(player1, panel1);
    controller.placePlayer(panel5);

    controller.beginTurn();
    int remSteps = controller.doMove();
    if (remSteps != 0) {
      assertEquals(player1.getCurrentPanel(), panel1);
      assertTrue(controller.getTurnState().isHomeStopChoosePhase());
      remSteps = controller.continueMovingThrough(panel3);
    }
    if (remSteps == 0) {
      assertTrue(controller.getTurnState().isEndPhase());
      controller.finishTurn();
      assertTrue(controller.getTurnState().isStartPhase());
    }



  }

  @RepeatedTest(500)
  void recoveryTrialTest() {
    HomePanel panel1 = controller.createHomePanel(0);
    NeutralPanel panel2 = controller.createNeutralPanel(1);
    DropPanel panel3 = controller.createDropPanel(2);
    BossPanel panel4 = controller.createBossPanel(3);
    NeutralPanel panel5 = controller.createNeutralPanel(4);

    controller.setNextPanel(panel1, panel2);
    controller.setNextPanel(panel2, panel5);
    controller.setNextPanel(panel1, panel3);
    controller.setNextPanel(panel3, panel4);
    controller.setNextPanel(panel4, panel5);
    controller.setNextPanel(panel5, panel1);

    Player player1 = controller.createPlayer("Suguri", 4, 1, -1, 2, panel2);
    Player player2 = controller.createPlayer("Suguri 2: Electric Boogaloo", 4, 1, -1, 2, panel3);

    controller.setPlayerHome(player1, panel1);
    controller.placePlayer(panel5);

    while(!player1.isKOd()) {
      player1.defendAttack(player2, 2000);
    }

    //
    controller.beginTurn();
    //
    int preRec = player1.getRecoveryLeft();
    controller.recoveryTrial();
    int postRec = player1.getRecoveryLeft();
    assertTrue(preRec > postRec);
    if (postRec == 0) {
      // Suguri did successfully complete the recovery trial so she should be able to
      // use her turn now.
      assertTrue(controller.getTurnState().isMovingPhase());
      assertEquals(player1.getCurrentHP(),player1.getMaxHP());
    }
  }


  @Test
  void placePlayerTest() {
    HomePanel panel1 = controller.createHomePanel(0);
    NeutralPanel panel2 = controller.createNeutralPanel(1);
    DropPanel panel3 = controller.createDropPanel(2);
    BossPanel panel4 = controller.createBossPanel(3);
    NeutralPanel panel5 = controller.createNeutralPanel(4);

    controller.setNextPanel(panel1, panel2);
    controller.setNextPanel(panel2, panel3);
    controller.setNextPanel(panel3, panel4);
    controller.setNextPanel(panel4, panel5);
    controller.setNextPanel(panel5, panel1);

    Player player1 = controller.createPlayer("Suguri", 4, 1, -1, 2, panel1);
    IPanel expected;

    assertEquals(player1.getCurrentPanel(),panel1);
    List<IPanel> testPanels = new ArrayList<>();
    testPanels.add(panel1);
    testPanels.add(panel2);
    testPanels.add(panel3);
    testPanels.add(panel4);
    testPanels.add(panel5);

    for (IPanel c: testPanels) {
      expected = c;
      controller.placePlayer(c);
      assertTrue(expected.getPlayers().contains(player1));
      assertEquals(player1.getCurrentPanel(), expected);
    }

  }


  @Test
  public void turnsAndChapterTest() {
    Player testSuguri1 = controller.createPlayer("Suguri", 4, 1, -1, 2, NullPanel.getNullPanel() );
    Player testSuguri2 = controller.createPlayer("Suguri2", 4, 1, -1, 2, NullPanel.getNullPanel() );

    assertEquals(controller.getTurnOwner(), testSuguri1, "First player should have the turn at the beginning!!");
    controller.endTurn();
    assertEquals(controller.getTurnOwner(), testSuguri2, "Second player should have the turn at the beginning!!");
    assertEquals(controller.getChapter(), 1, "Chapter shouldn't have ended yet!!");
    controller.endTurn();
    assertEquals(controller.getTurnOwner(), testSuguri1, "First player should have the turn after the end of the first chapter!!");
    assertEquals(controller.getChapter(), 2, "Chapter shouldn't have ended already!!");
  }

  @RepeatedTest(50)
  void uniqueBossTest() {
    BossUnit boss1 = controller.getCurrentBossUnit();
    BossUnit boss2 = controller.getCurrentBossUnit();
    assertEquals(boss1, boss2);
    controller.clearCurrentBossUnit();
    BossUnit boss3 = controller.getCurrentBossUnit();
    boss3.setCurrentHP(0);
    BossUnit boss4 = controller.getCurrentBossUnit();
    // Checking for hashCode to know they're different instances
    // equal could actually say they're the same because of their properties.
    assertNotEquals(boss1.hashCode(), boss3.hashCode());
    assertNotEquals(boss4.hashCode(), boss3.hashCode());
  }

  @RepeatedTest(50)
  void globalWildUnitTest() {
    WildUnit wild1 = controller.getCurrentWildUnit();
    WildUnit wild2 = controller.getCurrentWildUnit();
    assertEquals(wild1,wild2);
    wild1.setCurrentHP(0);
    WildUnit wild3 = controller.getCurrentWildUnit();
    assertNotEquals(wild2,wild3);

  }

  @RepeatedTest(50)
  void movePlayerTest() {

    List<IPanel> panels = new ArrayList();
    for(int i=0;i<=16;i++) {
      panels.add(controller.createNeutralPanel(i)); }
    for(int i=0;i<=16;i++) {
      controller.setNextPanel(panels.get(i),panels.get((i+1) % 17));
      }

    controller.createPlayer("Suguri", 4, 1, -1, 2, panels.get(0));
    int res = controller.movePlayer();
    assertTrue((res >= 0) && (res <=6));

  }

  @Test
  void practiceBoardTest() {
    IPanel[][] matrix = controller.generatePracticeBoard();

    assertEquals(9,matrix.length);
    assertEquals(9,matrix[0].length);

    assertTrue(matrix[2][6].getNextPanels().contains(matrix[3][6]));
  }

  @Test
  void newGameTest() {
    assertNull(controller.getBoardMatrix());
    assertEquals(0,controller.getPlayers().size());
    controller.newGame();
    assertNotNull(controller.getBoardMatrix());
    assertEquals(4,controller.getPlayers().size());

  }



}
