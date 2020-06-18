package com.github.cc3002.citricliquid.controller;

public class HomeStopChoosePhase extends TurnPhase {
  int steps;

  public HomeStopChoosePhase(int steps) {
    this.steps = steps;
  }

  public int getSteps() {
    return steps;
  }
  @Override
  void movingPhase() {
    changeTurnPhase(new MovingPhase());
  }

  @Override
  void endPhase() { changeTurnPhase(new EndPhase()); }

}
