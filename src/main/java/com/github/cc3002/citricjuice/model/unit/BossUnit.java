package com.github.cc3002.citricjuice.model.unit;

public class BossUnit extends AbstractUnit {

    /**
     * Creates a new boss unit.
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
    public BossUnit(final String name, final int hp, final int atk, final int def,
                    final int evd) {
        super(name, hp, atk, def, evd);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BossUnit)) {
            return false;
        }
        final BossUnit player = (BossUnit) o;
        return getMaxHP() == player.getMaxHP() &&
                getAtk() == player.getAtk() &&
                getDef() == player.getDef() &&
                getEvd() == player.getEvd() &&
                getStars() == player.getStars() &&
                getCurrentHP() == player.getCurrentHP() &&
                getName().equals(player.getName());
    }

    @Override
    public BossUnit copy() {
        return new BossUnit(name, maxHP, atk, def, evd);
    }

    @Override
    void defeatedBy(IUnit attacker) {
        attacker.winAgainstBossUnit(this);
    }


    public void winAgainstPlayer(IUnit player) {
        this.increaseWinsBy(2);
        int getStars = Math.floorDiv(player.getStars(),2);
        this.increaseStarsBy(getStars);
        player.reduceStarsBy(getStars);
    }

    @Override
    public void winAgainstWildUnit(IUnit wildunit) {
        this.increaseWinsBy(1);
        int getStars = Math.floorDiv(wildunit.getStars(),2);
        this.increaseStarsBy(getStars);
        wildunit.reduceStarsBy(getStars);
    }

    @Override
    public void winAgainstBossUnit(IUnit bossunit) {
        this.increaseWinsBy(3);
        int getStars = Math.floorDiv(bossunit.getStars(),2);
        this.increaseStarsBy(getStars);
        bossunit.reduceStarsBy(getStars);
    }
}
