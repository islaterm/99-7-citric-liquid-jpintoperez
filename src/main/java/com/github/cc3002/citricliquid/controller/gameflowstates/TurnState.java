package com.github.cc3002.citricliquid.controller.gameflowstates;

import com.github.cc3002.citricjuice.model.unit.IUnit;
import com.github.cc3002.citricjuice.model.unit.Player;

public class TurnState {
  TurnPhase phase;

  public TurnState() {
    this.setTurnPhase(new StartPhase());
  }

  /**
   * Sets a certain phase on the state.
   * @param phase
   *  turnPhase object to be setted as
   */
  public void setTurnPhase(TurnPhase phase) {
    this.phase = phase;
    phase.setTurnState(this);
  }

  /**
   * Transition method to recovery phase
   */
  public void recoveryPhase() { phase.recoveryPhase(); }

  /**
   * Transition method to start phase
   */
  public void startPhase() { phase.startPhase(); }

  /**
   * Transition method to card pick phase
   */
  public void cardPickPhase() { phase.cardPickPhase(); }

  /**
   * Transition method to moving phase
   */
  public void movingPhase() { phase.movingPhase(); }
  /**
   * Transition method to path choose phase
   */
  public void pathChoosePhase(int steps) { phase.pathChoosePhase(steps); }
  /**
   * Transition method to combat choose phase
   */
  public void combatChoosePhase(int steps) { phase.combatChoosePhase(steps); }
  /**
   * Transition method to response choose phase
   */
  public void combatResponseChoosePhase(Player attacker, int attackValue, Player target) { phase.combatResponseChoosePhase(attacker, attackValue, target); }

  /**
   * Transition method to stop choose phase
   */
  public void homeStopChoosePhase(int steps) { phase.homeStopChoosePhase(steps); }

  /**
   * Transition method to counterattack phase
   */
  public void counterattackPhase(IUnit target) { phase.counterattackPhase(target); }

  /**
   * Transition method to counterattack response choose phase
   */
  public void counterattackResponseChoosePhase(IUnit attacker, int attackValue, IUnit target) { phase.counterattackResponseChoosePhase(attacker, attackValue, target); }
  /**
   * Transition method to end phase
   */
  public void endPhase() { phase.endPhase(); }


  /**
   * Getter method to access the attacker reference
   * @return
   *    reference of the attacker unit
   */
  public IUnit getAttacker() { return phase.getAttacker(); }

  /**
   * Getter method to obtain the incoming attack value
   * @return
   *    amount of pending possible damage.
   */
  public int getAttackValue() { return phase.getAttackValue(); }

  /**
   * Getter method to obtain the remaining steps
   * @return
   *    amount of pending steps.
   */
  public int getSteps() { return phase.getSteps(); }

  /**
   * Getter method to access the target reference
   * @return
   *   reference of the target unit.
   */
  public IUnit getTarget() { return phase.getTarget(); }

  /**
   * Boolean method to check start phase
   * @return
   *   whether the state is currently on that phase.
   */
  public boolean isStartPhase() {
    return phase.isStartPhase();
  }

  /**
   * Boolean method to check card pick phase
   * @return
   *    whether the state is currently on that phase.
   */
  public boolean isCardPickPhase() {
    return phase.isCardPickPhase();
  }

  /**
   * Boolean method to check moving phase
   * @return
   *    whether the state is currently on that phase.
   */
  public boolean isMovingPhase() {
    return phase.isMovingPhase();
  }

  /**
   * Boolean method to check path choose phase
   * @return
   *    whether the state is currently on that phase.
   */
  public boolean isPathChoosePhase() {
    return phase.isPathChoosePhase();
  }

  /**
   * Boolean method to check combat choose phase
   * @return
   *    whether the state is currently on that phase.
   */
  public boolean isCombatChoosePhase() {
    return phase.isCombatChoosePhase();
  }

  /**
   * Boolean method to check combat response choose phase
   * @return
   *    whether the state is currently on that phase.
   */
  public boolean isCombatResponseChoosePhase() {
    return phase.isCombatResponseChoosePhase();
  }

  /**
   * Boolean method to check home stop choose phase
   * @return
   *    whether the state is currently on that phase.
   */
  public boolean isHomeStopChoosePhase() {
    return phase.isHomeStopChoosePhase();
  }
  /**
   * Boolean method to check counterattack phase
   * @return
   *    whether the state is currently on that phase.
   */
  public boolean isCounterattackPhase() {
    return phase.isCounterattackPhase();
  }
  /**
   * Boolean method to check counterattack response choose phase
   * @return
   *    whether the state is currently on that phase.
   */
  public boolean isCounterattackResponseChoosePhase() { return phase.isCounterattackResponseChoosePhase(); }
  /**
   * Boolean method to check end phase
   * @return
   *    whether the state is currently on that phase.
   */
  public boolean isEndPhase() { return phase.isEndPhase(); }
  /**
   * Boolean method to check recovery phase
   * @return
   *    whether the state is currently on that phase.
   */
  public boolean isRecoveryPhase() { return phase.isRecoveryPhase(); }

}
