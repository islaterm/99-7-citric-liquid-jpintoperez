package com.github.cc3002.citricliquid.controller;

public class PathChoosePhase extends TurnPhase {

  @Override
  void movingPhase() {
    changeTurnPhase(new MovingPhase());
  }
}
