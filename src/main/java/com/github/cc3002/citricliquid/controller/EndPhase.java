package com.github.cc3002.citricliquid.controller;

public class EndPhase extends TurnPhase {

  @Override
  void startPhase() {
    changeTurnPhase(new StartPhase());
  }

}
