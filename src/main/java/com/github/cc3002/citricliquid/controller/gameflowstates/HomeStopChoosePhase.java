package com.github.cc3002.citricliquid.controller.gameflowstates;

public class HomeStopChoosePhase extends TurnPhase {
  int steps;

  public HomeStopChoosePhase(int steps) {
    this.steps = steps;
  }

  public int getSteps() {
    return steps;
  }
  @Override
  public void movingPhase() {
    changeTurnPhase(new MovingPhase());
  }

  @Override
  public void endPhase() { changeTurnPhase(new EndPhase()); }

  @Override
  public boolean isHomeStopChoosePhase() {
    return true;
  }

}
