package com.github.cc3002.citricjuice.model.norma;

import com.github.cc3002.citricjuice.model.unit.Player;

/**
 * Enumeration representing the possible types of goals to level up a norma.
 *
 * @author <a href="mailto:ignacio.slater@ug.uchile.cl">Ignacio Slater M.</a>.
 * @version 2.0-b.1
 * @since 2.0
 */

public interface INormaGoal {

  /*

   */

  abstract boolean normaCheck(Player player);
  abstract int getRequirement();
  abstract void setRequirement(int value);
}

/*
public enum NormaGoal {
  STARS, WINS
}
*/