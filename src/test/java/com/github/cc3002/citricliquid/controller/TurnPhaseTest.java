package com.github.cc3002.citricliquid.controller;

import com.github.cc3002.citricjuice.model.unit.Player;
import com.github.cc3002.citricliquid.controller.gameflowstates.TurnState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnPhaseTest {
  TurnState state;
  private Player suguri;
  private Player kai;

  @BeforeEach
  void phaseSetUp() {
    List<Player> Players = new ArrayList();
    state = new TurnState();
  }

  @Test
  void MoveWithoutCombatTest() {
    // A valid game flow would be
    // StartPhase -> CardPickPhase -> MovingPhase -> CombatChoosePhase -> MovingPhase -> EndPhase
    state.cardPickPhase();
    state.movingPhase();
    state.combatChoosePhase(3);
    assertEquals(3, state.getSteps());
    state.movingPhase();
    state.endPhase();
  }

  @Test
  void MoveWithCombatTest() {
    // A valid game flow would be
    // StartPhase -> CardPickPhase -> MovingPhase -> CombatChoosePhase -> CombatResponseChoosePhase -> EndPhase
    Player attacker = new Player("Suguri", 4, 1, -1, 2 );
    int attackValue = 3;
    Player victim = new Player("Kai", 5, 1, 0, 0 );
    state.cardPickPhase();
    state.movingPhase();
    state.combatChoosePhase(3);
    state.combatResponseChoosePhase( attacker, attackValue, victim);
    assertEquals(attackValue, state.getAttackValue());
    assertEquals(attacker, state.getAttacker());
    assertEquals(victim, state.getTarget());
    state.endPhase();
  }

  @Test
  void MoveChoosingPathTest() {
    // A valid game flow would be
    // StartPhase -> CardPickPhase -> MovingPhase -> PathChoosePhase -> MovingPhase -> EndPhase
    state.cardPickPhase();
    state.movingPhase();
    state.pathChoosePhase(4);
    assertEquals(4, state.getSteps());
    state.movingPhase();
    state.endPhase();
  }

  @Test
  void StoppingAtHomeTest() {
    // A valid game flow would be
    // StartPhase -> CardPickPhase -> MovingPhase -> HomeStopChoosePhase -> EndPhase
    state.cardPickPhase();
    state.movingPhase();
    state.homeStopChoosePhase(4);
    assertEquals(4, state.getSteps());
    state.endPhase();
    // Game should be capable to make the turn cycle start again so we can go to StartPhase again
    state.startPhase();
  }

  @Test
  void NotStoppingAtHomeTest() {
    // A valid game flow would be
    // StartPhase -> CardPickPhase -> MovingPhase -> HomeStopChoosePhase -> MovingPhase -> EndPhase
    state.cardPickPhase();
    state.movingPhase();
    state.homeStopChoosePhase(4);
    assertEquals(4, state.getSteps());
    state.movingPhase();
    state.endPhase();
    // Game should be capable to make the turn cycle start again so we can go to StartPhase again
    state.startPhase();
  }


  @Test
  void InvalidCycle() {
    Player attacker = new Player("Suguri", 4, 1, -1, 2 );
    int attackValue = 3;
    Player victim = new Player("Kai", 5, 1, 0, 0 );
    Assertions.assertThrows(AssertionError.class, () -> state.combatChoosePhase(3), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.combatResponseChoosePhase(attacker, attackValue, victim), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.endPhase(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.homeStopChoosePhase(3), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.movingPhase(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.pathChoosePhase(3), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.startPhase(), "Should have thrown error by trying an illegal turn phase transition.");

    Assertions.assertThrows(AssertionError.class, () -> state.startPhase(), "Should have thrown error by trying an illegal turn phase transition.");

    state.cardPickPhase();
    Assertions.assertThrows(AssertionError.class, () -> state.cardPickPhase(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.getSteps(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.getAttacker(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.getAttackValue(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.getTarget(), "Should have thrown error by trying an illegal turn phase transition.");

  }



}
