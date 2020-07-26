package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.unit.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPanel implements IPanel {
    private final List<IPanel> nextPanels = new ArrayList<>();
    private final int panelID;
    private List<Player> players = new ArrayList<>();
    String panelDescriptionText;
    private int X;
    private int Y;

    public void setMatrixPos(int x, int y) {
        X = x;
        Y = y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public String getPanelDescription() {
        return panelDescriptionText;
    }

    public void setPanelDescription(String text) {
        this.panelDescriptionText = text;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns a copy of this panel's next ones.
     */
    public List<IPanel> getNextPanels() {
        return List.copyOf(nextPanels);
    }

    /**
     * Adds a new adjacent panel to this one if they have a different ID.
     *
     * @param panel
     *     the panel to be added.
     */
    public void addNextPanel(final IPanel panel) {
        if (!this.equals(panel) && this.getPanelID()!=panel.getPanelID()) {
            boolean IDRepeated = false;
            for (IPanel contained_panel: nextPanels) {
                if (contained_panel.equals(panel)) {
                    IDRepeated = true;
                    break;
                }
            }
            if (!IDRepeated) {
                nextPanels.add(panel);
            }
        }
    }

    /**
     * Executes the appropriate action to the player according to the panel's type.
     */
    public abstract void activatedBy(final @NotNull Player player);

    /**
     * Creates a new panel
     *
     * @param panelID
     *      the unique integer identifier of this panel
     */
    public AbstractPanel(int panelID) {
        this.panelID = panelID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPanelID(), getNextPanels().size(), getClass());
    }

    @Override
    public int getPanelID() {
        return this.panelID;
    }

    @Override
    public boolean equals(Object o) {
        boolean typeMatch;
        boolean IDMatch;
        if (this == o) {
            return true;
        }
        typeMatch = (o instanceof IPanel);
        if (!typeMatch) {
            return false;
        }
        final IPanel otherPanel = (IPanel) o;
        IDMatch = (this.getPanelID() == otherPanel.getPanelID());
        return (IDMatch);

    }

}
