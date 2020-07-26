package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.unit.Player;
import org.jetbrains.annotations.NotNull;

public class HomePanel extends AbstractPanel {

    /**
     * Creates a new panel
     *
     * @param panelID the unique integer identifier of this panel
     */
    public HomePanel(int panelID) {
        super(panelID);
        setPanelDescription("This is a Home Panel, land here to perform a Norma Check and get heal!");
    }

    /**
     * Restores a player's HP in 1.
     */
    public void activatedBy(final @NotNull Player player) {
        player.setCurrentHP(player.getCurrentHP() + 1);
        player.normaCheck();
    }

    @Override
    public String getSpriteString() {
        return "HOME";
    }
}
