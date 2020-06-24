package com.github.cc3002.citricliquid.controller.gameflowstates;

public class RecoveryPhase extends TurnPhase {

  public void cardPickPhase() {
    changeTurnPhase(new CardPickPhase());
  }

  public void endPhase() {
    changeTurnPhase(new EndPhase());
  }

  @Override
  public boolean isRecoveryPhase() {
    return true;
  }
}
