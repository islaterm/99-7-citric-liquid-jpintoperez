package com.github.cc3002.citricliquid.controller.gameflowstates;

public class StartPhase extends TurnPhase {

  @Override
  public void cardPickPhase() {
    changeTurnPhase(new CardPickPhase());
  }

}
