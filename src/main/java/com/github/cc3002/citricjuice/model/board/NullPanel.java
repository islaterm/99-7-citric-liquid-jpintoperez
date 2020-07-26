package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.unit.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NullPanel implements IPanel {
    static NullPanel nullPanel;

    /**
     *   Creates an instance of NeutralPanel.
     */

    private NullPanel() {

    }

    public static IPanel getNullPanel() {
        if (nullPanel==null){
            nullPanel = new NullPanel();
        }
        return nullPanel;
    }

    @Override
    public List<IPanel> getNextPanels() {
        return List.of();
    }

    @Override
    public List<Player> getPlayers() {
        return List.of();
    }

    @Override
    public void removePlayer(Player player) {
        return;
    }

    @Override
    public void addPlayer(Player player) {
        return;
    }

    @Override
    public void setMatrixPos(int x, int y) { }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public void addNextPanel(IPanel panel) {
        System.out.println("[Warning] NullPanel shouldn't be trying to add next panels.");
        return;
    }

    @Override
    public int getPanelID() {
        return -1;
    }

    /**
     * Does literally nothing.
     */
    public void activatedBy(final @NotNull Player player) {
        return;
    }

    @Override
    public String getSpriteString() {
        return null;
    }

    @Override
    public String getPanelDescription() {
        return "";
    }

    @Override
    public void setPanelDescription(String text) {
    }
}
