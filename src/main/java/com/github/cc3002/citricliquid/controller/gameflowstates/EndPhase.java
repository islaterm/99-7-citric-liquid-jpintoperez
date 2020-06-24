package com.github.cc3002.citricliquid.controller.gameflowstates;

public class EndPhase extends TurnPhase {

  @Override
  public void startPhase() {
    changeTurnPhase(new StartPhase());
  }

  @Override
  public boolean isEndPhase() {
    return true;
  }

}
