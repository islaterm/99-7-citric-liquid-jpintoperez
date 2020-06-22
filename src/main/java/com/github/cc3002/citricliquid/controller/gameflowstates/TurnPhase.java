package com.github.cc3002.citricliquid.controller.gameflowstates;

import com.github.cc3002.citricjuice.model.unit.Player;
import com.github.cc3002.citricliquid.controller.GameController;

public class TurnPhase {

  private GameController controller;

  public void setController(GameController controller) {
    this.controller = controller;
  }

  protected void changeTurnPhase(TurnPhase phase) {
    controller.setTurnPhase(phase);
  }

  void error() { throw new RuntimeException(); };

  public void startPhase() { error(); }
  public void cardPickPhase() { error(); }
  public void movingPhase() { error(); }
  public void pathChoosePhase(int steps) { error(); }
  public void combatChoosePhase(int steps) { error(); }
  public void combatResponseChoosePhase(Player attacker, int attackValue, Player target) { error(); }
  public void homeStopChoosePhase(int steps) { error(); }
  public void endPhase() { error(); };

  public Player getAttacker() { error(); return new Player("Error",1,1,1,1); }
  public int getAttackValue() { error(); return 0; }
  public int getSteps() { error(); return 0; }
  public Player getTarget() { error(); return new Player("Error",1,1,1,1); }
}
