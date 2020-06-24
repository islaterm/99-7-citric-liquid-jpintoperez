package com.github.cc3002.citricliquid.controller.gameflowstates;

public class CardPickPhase extends TurnPhase {

  @Override
  public void movingPhase() {
    changeTurnPhase(new MovingPhase());
  }

  @Override
  public boolean isCardPickPhase() {
    return true;
  }

}
