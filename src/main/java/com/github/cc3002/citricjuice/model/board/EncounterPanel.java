package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.unit.Player;
import org.jetbrains.annotations.NotNull;

public class EncounterPanel extends AbstractPanel {


    /**
     * Tells the player to force an encounter by sending the forceEncounter notification.
     * @param player the player activating this panel.
     */
    public void activatedBy(final @NotNull Player player) {
        player.forceEncounter();
    }


    /**
     * Creates an instance of EncounterPanel.
     */

    public EncounterPanel(int panelID) {
        super(panelID);
        setPanelDescription("This is an Encounter Panel, land here to engage in battle with a wild enemy!");
    }

    @Override
    public String getSpriteString() {
        return "ENCOUNTER";
    }

}
