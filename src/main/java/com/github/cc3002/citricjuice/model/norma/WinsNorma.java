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
      // player.setNormaGoal(NormaFactory.getWinsNorma(player.getNormaLevel()));
      return true;
    }
    return false;
  }

  @Override
  public String getRequirementNoun() {
    return "wins";
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
