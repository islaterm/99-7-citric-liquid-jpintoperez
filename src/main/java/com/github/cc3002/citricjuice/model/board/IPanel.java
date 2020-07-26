package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.unit.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IPanel {
    /**
     * Should return a copy of the panels that are next to it.
     */
    List<IPanel> getNextPanels();

    List<Player> getPlayers();

    void removePlayer(Player player);

    void addPlayer(Player player);

    void setMatrixPos(int x, int y);

    int getX();
    int getY();

    /**
     * Should add a new adjacent panel to the panel.
     * @param panel
     *      the panel object to be added.
     */
    void addNextPanel(final IPanel panel);

    /**
     * Should return this panel's ID
     */
    int getPanelID();

    /**
     * Should perform the action of each Panel
     */
    void activatedBy(final @NotNull Player player);

    /**
     * Returns the string of the panel sprite (for GUI identification).
     */
    String getSpriteString();

    /**
     * Returns a help string that describes the panel.
     */
    String getPanelDescription();

    /**
     * Returns a help string that describes the panel.
     */
    void setPanelDescription(String text);





}
