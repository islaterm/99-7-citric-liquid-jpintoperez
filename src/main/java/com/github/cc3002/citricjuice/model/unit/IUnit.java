package com.github.cc3002.citricjuice.model.unit;

/**
 * This class represents a player in the game 99.7% Citric Liquid.
 *
 * @author <a href="mailto:ignacio.slater@ug.uchile.cl">Ignacio Slater
 *     Muñoz</a>.
 * @version 1.0.6-rc.3
 * @since 1.0
 */
public interface IUnit {

    /**
     * Increases this player's star count by an amount.
     */
    void increaseStarsBy(final int amount);
    /**
     * Reduces this player's star count by a given amount.
     * <p>
     * The star count will must always be greater or equal to 0
     */
    void reduceStarsBy(final int amount);

    /**
     * Returns this player's star count.
     */
    int getStars();

    /**
     * Set's the seed for this player's random number generator.
     * <p>
     * The random number generator is used for taking non-deterministic decisions, this method is
     * declared to avoid non-deterministic behaviour while testing the code.
     */
    void setSeed(final long seed);

    /**
     * Returns a uniformly distributed random value in [1, 6]
     */
    int roll();

    /**
     * Returns the character's name.
     */
    String getName();

    /**
     * Returns the character's max hit points.
     */
    int getMaxHP();

    /**
     * Returns the current character's attack points.
     */
    int getAtk();

    /**
     * Returns the current character's defense points.
     */
    int getDef();

    /**
     * Returns the current character's evasion points.
     */
    int getEvd();

    /**
     * Returns the current hit points of the character.
     */
    int getCurrentHP();

    /**
     * Sets the current character's hit points.
     * <p>
     * The character's hit points have a constraint to always be between 0 and maxHP, both inclusive.
     */
    void setCurrentHP(final int newHP);

    /**
     * Returns a copy of this character.
     */
    IUnit copy();
}
