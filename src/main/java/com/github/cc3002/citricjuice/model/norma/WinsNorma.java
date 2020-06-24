package com.github.cc3002.citricjuice.model.norma;

import com.github.cc3002.citricjuice.model.unit.Player;

public class WinsNorma extends AbstractNormaGoal {

  public WinsNorma(int wins) {
    requirement = wins;
  }

  @Override
  public boolean normaCheck(Player player) {
    int wins = player.getWins();

    if (wins >= requirement) {
      player.normaClear();
      /*
      We should ask the player which norma goal would they like to set
      but since there's no player interaction yet we'll just assume
      they'll pick same normaGoal, this must be changed in future implementations.
       */
      player.setNormaGoal(NormaFactory.getWinsNorma(player.getNormaLevel()));
      return true;
    }
    return false;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof WinsNorma)) {
      return false;
    }
    final WinsNorma norma = (WinsNorma) o;
    return (getRequirement() == norma.getRequirement());
  }

}
