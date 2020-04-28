package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

abstract class AbstractPanel implements IPanel {
    private final Set<IPanel> nextPanels = new HashSet<>();
    private final int panelID;

    /**
     * Returns a copy of this panel's next ones.
     */
    public Set<IPanel> getNextPanels() {
        return Set.copyOf(nextPanels);
    }

    /**
     * Adds a new adjacent panel to this one if they have a different ID.
     *
     * @param panel
     *     the panel to be added.
     */
    public void addNextPanel(final IPanel panel) {
        if (!this.equals(panel)) {
            nextPanels.add(panel);
        }
    }


    /**
     * Creates a new panel
     *
     * @param panelID
     *      the unique integer identifier of this panel
     */
    public AbstractPanel(int panelID) {
        this.panelID = panelID;
    }

    /**
     * Restores a player's HP in 1.
     */
    protected static void applyHealTo(final @NotNull Player player) {
        player.setCurrentHP(player.getCurrentHP() + 1);
    }

    /**
     * Increases the player's star count by the D6 roll multiplied by the maximum between the player's
     * norma level and three.
     */
    protected static void applyBonusTo(final @NotNull Player player) {
        player.increaseStarsBy(player.roll() * Math.min(player.getNormaLevel(), 3));
    }

    /**
     * Reduces the player's star count by the D6 roll multiplied by the player's norma level.
     */
    protected static void applyDropTo(final @NotNull Player player) {
        player.reduceStarsBy(player.roll() * player.getNormaLevel());
    }

    /**
     * Executes the appropriate action to the player according to the panel's type.
     */
    public abstract void activatedBy(final Player player);

    @Override
    public int hashCode() {
        return Objects.hash(getPanelID(), getNextPanels(), getClass());
    }

    @Override
    public int getPanelID() {
        return this.panelID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractPanel)) {
            return false;
        }
        final AbstractPanel otherPanel = (AbstractPanel) o;
        return this.getPanelID() == otherPanel.getPanelID();

    }

}
