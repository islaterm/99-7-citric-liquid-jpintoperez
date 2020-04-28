package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;

public class BonusPanel extends AbstractPanel {

    /**
     * Creates a new panel
     *
     * @param panelID the unique integer identifier of this panel
     */
    public BonusPanel(int panelID) {
        super(panelID);
    }

    /**
     *   Creates an instance of BonusPanel.
     */

    @Override
    public void activatedBy(Player player) { applyBonusTo(player); }
}
