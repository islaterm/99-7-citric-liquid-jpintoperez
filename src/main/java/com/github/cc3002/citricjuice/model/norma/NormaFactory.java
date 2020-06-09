package com.github.cc3002.citricjuice.model.norma;

public class NormaFactory {

  /**
   * Generates an instance of StarsNorma that will behave as the
   * goal state that a player must reach in order to complete a norma clear.
   * @param level
   * Receives the current norma level to determine which should be the
   * next goal.
   */
  public static StarsNorma getStarsNorma(int level) {
    switch (level){
      case 1:
        return new StarsNorma(10);
      case 2:
        return new StarsNorma(30);
      case 3:
        return new StarsNorma(70);
      case 4:
        return new StarsNorma(120);
      case 5:
        return new StarsNorma(200);
      default:
        return new StarsNorma(9999);
    }
  }

  public static WinsNorma getWinsNorma(int level) {
    switch (level){
      case 2:
        return new WinsNorma(2);
      case 3:
        return new WinsNorma(5);
      case 4:
        return new WinsNorma(9);
      case 5:
        return new WinsNorma(14);
      default:
        return new WinsNorma(9999);
    }
  }

}
