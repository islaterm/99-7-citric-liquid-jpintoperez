package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;

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
     *   Creates an instance of AbstractPanel.
     */



    @Override
    public void activatedBy(Player player) { applyHealTo(player); }
}
