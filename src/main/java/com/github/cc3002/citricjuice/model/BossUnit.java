package com.github.cc3002.citricjuice.model;

public class BossUnit extends AbstractUnit {

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
    void defeatedBy(AbstractUnit attacker) {
        attacker.winAgainstBossUnit(this);
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
        this.increaseStarsBy(3);
        int getStars = Math.floorDiv(bossunit.getStars(),2);
        this.increaseStarsBy(getStars);
        bossunit.reduceStarsBy(getStars);
    }
}
