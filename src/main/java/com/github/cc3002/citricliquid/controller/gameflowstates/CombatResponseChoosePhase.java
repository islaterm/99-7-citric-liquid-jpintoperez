package com.github.cc3002.citricliquid.controller.gameflowstates;

import com.github.cc3002.citricjuice.model.unit.Player;

public class CombatResponseChoosePhase extends TurnPhase {

  Player attacker;
  Player target;
  int attackValue;

  protected CombatResponseChoosePhase(Player attacker, int attackValue, Player target) {
    this.attacker = attacker;
    this.target = target;
    this.attackValue = attackValue;
  }

  @Override
  public void endPhase() {
    changeTurnPhase(new EndPhase());
  }

  public Player getAttacker() {
    return attacker;
  }
  public Player getTarget() {
    return target;
  }
  public int getAttackValue() {
    return attackValue;
  }

}
