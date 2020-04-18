package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;

public class HomePanel extends AbstractPanel {

    /**
     *   Creates an instance of AbstractPanel.
     */

    public HomePanel() { super(PanelType.HOME); }

    @Override
    public void activatedBy(Player player) { applyHealTo(player); }
}
