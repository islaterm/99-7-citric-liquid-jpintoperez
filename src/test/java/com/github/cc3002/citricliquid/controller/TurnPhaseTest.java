package com.github.cc3002.citricliquid.controller;

import com.github.cc3002.citricjuice.model.unit.Player;
import com.github.cc3002.citricliquid.controller.gameflowstates.StartPhase;
import com.github.cc3002.citricliquid.controller.gameflowstates.TurnPhase;
import com.github.cc3002.citricliquid.controller.gameflowstates.TurnState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TurnPhaseTest {
  TurnState state;
  private Player suguri;
  private Player kai;

  @BeforeEach
  void phaseSetUp() {
    state = new TurnState();
  }

  @Test
  void TurnStateTest() {

    TurnPhase phase = state.getTurnPhase();
    assertTrue(phase instanceof StartPhase);

  }

  @Test
  void MoveWithoutCombatTest() {
    // A valid game flow would be
    // StartPhase -> CardPickPhase -> MovingPhase -> CombatChoosePhase -> MovingPhase -> EndPhase
    state.cardPickPhase();
    assertTrue(state.isCardPickPhase());
    state.movingPhase(-1);
    assertTrue(state.isMovingPhase());
    state.combatChoosePhase(3);
    assertTrue(state.isCombatChoosePhase());
    assertEquals(3, state.getSteps());
    state.movingPhase(-1);
    assertTrue(state.isMovingPhase());
    state.endPhase();
    assertTrue(state.isEndPhase());
    assertFalse(state.isNormaPickPhase());
  }

  @Test
  void MoveWithCombatTest() {
    // A valid game flow would be
    // StartPhase -> CardPickPhase -> MovingPhase -> CombatChoosePhase -> CombatResponseChoosePhase -> CounterattackPhase -> CounterattackResponseChoosePhase -> EndPhase
    Player attacker = new Player("Suguri", 4, 1, -1, 2 );
    int attackValue = 3;
    Player victim = new Player("Kai", 5, 1, 0, 0 );
    assertTrue(state.isStartPhase());
    state.cardPickPhase();
    assertTrue(state.isCardPickPhase());
    state.movingPhase(-1);
    assertTrue(state.isMovingPhase());
    state.combatChoosePhase(3);
    assertTrue(state.isCombatChoosePhase());
    state.combatResponseChoosePhase( attacker, attackValue, victim);
    assertTrue(state.isCombatResponseChoosePhase());
    assertEquals(attackValue, state.getAttackValue());
    assertEquals(attacker, state.getAttacker());
    assertEquals(victim, state.getTarget());
    state.counterattackPhase(attacker);
    assertTrue(state.isCounterattackPhase());
    assertEquals(attacker, state.getAttacker());
    state.counterattackResponseChoosePhase(victim,5,attacker);
    assertTrue(state.isCounterattackResponseChoosePhase());
    assertEquals(victim, state.getAttacker());
    assertEquals(5, state.getAttackValue());
    assertEquals(attacker, state.getTarget());

    state.endPhase();
    assertTrue(state.isEndPhase());
  }

  @Test
  void MoveChoosingPathTest() {
    // A valid game flow would be
    // StartPhase -> CardPickPhase -> MovingPhase -> PathChoosePhase -> MovingPhase -> EndPhase
    assertTrue(state.isStartPhase());
    state.cardPickPhase();
    assertTrue(state.isCardPickPhase());
    state.movingPhase(-1);
    assertTrue(state.isMovingPhase());
    state.pathChoosePhase(4);
    assertTrue(state.isPathChoosePhase());
    assertEquals(4, state.getSteps());
    state.movingPhase(-1);
    assertTrue(state.isMovingPhase());
    state.endPhase();
    assertTrue(state.isEndPhase());
  }

  @Test
  void StoppingAtHomeTest() {
    // A valid game flow would be
    // StartPhase -> CardPickPhase -> MovingPhase -> HomeStopChoosePhase -> EndPhase
    assertTrue(state.isStartPhase());
    state.cardPickPhase();
    assertTrue(state.isCardPickPhase());
    state.movingPhase(-1);
    assertTrue(state.isMovingPhase());
    assertEquals(-1,state.getSteps());
    state.setSteps(5);
    assertEquals(5,state.getSteps());
    state.homeStopChoosePhase(4);
    assertTrue(state.isHomeStopChoosePhase());
    assertEquals(4, state.getSteps());
    state.normaPickPhase();
    assertTrue(state.isNormaPickPhase());
    state.endPhase();
    assertTrue(state.isEndPhase());
    // Game should be capable to make the turn cycle start again so we can go to StartPhase again
    state.startPhase();
    assertTrue(state.isStartPhase());
  }

  @Test
  void NotStoppingAtHomeTest() {
    // A valid game flow would be
    // StartPhase -> CardPickPhase -> MovingPhase -> HomeStopChoosePhase -> MovingPhase -> EndPhase
    assertTrue(state.isStartPhase());
    state.cardPickPhase();
    assertTrue(state.isCardPickPhase());
    state.movingPhase(-1);
    assertTrue(state.isMovingPhase());
    state.homeStopChoosePhase(4);
    assertTrue(state.isHomeStopChoosePhase());
    assertEquals(4, state.getSteps());
    state.movingPhase(-1);
    assertTrue(state.isMovingPhase());
    state.endPhase();
    assertTrue(state.isEndPhase());
    // Game should be capable to make the turn cycle start again so we can go to StartPhase again
    state.startPhase();
    assertTrue(state.isStartPhase());
  }

  @Test
  void incorrectStateTest() {
    // We start
    assertTrue(state.isStartPhase());
    state.recoveryPhase();
    assertTrue(state.isRecoveryPhase());
    state.endPhase();
    // Game should be capable to make the turn cycle start again so we can go to StartPhase.
    assertTrue(state.isEndPhase());
    state.startPhase();
    assertTrue(state.isStartPhase());
  }

  @Test
  void recoveryTrialFailPhaseTest() {
    // The state starts in startPhase
    assertFalse(state.isEndPhase());
    assertFalse(state.isCardPickPhase());
    assertFalse(state.isCombatChoosePhase());
    assertFalse(state.isCombatResponseChoosePhase());
    assertFalse(state.isCounterattackPhase());
    assertFalse(state.isCounterattackResponseChoosePhase());
    assertFalse(state.isEndPhase());
    assertFalse(state.isHomeStopChoosePhase());
    assertFalse(state.isMovingPhase());
    assertFalse(state.isPathChoosePhase());
    assertFalse(state.isRecoveryPhase());

    state.cardPickPhase();
    assertFalse(state.isStartPhase());

  }

  @Test
  void recoveryTrialPassPhaseTest() {
    // A valid game flow would be
    // StartPhase -> RecoveryPhase -> CardPickPhase -> movingPhase -> EndPhase -> StartPhase
    assertTrue(state.isStartPhase());
    state.recoveryPhase();
    assertTrue(state.isRecoveryPhase());
    state.cardPickPhase();
    assertTrue(state.isCardPickPhase());
    state.movingPhase(-1);
    assertTrue(state.isMovingPhase());
    state.endPhase();
    assertTrue(state.isEndPhase());
    // Game should be capable to make the turn cycle start again so we can go to StartPhase.
    state.startPhase();
    assertTrue(state.isStartPhase());
  }


  @Test
  void InvalidCycle() {
    Player attacker = new Player("Suguri", 4, 1, -1, 2 );
    int attackValue = 3;
    Player victim = new Player("Kai", 5, 1, 0, 0 );
    Assertions.assertThrows(AssertionError.class, () -> state.combatChoosePhase(3), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.combatResponseChoosePhase(attacker, attackValue, victim), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.counterattackPhase(victim), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.counterattackResponseChoosePhase(victim, attackValue, attacker), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.endPhase(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.homeStopChoosePhase(3), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.movingPhase(-1), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.pathChoosePhase(3), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.startPhase(), "Should have thrown error by trying an illegal turn phase transition.");

    Assertions.assertThrows(AssertionError.class, () -> state.startPhase(), "Should have thrown error by trying an illegal turn phase transition.");

    state.cardPickPhase();
    Assertions.assertThrows(AssertionError.class, () -> state.cardPickPhase(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.getSteps(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.getAttacker(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.getAttackValue(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.getTarget(), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.recoveryPhase(), "Should have thrown error by trying an illegal turn phase transition.");

    Assertions.assertThrows(AssertionError.class, () -> state.setSteps(3), "Should have thrown error by trying an illegal turn phase transition.");
    Assertions.assertThrows(AssertionError.class, () -> state.normaPickPhase(), "Should have thrown error by trying an illegal turn phase transition.");

  }




}
