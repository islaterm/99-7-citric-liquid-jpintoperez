package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.unit.Player;
import org.jetbrains.annotations.NotNull;

public class BonusPanel extends AbstractPanel {


    /**
     * Creates a new panel
     *
     * @param panelID the unique integer identifier of this panel
     */
    public BonusPanel(int panelID) {
        super(panelID);
        setPanelDescription("This is a Bonus Panel, land here to roll a dice and get stars!");
    }

    /**
     * Increases the player's star count by the D6 roll multiplied by the maximum between the player's
     * norma level and three.
     */
    public void activatedBy(final @NotNull Player player) {
        player.increaseStarsBy(player.roll() * Math.min(player.getNormaLevel(), 3));
    }

    @Override
    public String getSpriteString() {
        return "BONUS";
    }

}
