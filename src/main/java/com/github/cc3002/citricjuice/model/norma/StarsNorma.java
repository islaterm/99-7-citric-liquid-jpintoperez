package com.github.cc3002.citricjuice.model.norma;

import com.github.cc3002.citricjuice.model.unit.Player;

public class StarsNorma extends AbstractNormaGoal {


  public StarsNorma(int stars) {
    requirement = stars;
  }

  @Override
  public boolean normaCheck(Player player) {
    int stars = player.getStars();

    if (stars >= requirement) {
      player.normaClear();
      /*
      We should ask the player which norma goal would they like to set
      but since there's no player interaction yet we'll just assume
      they'll pick same normaGoal, this must be changed in future implementations.
      */
      player.setNormaGoal(NormaFactory.getStarsNorma(player.getNormaLevel()));
      return true;
    }
    return false;

  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StarsNorma)) {
      return false;
    }
    final StarsNorma norma = (StarsNorma) o;
    return (getRequirement() == norma.getRequirement());
  }
}
