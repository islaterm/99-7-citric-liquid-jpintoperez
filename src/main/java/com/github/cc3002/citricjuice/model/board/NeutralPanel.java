package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.unit.Player;
import org.jetbrains.annotations.NotNull;

public class NeutralPanel extends AbstractPanel {

    /**
     *   Creates an instance of NeutralPanel.
     */

    public NeutralPanel(int panelID) { super(panelID);
        setPanelDescription("This is a Neutral Panel!");}

    /**
     * Does literally nothing.
     */
    public void activatedBy(final @NotNull Player player) {
        // nothing. truly nothingness.
    }

    @Override
    public String getSpriteString() {
        return "NEUTRAL";
    }

}
