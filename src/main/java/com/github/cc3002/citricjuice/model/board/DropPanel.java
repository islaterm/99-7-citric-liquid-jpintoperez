package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;

public class DropPanel extends AbstractPanel {

    /**
     *   Creates an instance of DropPanel.
     */

    public DropPanel() {
        super(PanelType.DROP);
    }

    @Override
    public void activatedBy(Player player) {
        applyDropTo(player);
    }
}