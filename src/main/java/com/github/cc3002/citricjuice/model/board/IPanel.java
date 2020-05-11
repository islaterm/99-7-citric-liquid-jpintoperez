package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface IPanel {
    /**
     * Should return a copy of the panels that are next to it.
     */
    Set<AbstractPanel> getNextPanels();

    /**
     * Should add a new adjacent panel to the panel.
     * @param panel
     *      the panel object to be added.
     */
    void addNextPanel(final AbstractPanel panel);

    /**
     * Should return this panel's ID
     */
    int getPanelID();

    /**
     * Should perform the action of each Panel
     */
    void activatedBy(final @NotNull Player player);





}
