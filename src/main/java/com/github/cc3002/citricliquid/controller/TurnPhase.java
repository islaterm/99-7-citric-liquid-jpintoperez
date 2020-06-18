package com.github.cc3002.citricliquid.controller;

import com.github.cc3002.citricjuice.model.unit.Player;

public class TurnPhase {

  private GameController controller;

  public void setController(GameController controller) {
    this.controller = controller;
  }

  protected void changeTurnPhase(TurnPhase phase) {
    controller.setTurnPhase(phase);
  }

  void error() { throw new RuntimeException(); };

  void startPhase() { error(); }
  void cardPickPhase() { error(); }
  void movingPhase() { error(); }
  void pathChoosePhase() { error(); }
  void combatChoosePhase(int steps) { error(); }
  void combatResponseChoosePhase(Player attacker, int attackValue, Player target) { error(); }
  void homeStopChoosePhase(int steps) { error(); }
  void endPhase() { error(); };

  Player getAttacker() { error(); return new Player("Error",1,1,1,1); }
  int getAttackValue() { error(); return 0; }
  int getSteps() { error(); return 0; }
  Player getTarget() { error(); return new Player("Error",1,1,1,1); }
}
