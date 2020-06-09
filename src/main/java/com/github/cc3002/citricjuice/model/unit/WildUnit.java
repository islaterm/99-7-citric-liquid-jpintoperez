package com.github.cc3002.citricjuice.model.unit;

public class WildUnit extends AbstractUnit {

    /**
     * Creates a new wild unit.
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
    public WildUnit(final String name, final int hp, final int atk, final int def,
                    final int evd) {
        super(name, hp, atk, def, evd);
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WildUnit)) {
            return false;
        }
        final WildUnit player = (WildUnit) o;
        return getMaxHP() == player.getMaxHP() &&
                getAtk() == player.getAtk() &&
                getDef() == player.getDef() &&
                getEvd() == player.getEvd() &&
                getStars() == player.getStars() &&
                getCurrentHP() == player.getCurrentHP() &&
                getName().equals(player.getName());
    }

    @Override
    public WildUnit copy() {
        return new WildUnit(name, maxHP, atk, def, evd);
    }

    @Override
    void defeatedBy(AbstractUnit attacker) {
        attacker.winAgainstWildUnit(this);
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
        int getStars = Math.floorDiv(wildunit.getStars(),2);
        this.increaseStarsBy(getStars);
        wildunit.reduceStarsBy(getStars);
    }

    @Override
    void winAgainstBossUnit(BossUnit bossunit) {
        this.increaseWinsBy(3);
        int getStars = Math.floorDiv(bossunit.getStars(),2);
        this.increaseStarsBy(getStars);
        bossunit.reduceStarsBy(getStars);
    }
}
