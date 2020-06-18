package com.github.cc3002.citricliquid.controller;

import com.github.cc3002.citricjuice.model.board.IPanel;
import com.github.cc3002.citricjuice.model.unit.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnPhaseTest {
  GameController controller = new GameController();
  private Player suguri;
  private Player kai;

  @BeforeEach
  void ControllerSetUp() {
    List<Player> Players = new ArrayList();
    controller = new GameController();
    IPanel panel1 = controller.createNeutralPanel(0);
    IPanel panel2 = controller.createNeutralPanel(1);
    IPanel panel3 = controller.createNeutralPanel(2);
    IPanel panel4 = controller.createNeutralPanel(3);
    IPanel panel5 = controller.createNeutralPanel(4);

    controller.setNextPanel(panel1,panel2);
    controller.setNextPanel(panel2,panel3);
    controller.setNextPanel(panel3,panel4);
    controller.setNextPanel(panel4,panel5);
    controller.setNextPanel(panel5,panel1);

    suguri = controller.createPlayer("Suguri",4,1,-1, 2, panel1);
    kai = controller.createPlayer("Kai",5,1,0, 0, panel2);
  }

  @RepeatedTest(50)
  void combatDefendTest() {
    Random suguriRandom = new Random();
    Random kaiRandom = new Random();
    final long testSeed = new Random().nextLong();
    suguriRandom.setSeed(testSeed);
    kaiRandom.setSeed(testSeed);
    suguri.setSeed(testSeed);
    kai.setSeed(testSeed);

    int expectedSuguriAtk = suguri.getAtk() + suguriRandom.nextInt(6) + 1;
    int expectedKaiDef = kai.getDef() + kaiRandom.nextInt(6) + 1;
    int prevHP = kai.getCurrentHP();
    int expectedHP = prevHP - Math.max(1, expectedSuguriAtk - (expectedKaiDef));
    // Suguri's Turn: Receive stars
    controller.beginTurn();
    controller.useCard(); // Pick no card
    controller.movePlayer(3); // Try to move (forced 3)
    controller.startCombat();
    // Kai has to answer this combat request
    controller.defendAgainstCombat();
    assertEquals(expectedHP, kai.getCurrentHP());
    controller.finishTurn();
  }

  @RepeatedTest(50)
  void combatEvadeTest() {
    Random suguriRandom = new Random();
    Random kaiRandom = new Random();
    final long testSeed = new Random().nextLong();
    suguriRandom.setSeed(testSeed);
    kaiRandom.setSeed(testSeed);
    suguri.setSeed(testSeed);
    kai.setSeed(testSeed);

    int expectedSuguriAtk = suguri.getAtk() + suguriRandom.nextInt(6) + 1;
    int expectedKaiEvd = kai.getEvd() + kaiRandom.nextInt(6) + 1;
    int prevHP = kai.getCurrentHP();
    int expectedHP = Math.max(0,prevHP - expectedSuguriAtk);
    if (expectedKaiEvd > expectedSuguriAtk) {
      expectedHP = prevHP;
    }

    // Suguri's Turn: Receive stars
    controller.beginTurn();
    controller.useCard(); // Pick no card
    controller.movePlayer(3); // Try to move (forced 3)
    controller.startCombat();
    // Kai has to answer this combat request
    controller.evadeAgainstCombat();
    assertEquals(expectedHP, kai.getCurrentHP());
    controller.finishTurn();
  }


}
