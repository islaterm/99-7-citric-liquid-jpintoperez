package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;
import org.jetbrains.annotations.NotNull;

public class EncounterPanel extends AbstractPanel {


    /**
     * Should start an encounter.
     * @param player the player activating this panel.
     */
    public void activatedBy(final @NotNull Player player) {
        // not implemented yet
    }


    /**
     *   Creates an instance of EncounterPanel.
     */

    public EncounterPanel(int panelID) {
        super(panelID); }


}
