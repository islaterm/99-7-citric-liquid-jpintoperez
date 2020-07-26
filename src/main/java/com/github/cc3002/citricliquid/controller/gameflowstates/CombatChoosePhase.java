package com.github.cc3002.citricliquid.controller.gameflowstates;

import com.github.cc3002.citricjuice.model.unit.IUnit;

public class CombatChoosePhase extends TurnPhase {
  int steps;

  public CombatChoosePhase(int steps) {
    this.steps = steps;
  }

  @Override
  public int getSteps() {
    return steps;
  }

  @Override
  public void movingPhase(int steps) {
    changeTurnPhase(new MovingPhase(steps));
  }

  @Override
  public void combatResponseChoosePhase(IUnit attacker, int attackValue, IUnit target) {
    changeTurnPhase(new CombatResponseChoosePhase(attacker, attackValue, target));
  }

  @Override
  public boolean isCombatChoosePhase() {
    return true;
  }

}
