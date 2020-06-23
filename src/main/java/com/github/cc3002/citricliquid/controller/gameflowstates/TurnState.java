package com.github.cc3002.citricliquid.controller.gameflowstates;

import com.github.cc3002.citricjuice.model.unit.Player;

public class TurnState {
  TurnPhase phase;

  public TurnState() {
    this.setTurnPhase(new StartPhase());
  }

  /**
   * Sets a certain phase on the state.
   * @param phase
   */
  public void setTurnPhase(TurnPhase phase) {
    this.phase = phase;
    phase.setTurnState(this);
  }

  public void startPhase() { phase.startPhase(); }
  public void cardPickPhase() { phase.cardPickPhase(); }
  public void movingPhase() { phase.movingPhase(); }
  public void pathChoosePhase(int steps) { phase.pathChoosePhase(steps); }
  public void combatChoosePhase(int steps) { phase.combatChoosePhase(steps); }
  public void combatResponseChoosePhase(Player attacker, int attackValue, Player target) { phase.combatResponseChoosePhase(attacker, attackValue, target); }
  public void homeStopChoosePhase(int steps) { phase.homeStopChoosePhase(steps); }
  public void endPhase() { phase.endPhase(); };

  public Player getAttacker() { return phase.getAttacker(); }
  public int getAttackValue() { return phase.getAttackValue(); }
  public int getSteps() { return phase.getSteps(); }
  public Player getTarget() { return phase.getTarget(); }


}
