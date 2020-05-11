package com.github.cc3002.citricjuice.model.board;

import com.github.cc3002.citricjuice.model.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

abstract class AbstractPanel implements IPanel {
    private final Set<AbstractPanel> nextPanels = new HashSet<>();
    private final int panelID;

    /**
     * Returns a copy of this panel's next ones.
     */
    public Set<AbstractPanel> getNextPanels() {
        return Set.copyOf(nextPanels);
    }

    /**
     * Adds a new adjacent panel to this one if they have a different ID.
     *
     * @param panel
     *     the panel to be added.
     */
    public void addNextPanel(final AbstractPanel panel) {
        if (!this.equals(panel) && this.getPanelID()!=panel.getPanelID()) {
            boolean IDRepeated = false;
            for (AbstractPanel contained_panel: nextPanels) {
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
        System.out.println("Hello-hashCode");
        return Objects.hash(getPanelID(), getNextPanels(), getClass());
    }

    @Override
    public int getPanelID() {
        return this.panelID;
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("Hello-equals");
        boolean typeMatch;
        boolean IDMatch;
        if (this == o) {
            return true;
        }
        typeMatch = (o instanceof AbstractPanel);
        if (!typeMatch) {
            return false;
        }
        final AbstractPanel otherPanel = (AbstractPanel) o;
        IDMatch = (this.getPanelID() == otherPanel.getPanelID());
        return (IDMatch);

    }

}
