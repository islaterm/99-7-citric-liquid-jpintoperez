package com.github.cc3002.citricliquid.controller;

public class MovingPhase extends TurnPhase {

  @Override
  void homeStopChoosePhase(int steps) {
    changeTurnPhase(new HomeStopChoosePhase(steps));
  }

  @Override
  void pathChoosePhase() {
    changeTurnPhase(new PathChoosePhase());
  }

  @Override
  void combatChoosePhase(int steps) {
    changeTurnPhase(new CombatChoosePhase(steps));
  }

  @Override
  void endPhase() {
    changeTurnPhase(new EndPhase());
  }
}
