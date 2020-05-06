package com.github.cc3002.citricjuice.model;

import java.util.Random;

/**
 * This class represents a player in the game 99.7% Citric Liquid.
 *
 * @author <a href="mailto:ignacio.slater@ug.uchile.cl">Ignacio Slater
 *     Muñoz</a>.
 * @version 1.0.6-rc.3
 * @since 1.0
 */
public class Player extends AbstractUnit {

  /**
   * Creates a new character.
   *
   * @param name
   *     the character's name.
   * @param hp
   *     the initial (and max) hit points of the character.
   * @param atk
   *     the base damage the character does.
   * @param def
   *     the base defense of the character.
   * @param evd
   *     the base evasion of the character.
   */
  public Player(final String name, final int hp, final int atk, final int def,
                final int evd) {
    super(name, hp, atk, def, evd);
    normaLevel = 1;
  }


  /**
   * Returns the current norma level
   */
  public int getNormaLevel() {
    return normaLevel;
  }

  /**
   * Performs a norma clear action; the {@code norma} counter increases in 1.
   */
  public void normaClear() {
    normaLevel++;
  }

  /***
   * Checks whether this player unit is equivalent to another object.
   * @param o Object to compare
   * @return {boolean} True if this unit is equivalent to o.
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Player)) {
      return false;
    }
    final Player player = (Player) o;
    return getMaxHP() == player.getMaxHP() &&
           getAtk() == player.getAtk() &&
           getDef() == player.getDef() &&
           getEvd() == player.getEvd() &&
           getNormaLevel() == player.getNormaLevel() &&
           getStars() == player.getStars() &&
           getCurrentHP() == player.getCurrentHP() &&
           getName().equals(player.getName());
  }

  /**
   * Changes the value of this player's atk.
   * @param value to replace atk with
   */
  public void setAtk(int value) {
    this.atk = value;
  }

  /**
   * Changes the value of this player's def.
   * @param value to replace def with
   */
  public void setDef(int value) {
    this.def = value;
  }
  /**
   * Changes the value of this player's evd.
   * @param value to replace evd with
   */
  public void setEvd(int value) {
    this.evd = value;
  }


  /**
   * Returns a copy of this character.
   */
  public Player copy() {
    return new Player(name, maxHP, atk, def, evd);
  }



  @Override
  void defeatedBy(AbstractUnit attacker) {
    attacker.winAgainstPlayer(this);
  }

  @Override
  void winAgainstPlayer(Player player) {
    this.increaseWinsBy(2);
    int getStars = Math.floorDiv(player.getStars(),2);
    this.increaseStarsBy(getStars);
    player.reduceStarsBy(getStars);
  }

  @Override
  void winAgainstWildUnit(WildUnit wildunit) {
    this.increaseWinsBy(1);
    int getStars = wildunit.getStars();
    this.increaseStarsBy(getStars);
    wildunit.reduceStarsBy(getStars);
  }

  @Override
  void winAgainstBossUnit(BossUnit bossunit) {
    this.increaseWinsBy(3);
    int getStars = bossunit.getStars();
    this.increaseStarsBy(getStars);
    bossunit.reduceStarsBy(getStars);
  }
}
