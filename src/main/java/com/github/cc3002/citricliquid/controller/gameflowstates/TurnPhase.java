package com.github.cc3002.citricliquid.controller.gameflowstates;

import com.github.cc3002.citricjuice.model.unit.IUnit;
import com.github.cc3002.citricjuice.model.unit.Player;

public class TurnPhase {

  private TurnState state;

  /**
   * Links the TurnState object to store this phase.
   * @param state
   *  the TurnState object to be linked.
   */
  public void setTurnState(TurnState state) {
    this.state = state;
  }

  /**
   * Changes the phase by calling the state's manager method.
   * @param phase
   *  New phase to be stored
   */
  protected void changeTurnPhase(TurnPhase phase) {
    state.setTurnPhase(phase);
  }

  /**
   * Function that throws an error to show that a phase transition was illegal.
   */
  void error() { throw new AssertionError("Invalid transition"); }

  /**
   * Transition to recovery phase.
   */
  public void recoveryPhase() { error(); }

  /**
   * Transition to start phase
   */
  public void startPhase() { error(); }

  /**
   * Transition to card pick phase
   */
  public void cardPickPhase() { error(); }

  /**
   * Transition to moving phase.
   */
  public void movingPhase(int steps) { error(); }

  /**
   * Transition to path choose phase, it must store the remaining steps.
   * @param steps
   *  pending steps
   */
  public void pathChoosePhase(int steps) { error(); }

  /**
   * Transition to norma pick phase.
   */
  public void normaPickPhase() { error(); }

  /**
   * Transition to combat choose phase, it must store the remaining steps.
   * @param steps
   *  pending steps
   */
  public void combatChoosePhase(int steps) { error(); }

  /**
   * Transition to combat response choose phase, it must store the battle
   * information to apply the battle later.
   * @param attacker
   *  attacker unit's reference
   * @param attackValue
   *  incoming damage value
   * @param target
   *  target unit's reference
   */
  public void combatResponseChoosePhase(IUnit attacker, int attackValue, IUnit target) { error(); }

  /**
   * Transition to counterattack phase, it must store the reference of
   * the counterattacker due that the target is always the turn owner.
   * @param counterattacker
   *  counterattacker's reference
   */
  public void counterattackPhase(IUnit counterattacker) { error(); }

  /**
   * Transition to counter attack response choose phase, it must store
   * the battle information to apply the attack action.
   * @param attacker
   *  attacker unit's reference
   * @param attackValue
   *  incoming damage value
   * @param target
   *  target unit's reference
   */
  public void counterattackResponseChoosePhase(IUnit attacker, int attackValue, IUnit target) { error(); }

  /**
   * Transition to home stop choose phase, it must store the steps remaining
   * @param steps
   *  pending steps
   */
  public void homeStopChoosePhase(int steps) { error(); }

  /**
   * Transition to end phase.
   */
  public void endPhase() { error(); }

  /**
   * Getter method to access the attacker reference.
   * @return
   *  attacker reference
   */
  public IUnit getAttacker() { error(); return new Player("Error",1,1,1,1); }

  /**
   * Getter method to get the attack value
   * @return
   *  incoming damage value
   */
  public int getAttackValue() { error(); return 0; }

  /**
   * Getter method to get the steps remaining from the movement.
   * @return
   *  remaining steps
   */
  public int getSteps() { error(); return 0; }

  /**
   * Setter method to set the steps remaining from the movement.
   * @return
   *  remaining steps
   */
  public void setSteps(int steps) { error(); }

  /**
   * Getter method to get the target reference.
   * @return
   *  target reference
   */
  public IUnit getTarget() { error(); return new Player("Error",1,1,1,1); }

  /**
   * Boolean method to check start phase
   * @return
   *  whether it is the current phase or not
   */
  public boolean isStartPhase() { return false; }

  /**
   * Boolean method to check card pick phase
   * @return
   *  whether it is the current phase or not
   */
  public boolean isCardPickPhase() {
    return false;
  }

  /**
   * Boolean method to check card moving phase
   * @return
   *  whether it is the current phase or not
   */
  public boolean isMovingPhase() {
    return false;
  }

  /**
   * Boolean method to check path choose phase
   * @return
   *  whether it is the current phase or not
   */
  public boolean isPathChoosePhase() {
    return false;
  }

  /**
   * Boolean method to check combat choose phase
   * @return
   *   whether it is the current phase or not
   */
  public boolean isCombatChoosePhase() {
    return false;
  }

  /**
   * Boolean method to check combat response choose phase
   * @return
   *  whether it is the current phase or not
   */
  public boolean isCombatResponseChoosePhase() {
    return false;
  }

  /**
   * Boolean method to check home stop choose phase
   * @return
   *  whether it is the current phase or not
   */
  public boolean isHomeStopChoosePhase() {
    return false;
  }

  /**
   * Boolean method to check counterattack phase
   * @return
   *  whether it is the current phase or not
   */
  public boolean isCounterattackPhase() {
    return false;
  }

  /**
   * Boolean method to check counterattack response choose phase
   * @return
   *  whether it is the current phase or not
   */
  public boolean isCounterattackResponseChoosePhase() { return false; }

  /**
   * Boolean method to check end phase.
   * @return
   *  whether it is the current phase or not
   */
  public boolean isEndPhase() { return false; }

  /**
   * Boolean method to check recovery phase.
   * @return
   *  whether it is the current phase or not
   */
  public boolean isRecoveryPhase() { return false; }

  /**
   * Boolean method to check NormaPick phase.
   * @return
   *  whether it is the current phase or not
   */
  public boolean isNormaPickPhase() { return false; }
}
