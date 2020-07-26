package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.unit.Player;
import org.jetbrains.annotations.NotNull;

public class BossPanel extends AbstractPanel {


    /**
     * Tells the player to force a bossEncounter by sending the forceBossEncounter notification.
     * @param player the player activating this panel.
     */
    public void activatedBy(final @NotNull Player player) {
        player.forceBossEncounter();
    }

    /**
     *   Creates an instance of BossPanel.
     */
    public BossPanel(int panelID) { super(panelID);
        setPanelDescription("This is a Boss Panel, land here to face against a powerful enemy!");}


    @Override
    public String getSpriteString() {
        return "BOSS";
    }

}
