package com.github.cc3002.citricliquid.controller.gameflowstates;

import com.github.cc3002.citricjuice.model.unit.Player;

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
  public void movingPhase() {
    changeTurnPhase(new MovingPhase());
  }

  @Override
  public void combatResponseChoosePhase(Player attacker, int attackValue, Player target) {
    changeTurnPhase(new CombatResponseChoosePhase(attacker, attackValue, target));
  }
}
