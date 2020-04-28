package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface IPanel {
    /**
     * Should return a copy of the panels that are next to it.
     */
    Set<IPanel> getNextPanels();

    /**
     * Should add a new adjacent panel to the panel.
     * @param panel
     *      the panel object to be added.
     */
    void addNextPanel(final IPanel panel);

    /**
     * Should execute the panel effect (according to each panel type).
     * @param player
     *      the player that activates this panel.
     */
    void activatedBy(final Player player);

    /**
     * Should return this panel's ID
     */
    int getPanelID();






}
