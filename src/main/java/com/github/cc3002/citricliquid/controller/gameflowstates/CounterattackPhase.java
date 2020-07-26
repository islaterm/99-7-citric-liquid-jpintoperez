package com.github.cc3002.citricliquid.controller.gameflowstates;

import com.github.cc3002.citricjuice.model.unit.IUnit;

public class CounterattackPhase extends TurnPhase {
  IUnit counterattacker;

  protected CounterattackPhase(IUnit attacker) {
    this.counterattacker = attacker;
  }

  @Override
  public IUnit getAttacker() {
    return counterattacker;
  }

  @Override
  public void counterattackResponseChoosePhase(IUnit attacker, int attackValue, IUnit target) {
    changeTurnPhase(new CounterattackResponseChoosePhase(attacker, attackValue, target));
  }

  @Override
  public boolean isCounterattackPhase() {
    return true;
  }

}
