package com.github.cc3002.citricliquid.controller.gameflowstates;

import com.github.cc3002.citricjuice.model.unit.IUnit;

public class CounterattackResponseChoosePhase extends TurnPhase {

  IUnit attacker;
  IUnit target;
  int attackValue;

  protected CounterattackResponseChoosePhase(IUnit attacker, int attackValue, IUnit target) {
    this.attacker = attacker;
    this.target = target;
    this.attackValue = attackValue;
  }

  @Override
  public void endPhase() {
    changeTurnPhase(new EndPhase());
  }

  public IUnit getAttacker() {
    return attacker;
  }
  public IUnit getTarget() {
    return target;
  }
  public int getAttackValue() {
    return attackValue;
  }

  @Override
  public boolean isCounterattackResponseChoosePhase() {
    return true;
  }

}
