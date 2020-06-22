package com.github.cc3002.citricliquid.controller.gameflowstates;

public class MovingPhase extends TurnPhase {

  @Override
  public void homeStopChoosePhase(int steps) {
    changeTurnPhase(new HomeStopChoosePhase(steps));
  }

  @Override
  public void pathChoosePhase(int steps) {
    changeTurnPhase(new PathChoosePhase(steps));
  }

  @Override
  public void combatChoosePhase(int steps) {
    changeTurnPhase(new CombatChoosePhase(steps));
  }

  @Override
  public void endPhase() {
    changeTurnPhase(new EndPhase());
  }
}
