package com.github.cc3002.citricliquid.gui.nodes;

import com.github.cc3002.citricjuice.model.board.IPanel;
import com.github.cc3002.citricliquid.gui.CitricLiquid;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.util.List;

import static com.github.cc3002.citricliquid.gui.CitricLiquid.*;

public class BoardPanel {
  Group group;
  ImageView imageView;
  CitricLiquid gui;
  String descText;
  IPanel panel;

  public BoardPanel(IPanel panel) throws FileNotFoundException {
    group = new Group();
    imageView = new ImageView(CitricLiquid.getPanelSprite(panel.getSpriteString()));
    descText = panel.getPanelDescription();
    this.panel = panel;
    imageView.setFitHeight(TILE_SIZE);
    imageView.setFitWidth(TILE_SIZE);
    imageView.setOnMouseEntered(event -> CitricLiquid.setBottomText(descText));
    imageView.setOnMouseClicked(event -> panelMouseClicked());
    group.getChildren().add(imageView);

    // Generate arrow sprites for multiple paths on a panel
    List<IPanel> nextPanels = panel.getNextPanels();
    if (nextPanels.size() > 1) {
      int panelX = panel.getX();
      int panelY = panel.getY();
      for (IPanel p : nextPanels) {
        int panel2X = p.getX();
        int panel2Y = p.getY();

        Image resource = connectedLeft;
        if (panel2X < panelX) { resource = connectedLeft; }
        if (panel2X > panelX) { resource = connectedRight; }
        if (panel2Y < panelY) { resource = connectedUp; }
        if (panel2Y > panelY) { resource = connectedDown; }

        var arrowImageView = new ImageView(resource);
        arrowImageView.setFitHeight(TILE_SIZE);
        arrowImageView.setFitWidth(TILE_SIZE);
        group.getChildren().add(arrowImageView);

      }

    }

  }

  public void setGui(CitricLiquid gui) {
    this.gui = gui;
  }

  public Group getGroup() {
    return group;
  }

  void panelMouseClicked() {
    gui.panelSelected(panel);
  }

}

