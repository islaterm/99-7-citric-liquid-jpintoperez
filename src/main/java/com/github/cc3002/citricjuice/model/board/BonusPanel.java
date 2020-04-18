package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;

public class BonusPanel extends AbstractPanel {

    /**
     *   Creates an instance of BonusPanel.
     */

    public BonusPanel() { super(PanelType.BONUS); }

    @Override
    public void activatedBy(Player player) { applyBonusTo(player); }
}
