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
public abstract class AbstractUnit implements IUnit {
  protected final Random random;
  protected final String name;
  protected final int maxHP;
  protected int atk;
  protected int def;
  protected int evd;
  protected int normaLevel;
  protected int stars;
  private int currentHP;
  protected int wins;

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
  public AbstractUnit(final String name, final int hp, final int atk, final int def,
                      final int evd) {
    this.name = name;
    this.maxHP = currentHP = hp;
    this.atk = atk;
    this.def = def;
    this.evd = evd;

    random = new Random();
  }


  /**
   * Increases this player's win count by an amount.
   */
  public void increaseWinsBy(final int amount) {
    wins += amount;
  }
  /**
   * Increases this player's star count by an amount.
   */
  public void increaseStarsBy(final int amount) {
    stars += amount;
  }

  /**
   * Returns this player's star count.
   */
  public int getStars() {
    return stars;
  }

  public int getWins() {
    return wins;
  }


  /**
   * Set's the seed for this player's random number generator.
   * <p>
   * The random number generator is used for taking non-deterministic decisions, this method is
   * declared to avoid non-deterministic behaviour while testing the code.
   */
  public void setSeed(final long seed) {
    random.setSeed(seed);
  }

  /**
   * Returns a uniformly distributed random value in [1, 6]
   */
  public int roll() {
    return random.nextInt(6) + 1;
  }

  /**
   * Returns the character's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the character's max hit points.
   */
  public int getMaxHP() {
    return maxHP;
  }

  /**
   * Returns the current character's attack points.
   */
  public int getAtk() {
    return atk;
  }

  /**
   * Returns the current character's defense points.
   */
  public int getDef() {
    return def;
  }

  /**
   * Returns the current character's evasion points.
   */
  public int getEvd() {
    return evd;
  }

  /**
   * Performs a norma clear action; the {@code norma} counter increases in 1.
   */
  public void normaClear() {
    normaLevel++;
  }

  /**
   * Returns the current hit points of the character.
   */
  public int getCurrentHP() {
    return currentHP;
  }

  /**
   * Sets the current character's hit points.
   * <p>
   * The character's hit points have a constraint to always be between 0 and maxHP, both inclusive.
   */
  public void setCurrentHP(final int newHP) {
    this.currentHP = Math.max(Math.min(newHP, maxHP), 0);
  }

  /**
   * Reduces this player's star count by a given amount.
   * <p>
   * The star count will must always be greater or equal to 0
   */
  public void reduceStarsBy(final int amount) {
    stars = Math.max(0, stars - amount);
  }

  /*
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AbstractUnit)) {
      return false;
    }
    final AbstractUnit player = (AbstractUnit) o;
    return getMaxHP() == player.getMaxHP() &&
           getAtk() == player.getAtk() &&
           getDef() == player.getDef() &&
           getEvd() == player.getEvd() &&
           getNormaLevel() == player.getNormaLevel() &&
           getStars() == player.getStars() &&
           getCurrentHP() == player.getCurrentHP() &&
           getName().equals(player.getName());
  }
  */


  /**
   * Returns a copy of this unit.
   * Each subunit must implement this according to their unique
   * parameters.
   */
  public abstract AbstractUnit copy();

  /**
   * Returns true if the units HP is 0 and therefore is KO'd.
   */
  public boolean isKOd() {
    return (this.getCurrentHP() == 0);
  }
  /**
   * Performs an attack roll and returns the value of the roll + the
   * unit's current attack.
   */
  public int getAttackRoll() {
    int total;
    int value = roll();

    total = value + this.getAtk();

    return total;
  }

  /**
   * Performs a defense roll and returns the value of the roll + the
   * unit's current defense.
   */
  public int getDefenseRoll() {
    int total;
    int value = roll();

    total = value + this.getDef();

    return total;

  }
  /**
   * Performs an evade roll and returns the value of the roll + the
   * unit's current evasion.
   */
  public int getEvasionRoll() {
    int total;
    int value = roll();

    total = value + this.getEvd();

    return total;

  }

  /**
   * Processes the defense action against an incoming attack from other unit.
   * @param attacker The unit that's attacking this unit.
   * @param incomingDamage The value of the incoming damage to defend.
   */
  public void defendAttack(AbstractUnit attacker, int incomingDamage) {
    int total;
    int value = getDefenseRoll();

    total = Math.max(1, incomingDamage - (value));

    int previousHP = this.getCurrentHP();
    this.setCurrentHP( previousHP - total );

    if (this.isKOd()) {
      defeatedBy(attacker);
    }

  }

  /**
   * Processes the attack action against an incoming attack from other unit.
   * @param attacker The unit that's attacking this unit.
   * @param incomingDamage The value of the incoming damage to evade.
   */
  public void evadeAttack(AbstractUnit attacker, int incomingDamage) {
    int total;
    int value = getEvasionRoll();

    total = incomingDamage;
    if (value > total) {
      return;
    }

    int previousHP = this.getCurrentHP();
    this.setCurrentHP( previousHP - total );

    if (this.isKOd()) {
      defeatedBy(attacker);
    }
  }

  abstract void defeatedBy(AbstractUnit attacker);
  abstract void winAgainstPlayer(Player player);
  abstract void winAgainstWildUnit(WildUnit wildunit);
  abstract void winAgainstBossUnit(BossUnit bossunit);


}
