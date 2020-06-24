package com.github.cc3002.citricliquid.controller;

import com.github.cc3002.citricjuice.model.board.HomePanel;
import com.github.cc3002.citricjuice.model.board.IPanel;
import com.github.cc3002.citricjuice.model.unit.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// This set of tests is supposed to simulate the flow of the game and its mechanics and see if it successfully
// changes between each one of them and performs the expected actions.
public class GameFlowTest {
  GameController controller;
  IPanel panel0;
  IPanel panel1;
  IPanel panel2;
  IPanel panel3;
  IPanel panel4;
  IPanel panel5;
  IPanel panel6;
  Player suguri;
  Player kai;

  @BeforeEach
  void setUp() {
    controller = new GameController();

    panel0 = controller.createHomePanel(0);
    panel1 = controller.createNeutralPanel(1);
    panel2 = controller.createNeutralPanel(2);
    panel3 = controller.createNeutralPanel(3);
    panel4 = controller.createBonusPanel(4);
    panel5 = controller.createNeutralPanel(5);
    panel6 = controller.createHomePanel(6);

    controller.setNextPanel(panel0,panel1);
    controller.setNextPanel(panel1,panel2);
    controller.setNextPanel(panel2,panel3);
    controller.setNextPanel(panel3,panel4);
    controller.setNextPanel(panel4,panel5);
    controller.setNextPanel(panel5,panel6);
    controller.setNextPanel(panel6,panel0);

    suguri = controller.createPlayer("Suguri", 4, 1, -1, 2, panel0 );
    controller.setPlayerHome(suguri, (HomePanel) panel0);

    kai    = controller.createPlayer("Kai", 5, 1, 0, 0, panel4);
    controller.setPlayerHome(kai, (HomePanel) panel6);

  }

  // Tests moving and attacking if it collides with another player.
  @RepeatedTest(500)
  void moveAndAttackEvdTurnTest() {

    Random random = new Random();
    Random suguriRandom = new Random();
    long testSeed = random.nextLong();

    suguri.setSeed(testSeed);
    suguriRandom.setSeed(testSeed);

    List<IPanel> circuit = List.of(panel0,panel1,panel2,panel3,panel4,panel5,panel6);

    // We're currently in startPhase in chapter 1
    // Suguri should receive 1 star on the beginning of the turn.
    controller.beginTurn();
    assertEquals(1,suguri.getStars());
    // Pick no card to use so we can get to the moving phase.
    controller.useCard();
    // Now we roll the dice and move.
    controller.doMove();
    int finalPanel = Math.min(suguriRandom.nextInt(6)+1,4);
    IPanel expectedPanel = circuit.get(finalPanel % circuit.size());
    assertEquals(expectedPanel, suguri.getCurrentPanel());
    if (suguri.getCurrentPanel()==panel4) {
      // Suguri landed at the same place as Kai so we must decide if to start a combat or continue
      controller.startCombat();
      // Attack is on its way so now it's time for Kai to decide if to defend or evade.
      controller.evadeAgainstCombat();
      if (!kai.isKOd()) {
        // If Kai's still alive he has capability to counterattack.
        controller.startCounterAttack();
        // Suguri now has to choose if to evade or defend, arbitrarily we'll pick defend.
        controller.defendAgainstCounterattack();
      }
      controller.finishTurn();
      assertTrue(controller.getTurnState().isStartPhase());
    }

  }

  // Tests moving and attacking if it collides with another player, this time the victim defends.
  @RepeatedTest(500)
  void moveAndAttackDefTurnTest() {

    Random random = new Random();
    Random suguriRandom = new Random();
    long testSeed = random.nextLong();

    suguri.setSeed(testSeed);
    suguriRandom.setSeed(testSeed);

    List<IPanel> circuit = List.of(panel0,panel1,panel2,panel3,panel4,panel5,panel6);

    // We're currently in startPhase in chapter 1
    // Suguri should receive 1 star on the beginning of the turn.
    controller.beginTurn();
    assertEquals(1,suguri.getStars());
    // Pick no card to use so we can get to the moving phase.
    controller.useCard();
    // Now we roll the dice and move.
    controller.doMove();
    int finalPanel = Math.min(suguriRandom.nextInt(6)+1,4);
    IPanel expectedPanel = circuit.get(finalPanel % circuit.size());
    assertEquals(suguri.getCurrentPanel(), expectedPanel);
    if (suguri.getCurrentPanel()==panel4) {
      // Suguri landed at the same place as Kai so we must decide if to start a combat or continue
      controller.startCombat();
      // Attack is on its way so now it's time for Kai to decide if to defend or evade.
      controller.defendAgainstCombat();
      if (!kai.isKOd()) {
        // If kai didn't get knocked out he is capable of counterattacking now.
        controller.startCounterAttack();
        // Suguri now has to choose if to evade or defend, arbitrarily we'll pick evade
        controller.evadeAgainstCounterattack();
      }

      controller.finishTurn();
      assertTrue(controller.getTurnState().isStartPhase());
    }

  }

  // Tests moving and not attacking, it might also stop .
  @RepeatedTest(500)
  void moveWithoutAttackTurnTest() {

    Random random = new Random();
    Random suguriRandom = new Random();
    long testSeed = random.nextLong();

    suguri.setSeed(testSeed);
    suguriRandom.setSeed(testSeed);

    List<IPanel> circuit = List.of(panel0,panel1,panel2,panel3,panel4,panel5,panel6);

    // We're currently in startPhase in chapter 1
    // Suguri should receive 1 star on the beginning of the turn.
    controller.beginTurn();
    assertEquals(1,suguri.getStars());
    // Pick no card to use so we can get to the moving phase.
    controller.useCard();
    // Now we roll the dice and move.
    controller.doMove();
    int roll = suguriRandom.nextInt(6)+1;
    int stopAtPanel = Math.min(roll,4);
    int finalPanel = (roll % circuit.size());

    assertEquals(suguri.getCurrentPanel(), circuit.get(stopAtPanel));
    if (suguri.getCurrentPanel()==panel4) {
      // Suguri landed at the same place as Kai so we must decide if to start a combat or continue
      controller.continueMoving();
      int additionalMove = roll-stopAtPanel;
      if (additionalMove > 3) { additionalMove = 3; }
      // Suguri decided to continue moving, we must check where is she going to end.
      // technically she can't go further than her homePanel because she will be asked if to stop or to continue.
      assertEquals(suguri.getCurrentPanel(),circuit.get(stopAtPanel + additionalMove % 7));
    }

  }


}
