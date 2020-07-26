package com.github.cc3002.citricliquid.controller.gameflowstates;

public class CardPickPhase extends TurnPhase {

  @Override
  public void movingPhase(int steps) {
    changeTurnPhase(new MovingPhase(steps));
  }

  @Override
  public boolean isCardPickPhase() {
    return true;
  }

}
