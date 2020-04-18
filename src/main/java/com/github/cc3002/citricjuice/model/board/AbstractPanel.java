package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

abstract class AbstractPanel implements IPanel {
    private final PanelType type;
    private final Set<IPanel> nextPanels = new HashSet<>();

    /**
     * Should return the according PanelType and might vary between
     * each subclass.
     */
    public PanelType getType() { return type; }

    /**
     * Returns a copy of this panel's next ones.
     */
    public Set<IPanel> getNextPanels() {
        return Set.copyOf(nextPanels);
    }

    /**
     * Adds a new adjacent panel to this one.
     *
     * @param panel
     *     the panel to be added.
     */
    public void addNextPanel(final IPanel panel) {
        nextPanels.add(panel);
    }


    /**
     * Creates a new panel
     *
     * @param type
     *      the type of the panel
     */
    protected AbstractPanel(final PanelType type) {
        this.type = type;
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



}
