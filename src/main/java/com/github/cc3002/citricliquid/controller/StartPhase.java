package com.github.cc3002.citricliquid.controller;

public class StartPhase extends TurnPhase {

  @Override
  void cardPickPhase() {
    changeTurnPhase(new CardPickPhase());
  }

}
