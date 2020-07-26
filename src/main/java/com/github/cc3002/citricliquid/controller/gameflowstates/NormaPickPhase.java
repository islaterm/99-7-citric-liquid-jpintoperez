package com.github.cc3002.citricliquid.controller.gameflowstates;

public class NormaPickPhase extends TurnPhase {

  @Override
  public void endPhase() {
    changeTurnPhase(new EndPhase());
  }

  @Override
  public boolean isNormaPickPhase() {
    return true;
  }

}
