package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;
import org.jetbrains.annotations.NotNull;

public class HomePanel extends AbstractPanel {

    /**
     * Creates a new panel
     *
     * @param panelID the unique integer identifier of this panel
     */
    public HomePanel(int panelID) {
        super(panelID);
    }

    /**
     * Restores a player's HP in 1.
     */
    public void activatedBy(final @NotNull Player player) {
        player.setCurrentHP(player.getCurrentHP() + 1);
    }
}
