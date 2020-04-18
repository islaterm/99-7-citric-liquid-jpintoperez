package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;

public class NeutralPanel extends AbstractPanel {

    /**
     *   Creates an instance of NeutralPanel.
     */

    public NeutralPanel() { super(PanelType.NEUTRAL); }

    @Override
    public void activatedBy(Player player) {
        /* true nothingness */
        /* at least of what we know, neutral panels don't do anything when landing
            on them so in the meanwhile this remains empty.
         */
    }
}
