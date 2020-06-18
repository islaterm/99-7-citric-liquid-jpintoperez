package com.github.cc3002.citricliquid.controller;

public class PathChoosePhase extends TurnPhase {
  int steps;


  public PathChoosePhase(int steps) {
    this.steps = steps;
  }

  @Override
  public int getSteps() {
    return steps;
  }

  @Override
  void movingPhase() {
    changeTurnPhase(new MovingPhase());
  }
}
