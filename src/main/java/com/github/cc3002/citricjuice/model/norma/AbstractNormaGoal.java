package com.github.cc3002.citricjuice.model.norma;
import com.github.cc3002.citricjuice.model.unit.Player;


public abstract class AbstractNormaGoal implements INormaGoal {

  int requirement;

  public abstract boolean normaCheck(Player player);

  public int getRequirement() {
    return requirement;
  }
}

