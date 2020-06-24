package com.github.cc3002.citricliquid.controller.gameflowstates;

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
  public void movingPhase() {
    changeTurnPhase(new MovingPhase());
  }

  @Override
  public boolean isPathChoosePhase() {
    return true;
  }

}
