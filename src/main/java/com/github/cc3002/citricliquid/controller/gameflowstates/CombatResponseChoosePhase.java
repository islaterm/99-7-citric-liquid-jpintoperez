package com.github.cc3002.citricliquid.controller.gameflowstates;

import com.github.cc3002.citricjuice.model.unit.IUnit;

public class CombatResponseChoosePhase extends TurnPhase {

  IUnit attacker;
  IUnit target;
  int attackValue;

  protected CombatResponseChoosePhase(IUnit attacker, int attackValue, IUnit target) {
    this.attacker = attacker;
    this.target = target;
    this.attackValue = attackValue;
  }

  @Override
  public void endPhase() {
    changeTurnPhase(new EndPhase());
  }

  @Override
  public void counterattackPhase(IUnit attacker) {
    changeTurnPhase(new CounterattackPhase(attacker));
  }

  @Override
  public IUnit getAttacker() {
    return attacker;
  }

  @Override
  public IUnit getTarget() {
    return target;
  }

  @Override
  public int getAttackValue() {
    return attackValue;
  }

  @Override
  public boolean isCombatResponseChoosePhase() {
    return true;
  }

}
